package com.shipgig.thegun.pos.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.adapter.AboutOutOfStockReportsAdapter;
import com.shipgig.thegun.pos.model.AboutOutOfStockReportsModel;
import com.shipgig.thegun.pos.utilities.Sharepreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AboutOutOfStockReportsActivity extends Activity implements View.OnClickListener{


    ArrayList<AboutOutOfStockReportsModel> aboutOutOfStockReportModels = new ArrayList<>();
    RecyclerView recyclerView;
    Context context;
    ArrayList<String> list;



    AboutOutOfStockReportsAdapter aboutOutOfStockReportsAdapter;
    ImageView backarrow;
    private SQLiteDatabase mydatabase;
    JSONArray jsonArray;// = new JSONArray();
    private AboutOutOfStockReportsModel aboutOutOfStockReportModel;
    Cursor cursor;

    TextView nextBtn, prevBtn;
    TextView pageNumber;
    private Button[] btns;
    private int noOfBtns;
    private int currentPage;

    public  int TOTAL_NUM_ITEMS;
    public  int ITEMS_PER_PAGE=10;
    public int ITEMS_REMAINING;
    public  int LAST_PAGE;
    private int totalPages=0;
    int storeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.about_out_of_stock_reports);

        storeID = Sharepreference.getSharedPreferenceInt(AboutOutOfStockReportsActivity.this,"storeID",0);

        init();

        nextBtn.setVisibility(View.GONE);
        prevBtn.setVisibility(View.GONE);

        context = this;
        list= new ArrayList<>();

        seeOutStockdataweekly();

        try {
            TOTAL_NUM_ITEMS = jsonArray.length();
        } catch (NullPointerException e) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        }

        Btnfooter();
        loadList(0);
        CheckBtnBackGroud(0);


    }


    public void recycler () {

        aboutOutOfStockReportsAdapter = new AboutOutOfStockReportsAdapter(aboutOutOfStockReportModels, context);
        aboutOutOfStockReportsAdapter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(aboutOutOfStockReportsAdapter);
    }

    private void init() {

        backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(this);
        pageNumber = findViewById(R.id.number);
        nextBtn =  findViewById(R.id.next);
        prevBtn =  findViewById(R.id.previous);
        recyclerView = findViewById(R.id.recylerview);
        mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/newinventory.db", null, 0);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backarrow:
                finish();
                break;
        }
    }




    @SuppressLint("WrongConstant")
    private void seeOutStockdataweekly() {
        try {

            cursor = mydatabase.rawQuery( "SELECT product_ID, productName, unitPrice, costPrice, totalstock, expiryDate\n" +
                    " FROM tbl_product where totalstock > 0 AND totalstock <= 4 AND store_ID="+storeID+" ORDER BY totalstock and createdOn BETWEEN  datetime('now','-7 day') AND datetime('now','localtime')",null);
            jsonArray = cur2Json(cursor);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setDataAdptr(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                aboutOutOfStockReportModel= new AboutOutOfStockReportsModel();
                aboutOutOfStockReportModel.setSrno(String.valueOf(i));
                aboutOutOfStockReportModel.setProductId(jsonobject.optString("product_ID"));//String.valueOf(jsonobject.optInt("product_ID"));
                aboutOutOfStockReportModel.setProductName(jsonobject.optString("productName"));
                aboutOutOfStockReportModel.setUnitPrice(jsonobject.optString("unitPrice"));
                aboutOutOfStockReportModel.setStock(jsonobject.optString("totalstock"));
                aboutOutOfStockReportModel.setExpiryDate(jsonobject.optString("expiryDate"));
                aboutOutOfStockReportModels.add(aboutOutOfStockReportModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public JSONArray cur2Json(Cursor cursor) {
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),
                                cursor.getString(i));
                    } catch (Exception e) {
                        Log.e("thegun", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;

    }

    int startItem;
    int numOfData;
    public ArrayList<AboutOutOfStockReportsModel> generatePage(int currentPage, JSONArray jsonArray) {
        startItem=currentPage*ITEMS_PER_PAGE;//+1;
        numOfData=ITEMS_PER_PAGE;
        aboutOutOfStockReportModels= new ArrayList<>();
        if (currentPage==LAST_PAGE && ITEMS_REMAINING>0)
        {
            for (int i=startItem;i<startItem+ITEMS_REMAINING;i++)
            {
                JSONObject jsonobject = null;
                try {
                    jsonobject = jsonArray.getJSONObject(i);
                    aboutOutOfStockReportModel= new AboutOutOfStockReportsModel();
                    aboutOutOfStockReportModel.setSrno(String.valueOf(i));
                    aboutOutOfStockReportModel.setProductId(jsonobject.optString("product_ID"));//String.valueOf(jsonobject.optInt("product_ID"));
                    aboutOutOfStockReportModel.setProductName(jsonobject.optString("productName"));
                    aboutOutOfStockReportModel.setUnitPrice(jsonobject.optString("unitPrice"));
                    aboutOutOfStockReportModel.setStock(jsonobject.optString("totalstock"));
                    aboutOutOfStockReportModel.setExpiryDate(jsonobject.optString("expiryDate"));
                    aboutOutOfStockReportModels.add(aboutOutOfStockReportModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }else
        {
            for (int i=startItem;i<startItem+numOfData;i++)
            {
                JSONObject jsonobject = null;
                try {
                    jsonobject = jsonArray.getJSONObject(i);
                    aboutOutOfStockReportModel= new AboutOutOfStockReportsModel();
                    aboutOutOfStockReportModel.setSrno(String.valueOf(i));
                    aboutOutOfStockReportModel.setProductId(jsonobject.optString("product_ID"));//String.valueOf(jsonobject.optInt("product_ID"));
                    aboutOutOfStockReportModel.setProductName(jsonobject.optString("productName"));
                    aboutOutOfStockReportModel.setUnitPrice(jsonobject.optString("unitPrice"));
                    aboutOutOfStockReportModel.setStock(jsonobject.optString("totalstock"));
                    aboutOutOfStockReportModel.setExpiryDate(jsonobject.optString("expiryDate"));
                    aboutOutOfStockReportModels.add(aboutOutOfStockReportModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return aboutOutOfStockReportModels;
    }


    private void Btnfooter() {
        int val = TOTAL_NUM_ITEMS%ITEMS_PER_PAGE;
        val = val==0 ? 0:1;

        noOfBtns=TOTAL_NUM_ITEMS/ITEMS_PER_PAGE+val;

        LinearLayout ll = findViewById(R.id.btnLay);

        btns  = new Button[noOfBtns];



        for(int i=0;i<noOfBtns;i++)
        {
            btns[i] =   new Button(context);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            btns[i].setText(""+(i+1));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.addView(btns[i], lp);

            int finalI1 = i;

            int finalI = i;

            final int j = i;
            btns[j].setOnClickListener(new View.OnClickListener() {

                public void onClick(View v)
                {
                    loadList(j);
                    CheckBtnBackGroud(j);
                }
            });
        }

    }

    private void CheckBtnBackGroud(int index) {
        pageNumber.setText("Page "+(index+1)+" of "+noOfBtns);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                new LinearLayout.LayoutParams(55,45)
        );
        params.setMargins(5, 0, 0, 0);

        for(int i=0;i<noOfBtns;i++)
        {
            if(i==index)
            {
                btns[i].setBackground(getResources().getDrawable(R.drawable.pagignation_button_border));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
                btns[i].setBackgroundResource(R.drawable.pagignation_button_border);
                btns[i].setBackgroundColor(getResources().getColor(R.color.humburg_icon_color));
                btns[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, 15);
                btns[i].setLayoutParams(params);
            }
            else
            {
                btns[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.pagignation_button_border));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
                btns[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, 15);
                btns[i].setLayoutParams(params);
            }
        }

    }

    private void loadList(int number) {
        ArrayList<AboutOutOfStockReportsModel> sort = new ArrayList<>();

        int start = number * ITEMS_PER_PAGE;
        for(int i=start;i<(start)+ITEMS_PER_PAGE;i++)
        {
            if(i<aboutOutOfStockReportModels.size())
            {
                sort.add(aboutOutOfStockReportModels.get(i));

            }
            else
            {
                break;
            }
        }
        aboutOutOfStockReportsAdapter = new AboutOutOfStockReportsAdapter(sort,context);
        aboutOutOfStockReportsAdapter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(aboutOutOfStockReportsAdapter);

    }

}
