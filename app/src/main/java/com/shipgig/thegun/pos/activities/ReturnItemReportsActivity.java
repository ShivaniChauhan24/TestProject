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
import com.shipgig.thegun.pos.adapter.ReturnItemReportsAdapter;
import com.shipgig.thegun.pos.model.ReturnItemReportsModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ReturnItemReportsActivity extends Activity implements View.OnClickListener{


    ArrayList<ReturnItemReportsModel> returnItemReportModels = new ArrayList<>();
    RecyclerView recyclerView;
    Context context;

    ReturnItemReportsAdapter returnItemAdapter;
    ImageView backarrow;
    private JSONArray jsonArray;
    Cursor cursor;
    SQLiteDatabase mydatabase;
    ReturnItemReportsModel returnItemReportsModel;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.return_items);

        init();
//        initCustomSpinner();

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
        mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/newinventory.db", null, 0);

    }

    public void recycler () {

        returnItemAdapter = new ReturnItemReportsAdapter(returnItemReportModels, context);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(returnItemAdapter);
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
        // Spinner Drop down elements
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
                            returnItemReportModels.clear();
                            seeReturndataDaily();
                            returnItemReportModels.add(returnItemReportsModel);
                            recycler();

                            Log.d("call from monthley","Daily");
                        } else if (item == "Weekly") {
                            returnItemReportModels.clear();
                            seeReturndataWeekly();
                            returnItemReportModels.add(returnItemReportsModel);
                            recycler();
                            Log.d("call from weekly","weekly");
                        }

                        else if (item == "Monthly") {
                            returnItemReportModels.clear();
                            seeReturndataMonthly();
                            returnItemReportModels.add(returnItemReportsModel);
                            recycler();
                            Log.d("call from weekly","weekly");
                        }  else if (item == "Yearly") {
                            returnItemReportModels.clear();
                            seeReturndataYearly();
                            returnItemReportModels.add(returnItemReportsModel);
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
    private void seeReturndataDaily() {
        try {

            cursor = mydatabase.rawQuery( "SELECT transaction_ID, custUser_ID, paymentMode, returnDate from tbl_transaction WHERE isDeleted = 0 and store_ID = ? and transactionType = \"Sales\" and isReturned = 1 and createdOn  BETWEEN  datetime('now','-1 day') AND datetime('now','localtime')",null);
            jsonArray = cur2Json(cursor);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void seeReturndataWeekly() {
        try {

            cursor = mydatabase.rawQuery( "SELECT transaction_ID, custUser_ID, paymentMode, returnDate from tbl_transaction WHERE isDeleted = 0 and store_ID = ? and transactionType = \"Sales\" and isReturned = 1 and createdOn  BETWEEN  datetime('now','-7 day') AND datetime('now','localtime')",null);
            jsonArray = cur2Json(cursor);
            Log.e("returnitemthegun", "customer data " + jsonArray);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void seeReturndataMonthly() {
        try {

            cursor = mydatabase.rawQuery( "SELECT transaction_ID, custUser_ID, paymentMode, returnDate from tbl_transaction WHERE isDeleted = 0 and store_ID = ? and transactionType = \"Sales\" and isReturned = 1 and createdOn  BETWEEN  datetime('now','-1 month') AND datetime('now','localtime')",null);

            jsonArray = cur2Json(cursor);
            Log.e("returnitemthegun", "customer data " + jsonArray);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void seeReturndataYearly() {
        try {

            cursor = mydatabase.rawQuery( "SELECT transaction_ID, custUser_ID, paymentMode, returnDate from tbl_transaction/* WHERE isDeleted = 0 and store_ID = ? and transactionType = \"Sales\" and isReturned = 1 and createdOn  BETWEEN  datetime('now','-1 year') AND datetime('now','localtime')*/",null);

            jsonArray = cur2Json(cursor);
            Log.e("returnitemthegun", "customer data " + jsonArray);
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
                returnItemReportsModel = new ReturnItemReportsModel();
                returnItemReportsModel.setSrno(String.valueOf(i));
                returnItemReportsModel.setTransactionId(String.valueOf(jsonobject.optInt("transaction_ID")));
                returnItemReportsModel.setCustomerId(jsonobject.optString("custUser_ID"));
                returnItemReportsModel.setPaymentMode(jsonobject.optString("paymentMode"));
                returnItemReportsModel.setReturnDate(jsonobject.optString("returnDate"));
                returnItemReportModels.add(returnItemReportsModel);

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
    public ArrayList<ReturnItemReportsModel> generatePage(int currentPage, JSONArray jsonArray)
    {
        startItem=currentPage*ITEMS_PER_PAGE;//+1;
        numOfData=ITEMS_PER_PAGE;
        returnItemReportModels= new ArrayList<>();
        if (currentPage==LAST_PAGE && ITEMS_REMAINING>0)
        {
            for (int i=startItem;i<startItem+ITEMS_REMAINING;i++)
            {
                JSONObject jsonobject = null;
                try {
                    jsonobject = jsonArray.getJSONObject(i);
                    returnItemReportsModel = new ReturnItemReportsModel();
                    returnItemReportsModel.setSrno(String.valueOf(i));
                    returnItemReportsModel.setTransactionId(String.valueOf(jsonobject.optInt("transaction_ID")));
                    returnItemReportsModel.setCustomerId(jsonobject.optString("custUser_ID"));
                    returnItemReportsModel.setPaymentMode(jsonobject.optString("paymentMode"));
                    returnItemReportsModel.setReturnDate(jsonobject.optString("returnDate"));
                    returnItemReportModels.add(returnItemReportsModel);

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
                    returnItemReportsModel = new ReturnItemReportsModel();
                    returnItemReportsModel.setSrno(String.valueOf(i));
                    returnItemReportsModel.setTransactionId(String.valueOf(jsonobject.optInt("transaction_ID")));
                    returnItemReportsModel.setCustomerId(jsonobject.optString("custUser_ID"));
                    returnItemReportsModel.setPaymentMode(jsonobject.optString("paymentMode"));
                    returnItemReportsModel.setReturnDate(jsonobject.optString("returnDate"));
                    returnItemReportModels.add(returnItemReportsModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return returnItemReportModels;
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
        ArrayList<ReturnItemReportsModel> sort = new ArrayList<>();

        int start = number * ITEMS_PER_PAGE;
        for(int i=start;i<(start)+ITEMS_PER_PAGE;i++)
        {
            if(i<returnItemReportModels.size())
            {
                sort.add(returnItemReportModels.get(i));


            }
            else
            {
                break;
            }
        }
        returnItemAdapter = new ReturnItemReportsAdapter(sort,context);
        returnItemAdapter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(returnItemAdapter);

    }

}
