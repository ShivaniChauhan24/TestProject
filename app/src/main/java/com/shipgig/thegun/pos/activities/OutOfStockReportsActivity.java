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
import com.shipgig.thegun.pos.adapter.OutOfStockReportsAdapter;
import com.shipgig.thegun.pos.model.OutOfStockReportsModel;
import com.shipgig.thegun.pos.utilities.Sharepreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OutOfStockReportsActivity extends Activity implements View.OnClickListener {


    ArrayList<OutOfStockReportsModel> outOfStockReportModels = new ArrayList<>();
    RecyclerView recyclerView;
    Context context;

    OutOfStockReportsAdapter outOfStockReportsAdapter;
    ImageView backarrow;
    private JSONArray jsonArray;

    TextView nextBtn, prevBtn;
    TextView pageNumber;
    private Button[] btns;
    private int noOfBtns;
    private int currentPage;

    public  int TOTAL_NUM_ITEMS;
    public  int ITEMS_PER_PAGE=7;
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
        setContentView(R.layout.out_of_stock_reports);

        storeID = Sharepreference.getSharedPreferenceInt(OutOfStockReportsActivity.this,"storeID",0);


        init();
        seeExpensedataAll();
        context = this;
        nextBtn.setVisibility(View.GONE);
        prevBtn.setVisibility(View.GONE);

        outOfStockReportsAdapter = new OutOfStockReportsAdapter(outOfStockReportModels, context);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(outOfStockReportsAdapter);

        try {
            TOTAL_NUM_ITEMS = jsonArray.length();
        } catch (NullPointerException e) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        }


        Btnfooter();
        loadList(0);
        CheckBtnBackGroud(0);




    }

    private void toggleButtons() {
        if (currentPage == totalPages) {
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(true);
        } else if (currentPage == 0) {
            prevBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        } else if (currentPage >= 1 && currentPage <= totalPages) {
            nextBtn.setEnabled(true);
            prevBtn.setEnabled(true);
        }
    }

    private void init() {

        pageNumber = findViewById(R.id.number);
        nextBtn =  findViewById(R.id.next);
        prevBtn =  findViewById(R.id.previous);
        backarrow =  findViewById(R.id.backarrow);
        backarrow.setOnClickListener(this);
        recyclerView =  findViewById(R.id.recylerview);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backarrow:
                finish();
                break;
        }
    }


    @SuppressLint("WrongConstant")
    private void seeExpensedataAll() {
        try {
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() +
                    "/newinventory.db", null, 0);

            Cursor cursor = mydatabase.rawQuery("SELECT product_ID, productName, unitPrice, costPrice, totalstock, expiryDate  FROM tbl_product WHERE  totalstock = 0 AND store_ID="+storeID, null);
            jsonArray = cur2Json(cursor);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setDataAdptr(JSONArray jsonArray) {
        // int srNo =1;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);

                OutOfStockReportsModel outOfStockReportsModel = new OutOfStockReportsModel();
                outOfStockReportsModel.setSrno(String.valueOf(i));
                outOfStockReportsModel.setProductId(jsonobject.getString("product_ID"));
                outOfStockReportsModel.setProductName(jsonobject.getString("productName"));
                outOfStockReportsModel.setUnitPrice(jsonobject.getString("unitPrice"));
                outOfStockReportsModel.setStock(jsonobject.getString("totalstock"));
                outOfStockReportsModel.setExpiryDate(jsonobject.getString("expiryDate"));
                outOfStockReportModels.add(outOfStockReportsModel);

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

    public ArrayList<OutOfStockReportsModel> generatePage(int currentPage, JSONArray jsonArray) {
        startItem = currentPage * ITEMS_PER_PAGE;//+1;
        numOfData = ITEMS_PER_PAGE;
        outOfStockReportModels = new ArrayList<>();
        if (currentPage == LAST_PAGE && ITEMS_REMAINING > 0) {
            for (int i = startItem; i < startItem + ITEMS_REMAINING; i++) {
                JSONObject jsonobject = null;
                try {
                    jsonobject = jsonArray.getJSONObject(i);
                    // Toast.makeText(getContext(), "cutomer value of database::" + jsonobject.toString(), Toast.LENGTH_SHORT).show();
                    OutOfStockReportsModel outOfStockReportsModel = new OutOfStockReportsModel();
                    outOfStockReportsModel.setSrno(String.valueOf(i));
                    outOfStockReportsModel.setProductId(jsonobject.getString("product_ID"));
                    outOfStockReportsModel.setProductName(jsonobject.getString("productName"));
                    outOfStockReportsModel.setUnitPrice(jsonobject.getString("unitPrice"));
                    outOfStockReportsModel.setStock(jsonobject.getString("totalstock"));
                    outOfStockReportsModel.setExpiryDate(jsonobject.getString("expiryDate"));
                    outOfStockReportModels.add(outOfStockReportsModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } else {
            for (int i = startItem; i < startItem + numOfData; i++) {
                JSONObject jsonobject = null;
                try {
                    jsonobject = jsonArray.getJSONObject(i);
                    OutOfStockReportsModel outOfStockReportsModel = new OutOfStockReportsModel();
                    outOfStockReportsModel.setSrno(String.valueOf(i));
                    outOfStockReportsModel.setProductId(jsonobject.getString("product_ID"));
                    outOfStockReportsModel.setProductName(jsonobject.getString("productName"));
                    outOfStockReportsModel.setUnitPrice(jsonobject.getString("unitPrice"));
                    outOfStockReportsModel.setStock(jsonobject.getString("totalstock"));
                    outOfStockReportsModel.setExpiryDate(jsonobject.getString("expiryDate"));
                    outOfStockReportModels.add(outOfStockReportsModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return outOfStockReportModels;

    }

    private void Btnfooter() {
        int val = TOTAL_NUM_ITEMS%ITEMS_PER_PAGE;
        val = val==0 ? 0:1;


        //val = val==0?0:1;
        noOfBtns=TOTAL_NUM_ITEMS/ITEMS_PER_PAGE+val;

        LinearLayout ll = (LinearLayout)findViewById(R.id.btnLay);

        btns    =new Button[noOfBtns];



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
        ArrayList<OutOfStockReportsModel> sort = new ArrayList<>();

        int start = number * ITEMS_PER_PAGE;
        for(int i=start;i<(start)+ITEMS_PER_PAGE;i++)
        {
            if(i<outOfStockReportModels.size())
            {
                sort.add(outOfStockReportModels.get(i));

            }
            else
            {
                break;
            }
        }
        outOfStockReportsAdapter = new OutOfStockReportsAdapter(sort,context);
        outOfStockReportsAdapter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(outOfStockReportsAdapter);

    }
}
