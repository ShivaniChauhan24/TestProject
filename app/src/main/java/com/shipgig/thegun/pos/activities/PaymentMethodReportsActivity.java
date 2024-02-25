package com.shipgig.thegun.pos.activities;

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
import com.shipgig.thegun.pos.adapter.PaymentMethodReportsAdapter;
import com.shipgig.thegun.pos.model.PaymentMethodReportsModel;
import com.shipgig.thegun.pos.utilities.Sharepreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class PaymentMethodReportsActivity extends Activity implements View.OnClickListener{

    ArrayList<PaymentMethodReportsModel> paymentMethodReportModels = new ArrayList<>();
    RecyclerView recyclerView;
    Context context;
    PaymentMethodReportsModel paymentMethodReportsModel;
    PaymentMethodReportsAdapter paymentMethodReportsAdapter;
    ImageView backarrow;
    private JSONArray jsonArray;
    private Cursor cursor;
    SQLiteDatabase mydatabase;
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
        setContentView(R.layout.payment_method_reports);

        init();
//        initCustomSpinner();
        seePaymentModedataDaily();

        context = this;
        nextBtn.setVisibility(View.GONE);
        prevBtn.setVisibility(View.GONE);

        storeID = Sharepreference.getSharedPreferenceInt(this,"storeID",0);

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


    public void recycler() {
        paymentMethodReportsAdapter = new PaymentMethodReportsAdapter(paymentMethodReportModels, context);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(paymentMethodReportsAdapter);
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
        gender.add("Hourly");
        gender.add("Daily");
        gender.add("Weekly");
        gender.add("Monthly");

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
                        paymentMethodReportModels.clear();
                        seePaymentModedataDaily();
                        paymentMethodReportModels.add(paymentMethodReportsModel);
                        recycler();

                        Log.d("call from monthley","Daily");
                    } else if (item == "Weekly") {
                        paymentMethodReportModels.clear();
                        seePaymentModedataWeekly();
                        paymentMethodReportModels.add(paymentMethodReportsModel);
                        recycler();
                        Log.d("call from weekly","weekly");
                    }

                    else if (item == "Monthly") {
                        paymentMethodReportModels.clear();
                        seePaymentModedataMonthly();
                        paymentMethodReportModels.add(paymentMethodReportsModel);
                        recycler();
                        Log.d("call from weekly","weekly");
                    }  else if (item == "Hourly") {
                        paymentMethodReportModels.clear();
                        seePaymentModedataHourly();
                        paymentMethodReportModels.add(paymentMethodReportsModel);
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

    private void seePaymentModedataHourly() {
        try {

            cursor = mydatabase.rawQuery( "SELECT transaction_ID,discount,totalAmount,CGST,specialDiscount,paymentMode,totalQty,createdOn  FROM tbl_transaction WHERE/* isDeleted = 0 AND store_ID = ?  AND transactionType = \"Sales\"  AND isReturned = 0 AND isVoid = 0  AND*/ createdOn BETWEEN datetime(\"now\",\"localtime\",\"-60 minutes\") AND datetime(\"now\",\"localtime\") ORDER BY createdOn DESC",null);
            jsonArray = cur2Json(cursor);
            Log.e("returnitemthegun", "customer data " + jsonArray);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void seePaymentModedataDaily() {
        try {

            cursor = mydatabase.rawQuery( "SELECT transaction_ID,discount,itemCount,totalAmount,CGST,specialDiscount,paymentMode,totalQty,createdOn  FROM tbl_transaction WHERE/* isDeleted = 0 AND store_ID = ?  AND transactionType = \"Sales\"  AND isReturned = 0 AND isVoid = 0  AND*/ createdOn BETWEEN datetime(\"now\",\"localtime\",\"-1 day\") AND datetime(\"now\",\"localtime\") ORDER BY createdOn DESC",null);
            jsonArray = cur2Json(cursor);
            Log.e("returnitemthegun", "customer data " + jsonArray);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void seePaymentModedataWeekly() {
        try {

            cursor = mydatabase.rawQuery( "SELECT transaction_ID,discount,totalAmount,GST,SGST,CGST,specialDiscount,paymentMode,totalQty,createdOn  FROM tbl_transaction WHERE/* isDeleted = 0 AND store_ID = ?  AND transactionType = \"Sales\"  AND isReturned = 0 AND isVoid = 0  AND*/ createdOn BETWEEN datetime(\"now\",\"localtime\",\"-7 day\") AND datetime(\"now\",\"localtime\") ORDER BY createdOn DESC",null);

            jsonArray = cur2Json(cursor);
            Log.e("returnitemthegun", "customer data " + jsonArray);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void seePaymentModedataMonthly() {
        try {


            cursor = mydatabase.rawQuery( "SELECT transaction_ID,discount,totalAmount,GST,SGST,CGST,specialDiscount,paymentMode,totalQty,createdOn  FROM tbl_transaction WHERE/* isDeleted = 0 AND store_ID = ?  AND transactionType = \"Sales\"  AND isReturned = 0 AND isVoid = 0  AND*/ createdOn BETWEEN datetime(\"now\",\"localtime\",\"-1 month\") AND datetime(\"now\",\"localtime\") ORDER BY createdOn DESC",null);

            jsonArray = cur2Json(cursor);
            Log.e("returnitemthegun", "customer data " + jsonArray);
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

                paymentMethodReportsModel = new PaymentMethodReportsModel();
                paymentMethodReportsModel.setSrno(String.valueOf(i));
                paymentMethodReportsModel.setDisc(jsonobject.getString("discount"));
                paymentMethodReportsModel.setPyMode(jsonobject.getString("paymentMode"));
                paymentMethodReportsModel.setCgst(jsonobject.optString("CGST"));
                paymentMethodReportsModel.setTotalamount(jsonobject.getString("totalAmount"));
                paymentMethodReportsModel.setQuantity(jsonobject.getString("itemCount"));
                paymentMethodReportsModel.setDate(jsonobject.getString("createdOn"));
                paymentMethodReportModels.add(paymentMethodReportsModel);

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
    public ArrayList<PaymentMethodReportsModel> generatePage(int currentPage, JSONArray jsonArray) {
        startItem=currentPage*ITEMS_PER_PAGE;//+1;
        numOfData=ITEMS_PER_PAGE;
        paymentMethodReportModels= new ArrayList<>();
        if (currentPage==LAST_PAGE && ITEMS_REMAINING>0)
        {
            for (int i=startItem;i<startItem+ITEMS_REMAINING;i++)
            {
                JSONObject jsonobject = null;
                try {
                    jsonobject = jsonArray.getJSONObject(i);
                    paymentMethodReportsModel = new PaymentMethodReportsModel();
                    paymentMethodReportsModel.setSrno(String.valueOf(i));
                    paymentMethodReportsModel.setDisc(jsonobject.getString("discount"));
                    paymentMethodReportsModel.setPyMode(jsonobject.getString("paymentMode"));
                    paymentMethodReportsModel.setCgst(jsonobject.optString("CGST"));
                    paymentMethodReportsModel.setTotalamount(jsonobject.optString("totalAmount"));
                    paymentMethodReportsModel.setQuantity(jsonobject.optString("itemCount"));
                    paymentMethodReportsModel.setDate(jsonobject.optString("createdOn"));
                    paymentMethodReportModels.add(paymentMethodReportsModel);

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
                    paymentMethodReportsModel = new PaymentMethodReportsModel();
                    paymentMethodReportsModel.setSrno(String.valueOf(i));
                    paymentMethodReportsModel.setDisc(jsonobject.getString("discount"));
                    paymentMethodReportsModel.setPyMode(jsonobject.getString("paymentMode"));
                    paymentMethodReportsModel.setCgst(jsonobject.optString("CGST"));
                    paymentMethodReportsModel.setTotalamount(jsonobject.optString("totalAmount"));
                    paymentMethodReportsModel.setQuantity(jsonobject.optString("itemCount"));
                    paymentMethodReportsModel.setDate(jsonobject.optString("createdOn"));
                    paymentMethodReportModels.add(paymentMethodReportsModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return paymentMethodReportModels;
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
        ArrayList<PaymentMethodReportsModel> sort = new ArrayList<>();

        int start = number * ITEMS_PER_PAGE;
        for(int i=start;i<(start)+ITEMS_PER_PAGE;i++)
        {
            if(i<paymentMethodReportModels.size())
            {
                sort.add(paymentMethodReportModels.get(i));

            }
            else
            {
                break;
            }
        }
        paymentMethodReportsAdapter = new PaymentMethodReportsAdapter(sort,context);
        paymentMethodReportsAdapter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(paymentMethodReportsAdapter);

    }

}
