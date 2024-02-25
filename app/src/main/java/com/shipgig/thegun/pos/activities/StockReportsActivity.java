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

import com.shipgig.thegun.pos.MainActivity;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.adapter.AboutOutOfStockReportsAdapter;
import com.shipgig.thegun.pos.adapter.StockReportsAdapter;
import com.shipgig.thegun.pos.model.AboutOutOfStockReportsModel;
import com.shipgig.thegun.pos.model.StockReportsModel;
import com.shipgig.thegun.pos.utilities.Sharepreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StockReportsActivity extends Activity implements View.OnClickListener{


    ArrayList<StockReportsModel> stockReportModels = new ArrayList<>();
    RecyclerView recyclerView;
    Context context;

    StockReportsAdapter stockReportsAdapter;
    ImageView backarrow;
    JSONArray jsonArray;

    TextView nextBtn, prevBtn;
    TextView pageNumber;
    private Button[] btns;
    private int noOfBtns;
    private int currentPage;

    public  int TOTAL_NUM_ITEMS;
    public  int ITEMS_PER_PAGE=6;
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
        setContentView(R.layout.stock_reports);

        storeID = Sharepreference.getSharedPreferenceInt(StockReportsActivity.this,"storeID",0);
        init();
        context = this;
        nextBtn.setVisibility(View.GONE);
        prevBtn.setVisibility(View.GONE);

        seeExpensedataAll();
        stockReportsAdapter = new StockReportsAdapter(stockReportModels, context);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(stockReportsAdapter);

        try {
            TOTAL_NUM_ITEMS = jsonArray.length();
        } catch (NullPointerException e) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        }
        ITEMS_REMAINING=TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
        LAST_PAGE=TOTAL_NUM_ITEMS/ITEMS_PER_PAGE;
        totalPages = TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;

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
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Stock ReportsModel");*/
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
    private void seeExpensedataAll() {
        try {
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() +
                    "/newinventory.db", null, 0);
            Cursor cursor = mydatabase.rawQuery("SELECT product_ID, productName, unitPrice, totalstock costPrice, totalstock, expiryDate  FROM tbl_product WHERE totalstock > 0 AND store_ID="+ storeID, null);
            jsonArray = cur2Json(cursor);
            Log.e("itemdatathegun", "customer data " + jsonArray);
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
                StockReportsModel stockReportsModel = new StockReportsModel();
                stockReportsModel.setSrno(String.valueOf(i));
                stockReportsModel.setProductId(jsonobject.getString("product_ID"));
                stockReportsModel.setProductName(jsonobject.getString("productName"));
                stockReportsModel.setUnitPrice(jsonobject.getString("unitPrice"));
                stockReportsModel.setStock(jsonobject.getString("totalstock"));
                stockReportsModel.setExpiryDate(jsonobject.getString("expiryDate"));
                stockReportModels.add(stockReportsModel);

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
    public ArrayList<StockReportsModel> generatePage(int currentPage, JSONArray jsonArray)
    {
        startItem=currentPage*ITEMS_PER_PAGE;//+1;
        numOfData=ITEMS_PER_PAGE;
        stockReportModels= new ArrayList<>();
        if (currentPage==LAST_PAGE && ITEMS_REMAINING>0)
        {
            for (int i=startItem;i<startItem+ITEMS_REMAINING;i++)
            {
                JSONObject jsonobject = null;
                try {
                    jsonobject = jsonArray.getJSONObject(i);
                    StockReportsModel stockReportsModel = new StockReportsModel();
                    stockReportsModel.setSrno(String.valueOf(i));
                    stockReportsModel.setProductId(jsonobject.getString("product_ID"));
                    stockReportsModel.setProductName(jsonobject.getString("productName"));
                    stockReportsModel.setUnitPrice(jsonobject.getString("unitPrice"));
                    stockReportsModel.setStock(jsonobject.getString("totalstock"));
                    stockReportsModel.setExpiryDate(jsonobject.getString("expiryDate"));
                    stockReportModels.add(stockReportsModel);

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
                    StockReportsModel stockReportsModel = new StockReportsModel();
                    stockReportsModel.setSrno(String.valueOf(i));
                    stockReportsModel.setProductId(jsonobject.getString("product_ID"));
                    stockReportsModel.setProductName(jsonobject.getString("productName"));
                    stockReportsModel.setUnitPrice(jsonobject.getString("unitPrice"));
                    stockReportsModel.setStock(jsonobject.getString("totalstock"));
                    stockReportsModel.setExpiryDate(jsonobject.getString("expiryDate"));
                    stockReportModels.add(stockReportsModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return stockReportModels;
    }

    private void Btnfooter() {
        int val = TOTAL_NUM_ITEMS%ITEMS_PER_PAGE;
        val = val==0 ? 0:1;

        noOfBtns=TOTAL_NUM_ITEMS/ITEMS_PER_PAGE+val;

        LinearLayout ll = findViewById(R.id.btnLay);

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
            /*btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadList(finalI);
                    CheckBtnBackGroud(finalI);
                }
            });*/

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
                new LinearLayout.LayoutParams(45,55)
        );
        params.setMargins(5, 0, 0, 0);

        for(int i=0;i<noOfBtns;i++)
        {
            if(i==index)
            {
                //btns[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.pagignation_button_border));
                btns[i].setBackground(getResources().getDrawable(R.drawable.pagignation_button_border));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
                btns[i].setBackgroundResource(R.drawable.pagignation_button_border);
                btns[i].setBackgroundColor(getResources().getColor(R.color.humburg_icon_color));
                //btns[i].setLayoutParams(new LinearLayout.LayoutParams(40, 50));
                btns[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, 15);
                btns[i].setLayoutParams(params);
                //btns[i].setMa
            }
            else
            {
                btns[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.pagignation_button_border));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
                //btns[i].setLayoutParams(new LinearLayout.LayoutParams(40, 50));
                btns[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, 15);
                btns[i].setLayoutParams(params);
            }
        }

    }

    private void loadList(int number) {
        ArrayList<StockReportsModel> sort = new ArrayList<>();

        int start = number * ITEMS_PER_PAGE;
        for(int i=start;i<(start)+ITEMS_PER_PAGE;i++)
        {
            if(i<stockReportModels.size())
            {
                sort.add(stockReportModels.get(i));
                //seeCustomerEditdata();

            }
            else
            {
                break;
            }
        }
        stockReportsAdapter = new StockReportsAdapter(sort,context);
        stockReportsAdapter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(stockReportsAdapter);

    }


}
