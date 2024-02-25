package com.shipgig.thegun.pos.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.shipgig.thegun.pos.adapter.InStockReportsAdapter;
import com.shipgig.thegun.pos.model.InStockReportsModel;
import com.shipgig.thegun.pos.utilities.Sharepreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InStockReportsActivity extends Activity implements View.OnClickListener{


    ArrayList<InStockReportsModel> inStockReportsModels= new ArrayList<>();
    RecyclerView recyclerView;
    Context context;

    InStockReportsAdapter inStockReportsAdapter;
    ImageView backarrow;
    private JSONArray jsonArray;

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
        setContentView(R.layout.in_stock_reports);


        storeID = Sharepreference.getSharedPreferenceInt(InStockReportsActivity.this,"storeID",0);

        init();

        nextBtn.setVisibility(View.GONE);
        prevBtn.setVisibility(View.GONE);

        seeInStockdataAll();
        context = this;
        //inStockReportsModels = reports();
        inStockReportsAdapter = new InStockReportsAdapter(inStockReportsModels, context);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(inStockReportsAdapter);


        try {
            TOTAL_NUM_ITEMS = jsonArray.length();
        } catch (NullPointerException e) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        }



        Btnfooter();
        loadList(0);
        CheckBtnBackGroud(0);

    }

    private void init() {
        pageNumber = findViewById(R.id.number);
        nextBtn =  findViewById(R.id.next);
        prevBtn =  findViewById(R.id.previous);
        backarrow = (ImageView)findViewById(R.id.backarrow);
        backarrow.setOnClickListener(this);
        recyclerView = (RecyclerView)findViewById(R.id.recylerview);

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
    private void seeInStockdataAll() {
        try {
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() +
                    "/newinventory.db", null, 0);

            Cursor cursor = mydatabase.rawQuery("SELECT product_ID, productName, unitPrice, costPrice, totalstock, expiryDate  FROM tbl_product WHERE totalstock > 0 AND store_ID="+storeID+"", null);
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

                InStockReportsModel inStockReportsModel = new InStockReportsModel();
                inStockReportsModel.setSrno( String.valueOf(i));
                inStockReportsModel.setProductId(jsonobject.getString("product_ID"));
                inStockReportsModel.setProductName(jsonobject.getString("productName"));
                inStockReportsModel.setUnitPrice(jsonobject.getString("unitPrice"));
                inStockReportsModel.setCostPrice(jsonobject.getString("costPrice"));
                inStockReportsModel.setExpiryDate(jsonobject.getString("expiryDate"));
                inStockReportsModels.add(inStockReportsModel);

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
        ArrayList<InStockReportsModel> sort = new ArrayList<>();

        int start = number * ITEMS_PER_PAGE;
        for(int i=start;i<(start)+ITEMS_PER_PAGE;i++)
        {
            if(i<inStockReportsModels.size())
            {
                sort.add(inStockReportsModels.get(i));

            }
            else
            {
                break;
            }
        }
        inStockReportsAdapter = new InStockReportsAdapter(sort,context);
        inStockReportsAdapter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(inStockReportsAdapter);

    }

}