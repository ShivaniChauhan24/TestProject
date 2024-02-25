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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.adapter.CustomSpinnerAdapter;
import com.shipgig.thegun.pos.adapter.ProfitReportsAdapter;
import com.shipgig.thegun.pos.model.ProfitReportsModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfitReportsActivity extends Activity implements View.OnClickListener{


    ArrayList<ProfitReportsModel> profitReportModels = new ArrayList<>();
    RecyclerView recyclerView;
    Context context;
    ProfitReportsModel profitReportsModel;
    ProfitReportsAdapter profitReportsAdapter;
    ImageView backarrow;

    private SQLiteDatabase mydatabase;
    private Cursor cursor;
    private JSONArray jsonArray;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.profit_reports);

        init();
        initCustomSpinner();

        context = this;
        nextBtn.setVisibility(View.GONE);
        prevBtn.setVisibility(View.GONE);


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
        backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(this);
        pageNumber = findViewById(R.id.number);
        nextBtn =  findViewById(R.id.next);
        prevBtn =  findViewById(R.id.previous);
        recyclerView = findViewById(R.id.recylerview);

        mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() +
                "/newinventory.db", null, 0);

    }

    public void recycler () {
        profitReportsAdapter = new ProfitReportsAdapter(profitReportModels, context);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(profitReportsAdapter);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backarrow:
                finish();
                break;
        }
    }

    private void initCustomSpinner() {

        Spinner spinnerCustom= (Spinner) findViewById(R.id.transactionReportsSpinner);
        ArrayList<String> gender = new ArrayList<String>();
        gender.add("Choose ReportsModel");
        gender.add("Daily");
        gender.add("Weekly");
        gender.add("Monthly");
        gender.add("Yearly");
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(this,gender);
        spinnerCustom.setAdapter(customSpinnerAdapter);
        spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                if(position > 0){
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + item, Toast.LENGTH_SHORT)
                            .show();

                    if(item == "Daily") {
                        profitReportModels.clear();
                        seeProfitDataDaily();
                        profitReportModels.add(profitReportsModel);
                        recycler();
                        Log.d("call from monthley","Daily");
                    } else if (item == "Weekly") {
                        profitReportModels.clear();
                        seeProfitDataWeekly();
                        profitReportModels.add(profitReportsModel);
                        recycler();
                        Log.d("call from weekly","weekly");
                    }

                    else if (item == "Monthly") {
                        profitReportModels.clear();
                        seeProfitDataMonthly();
                        profitReportModels.add(profitReportsModel);
                        recycler();
                        Log.d("call from weekly","weekly");
                    }  else if (item == "Yearly") {
                        profitReportModels.clear();
                        seeProfitDataYearly();
                        profitReportModels.add(profitReportsModel);
                        recycler();
                        Log.d("call from weekly","weekly");
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @SuppressLint("WrongConstant")
    private void seeProfitDataYearly() {
        try {

            cursor = mydatabase.rawQuery("SELECT tbl_transaction.transaction_ID, tbl_transaction.custUser_ID, (tbl_transaction.totalAmount - tbl_transaction.GST)- SUM(tbl_product.costPrice) as Profit, SUM(tbl_product.costPrice)" +
                            " as cost , tbl_transaction.GST as GST, tbl_transaction.totalAmount as SaleTotal from tbl_transaction " +
                            " JOIN tbl_invoice  on tbl_transaction.transaction_ID = tbl_invoice.transaction_ID JOIN tbl_product " +
                            "on tbl_product.product_ID = tbl_invoice.product_ID" ,null);

            Log.d("tableexpenseThegun", cursor.toString());

            jsonArray = cur2Json(cursor);
            Log.e("itemdatathegun", "customer data " + jsonArray);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("WrongConstant")
    private void seeProfitDataDaily() {
        try {

            cursor = mydatabase.rawQuery("SELECT tt.transaction_ID, tt.custUser_ID, (tt.totalAmount - tt.GST)- SUM(tp.costPrice) as Profit, SUM(tp.costPrice)" +
                    " as cost , tt.GST as GST, tt.totalAmount as SaleTotal from tbl_transaction tt" +
                    " JOIN tbl_invoice ti on tt.transaction_ID = ti.transaction_ID JOIN tbl_product tp " +
                    "on tp.product_ID = ti.product_ID and tp.costPrice is NOT NULL WHERE tt.isDeleted = 0 and tt.store_ID = ? " +
                    "and tt.transactionType = \"Sales\" and ti.isReturned = 0 and tt.isVoid = 0 " +
                    "and tt.createdOn BETWEEN datetime(\"now\",\"-1 month\") AND datetime(\"now\",\"localtime\")" +
                    " GROUP BY tt.transaction_ID", null);

            jsonArray = cur2Json(cursor);
            Log.e("itemdatathegun", "customer data " + jsonArray);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("WrongConstant")
    private void seeProfitDataWeekly() {
        try {

            cursor = mydatabase.rawQuery("SELECT tt.transaction_ID, tt.custUser_ID, (tt.totalAmount - tt.GST)- SUM(tp.costPrice) as Profit, SUM(tp.costPrice)" +
                    " as cost , tt.GST as GST, tt.totalAmount as SaleTotal from tbl_transaction tt" +
                    " JOIN tbl_invoice ti on tt.transaction_ID = ti.transaction_ID JOIN tbl_product tp " +
                    "on tp.product_ID = ti.product_ID and tp.costPrice is NOT NULL WHERE tt.isDeleted = 0 and tt.store_ID = ? " +
                    "and tt.transactionType = \"Sales\" and ti.isReturned = 0 and tt.isVoid = 0 " +
                    "and tt.createdOn BETWEEN datetime(\"now\",\"-1 month\") AND datetime(\"now\",\"localtime\")" +
                    " GROUP BY tt.transaction_ID", null);

            jsonArray = cur2Json(cursor);
            Log.e("itemdatathegun", "customer data " + jsonArray);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("WrongConstant")
    private void seeProfitDataMonthly() {
        try {

            cursor = mydatabase.rawQuery("SELECT tt.transaction_ID, tt.custUser_ID, (tt.totalAmount - tt.GST)- SUM(tp.costPrice) as Profit, SUM(tp.costPrice)" +
                    " as cost , tt.GST as GST, tt.totalAmount as SaleTotal from tbl_transaction tt" +
                    " JOIN tbl_invoice ti on tt.transaction_ID = ti.transaction_ID JOIN tbl_product tp " +
                    "on tp.product_ID = ti.product_ID and tp.costPrice is NOT NULL WHERE tt.isDeleted = 0 and tt.store_ID = ? " +
                    "and tt.transactionType = \"Sales\" and ti.isReturned = 0 and tt.isVoid = 0 " +
                    "and tt.createdOn BETWEEN datetime(\"now\",\"-1 month\") AND datetime(\"now\",\"localtime\")" +
                    " GROUP BY tt.transaction_ID", null);

            jsonArray = cur2Json(cursor);
            Log.e("itemdatathegun", "customer data " + jsonArray);
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

                profitReportsModel = new ProfitReportsModel();
                profitReportsModel.setSrno(String.valueOf(i));
                profitReportsModel.setTransactionId(jsonobject.getString("transaction_ID"));
                profitReportsModel.setCustomerId(jsonobject.getString("custUser_ID"));
                profitReportsModel.setSaleTotal(jsonobject.getString("SaleTotal"));
                profitReportsModel.setProfit(jsonobject.getString("Profit"));
                profitReportModels.add(profitReportsModel);

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
    public ArrayList<ProfitReportsModel> generatePage(int currentPage, JSONArray jsonArray)
    {
        startItem=currentPage*ITEMS_PER_PAGE;//+1;
        numOfData=ITEMS_PER_PAGE;
        profitReportModels= new ArrayList<>();
        if (currentPage==LAST_PAGE && ITEMS_REMAINING>0)
        {
            for (int i=startItem;i<startItem+ITEMS_REMAINING;i++)
            {
                JSONObject jsonobject = null;
                try {
                    jsonobject = jsonArray.getJSONObject(i);
                    profitReportsModel = new ProfitReportsModel();
                    profitReportsModel.setSrno(String.valueOf(i));
                    profitReportsModel.setTransactionId(jsonobject.getString("transaction_ID"));
                    profitReportsModel.setCustomerId(jsonobject.getString("custUser_ID"));
                    profitReportsModel.setSaleTotal(jsonobject.getString("SaleTotal"));
                    profitReportsModel.setProfit(jsonobject.getString("Profit"));
                    profitReportModels.add(profitReportsModel);

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
                    profitReportsModel = new ProfitReportsModel();
                    profitReportsModel.setSrno(String.valueOf(i));
                    profitReportsModel.setTransactionId(jsonobject.getString("transaction_ID"));
                    profitReportsModel.setCustomerId(jsonobject.getString("custUser_ID"));
                    profitReportsModel.setSaleTotal(jsonobject.getString("SaleTotal"));
                    profitReportsModel.setProfit(jsonobject.getString("Profit"));
                    profitReportModels.add(profitReportsModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return profitReportModels;
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
        ArrayList<ProfitReportsModel> sort = new ArrayList<>();

        int start = number * ITEMS_PER_PAGE;
        for(int i=start;i<(start)+ITEMS_PER_PAGE;i++)
        {
            if(i<profitReportModels.size())
            {
                sort.add(profitReportModels.get(i));

            }
            else
            {
                break;
            }
        }
        profitReportsAdapter = new ProfitReportsAdapter(sort,context);
        profitReportsAdapter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(profitReportsAdapter);

    }

}
