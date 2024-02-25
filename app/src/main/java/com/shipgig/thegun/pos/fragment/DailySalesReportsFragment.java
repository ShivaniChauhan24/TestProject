package com.shipgig.thegun.pos.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shipgig.thegun.pos.MainActivity;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.adapter.CustomSpinnerAdapter;
import com.shipgig.thegun.pos.adapter.DailySalesReportsAdapter;
import com.shipgig.thegun.pos.adapter.TransactionReportsAdapter;
import com.shipgig.thegun.pos.model.DailySalesReportsModel;
import com.shipgig.thegun.pos.model.ReportsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.shipgig.thegun.pos.MainActivity.drawer;


public class DailySalesReportsFragment extends Fragment {

    View myView;
    DailySalesReportsAdapter dailySalesReportsAdapter;
    ArrayList<DailySalesReportsModel> dailySalesReportsModels = new ArrayList<>();



    ArrayList<ReportsModel> tReports = new ArrayList<>();
    ImageView open_drawer;
    RecyclerView recyclerView;
    Context context;
    //  Spinner transactionReportsSpinner;
    ArrayList<String> list;
    TransactionReportsAdapter transactionReportsAdapter;
    ImageView backarrow;
    private SQLiteDatabase mydatabase;
    private Cursor cursor;
    JSONArray jsonArray = new JSONArray();
    ReportsModel reportsModel;

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
    String creatOn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.daily_sales_reports, container, false);

        init();
        open_drawer = myView.findViewById(R.id.open_drawer);
        open_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });

        context = getActivity();
        seeExpensedataAll();

        nextBtn.setVisibility(View.GONE);
        prevBtn.setVisibility(View.GONE);

        transactionReportsAdapter = new TransactionReportsAdapter(tReports, context,1);
        transactionReportsAdapter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(transactionReportsAdapter);

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        creatOn = currentDate + "" + currentTime;


        try {
            TOTAL_NUM_ITEMS = jsonArray.length();
        } catch (NullPointerException e) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        }


        Btnfooter();
        loadList(0);
        CheckBtnBackGroud(0);

        return myView;
    }

    private void init() {
        backarrow = (ImageView)myView.findViewById(R.id.backarrow);
        recyclerView = (RecyclerView)myView.findViewById(R.id.recylerview);
        pageNumber = myView.findViewById(R.id.number);
        nextBtn =  myView.findViewById(R.id.next);
        prevBtn =  myView.findViewById(R.id.previous);
        try {

            mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getContext().getPackageName() + "/newinventory.db", null, 0);
        }catch (SQLiteCantOpenDatabaseException e)
        {
            Toast.makeText(getActivity(), "First initialize to create database", Toast.LENGTH_SHORT).show();
        }


    }



    @SuppressLint("WrongConstant")
    private void seeExpensedataAll() {
        try {

            mydatabase.beginTransaction();
            cursor = mydatabase.rawQuery("SELECT discount,totalAmount,CGST,itemCount,transaction_ID,createdOn,customer_name,paymentMode,custUser_ID from tbl_transaction where isDeleted=0", null);
            mydatabase.endTransaction();
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
                reportsModel = new ReportsModel();
                reportsModel.setSrno(String.valueOf(i));
                reportsModel.setDisc(jsonobject.getString("discount"));
                reportsModel.setPaymode(jsonobject.getString("paymentMode"));
                reportsModel.setCgst(jsonobject.getString("CGST"));
                reportsModel.setTransactionId(jsonobject.getString("transaction_ID"));
                reportsModel.setQuantity(jsonobject.getString("itemCount"));
                reportsModel.setTotalamount(jsonobject.getString("totalAmount"));
                reportsModel.setDate(jsonobject.getString("createdOn"));
                reportsModel.setCustomerId(jsonobject.getString("custUser_ID"));
                reportsModel.setCustomer_name(jsonobject.getString("customer_name"));
                tReports.add(reportsModel);

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
    public ArrayList<ReportsModel> generatePage(int currentPage, JSONArray jsonArray)
    {
        startItem=currentPage*ITEMS_PER_PAGE;//+1;
        numOfData=ITEMS_PER_PAGE;
        tReports= new ArrayList<>();
        if (currentPage==LAST_PAGE && ITEMS_REMAINING>0)
        {
            for (int i=startItem;i<startItem+ITEMS_REMAINING;i++)
            {
                JSONObject jsonobject = null;
                try {
                    jsonobject = jsonArray.getJSONObject(i);
                    reportsModel = new ReportsModel();
                    reportsModel.setSrno(String.valueOf(i));
                    reportsModel.setDisc(jsonobject.getString("discount"));
                    reportsModel.setPaymode(jsonobject.getString("paymentMode"));
                    reportsModel.setCgst(jsonobject.getString("CGST"));
                    reportsModel.setSgst(jsonobject.getString("SGST"));
                    reportsModel.setQuantity(jsonobject.getString("itemCount"));
                    reportsModel.setTotalamount(jsonobject.getString("totalAmount"));
                    reportsModel.setDate(jsonobject.getString("createdOn"));
                    reportsModel.setTransactionId(jsonobject.getString("transaction_ID"));
                    reportsModel.setCustomerId(jsonobject.getString("custUser_ID"));
                    // reportsModel.time = jsonobject.getString("DATETIME");//created_On
                    tReports.add(reportsModel);

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
                    reportsModel = new ReportsModel();
                    reportsModel.setSrno(String.valueOf(i));
                    reportsModel.setDisc(jsonobject.getString("discount"));
                    reportsModel.setPaymode(jsonobject.getString("paymentMode"));
                    reportsModel.setCgst(jsonobject.getString("CGST"));
                    reportsModel.setSgst(jsonobject.getString("SGST"));
                    reportsModel.setQuantity(jsonobject.getString("itemCount"));
                    reportsModel.setTotalamount(jsonobject.getString("totalAmount"));
                    reportsModel.setDate(jsonobject.getString("createdOn"));
                    reportsModel.setTransactionId(jsonobject.getString("transaction_ID"));
                    reportsModel.setCustomerId(jsonobject.getString("custUser_ID"));
                    // reportsModel.time = jsonobject.getString("DATETIME");//created_On
                    tReports.add(reportsModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return tReports;
    }

    private void Btnfooter() {
        int val = TOTAL_NUM_ITEMS%ITEMS_PER_PAGE;
        val = val==0 ? 0:1;
        noOfBtns=TOTAL_NUM_ITEMS/ITEMS_PER_PAGE+val;

        LinearLayout ll = myView.findViewById(R.id.btnLay);

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
        ArrayList<ReportsModel> sort = new ArrayList<>();

        int start = number * ITEMS_PER_PAGE;
        for(int i=start;i<(start)+ITEMS_PER_PAGE;i++)
        {
            if(i<tReports.size())
            {
                sort.add(tReports.get(i));

            }
            else
            {
                break;
            }
        }
        transactionReportsAdapter = new TransactionReportsAdapter(sort,context,1);
        transactionReportsAdapter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(transactionReportsAdapter);

    }



}
