package com.shipgig.thegun.pos.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.activities.InStockReportsActivity;
import com.shipgig.thegun.pos.activities.NewCustomerActivity;
import com.shipgig.thegun.pos.activities.OutOfStockReportsActivity;
import com.shipgig.thegun.pos.activities.PaymentModeActivity;
import com.shipgig.thegun.pos.activities.StockReportsActivity;
import com.shipgig.thegun.pos.activities.TransactionReportsActivity;
import com.shipgig.thegun.pos.adapter.LastSalesReportsAdapter;
import com.shipgig.thegun.pos.adapter.LastTenReportAdapter;
import com.shipgig.thegun.pos.model.LastSalesReportsModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.shipgig.thegun.pos.model.ReportsModel;
import com.shipgig.thegun.pos.utilities.Sharepreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.shipgig.thegun.pos.MainActivity.drawer;

public class DashBoardFragment extends Fragment implements View.OnClickListener {

    Context context;
    View rootView;
    LinearLayout llPaymentcash, llNewcustomer, llOutofstock, llInstock, llPaymentcard, llPaymentcredit;
    ProgressBar totalSalesBar;
    TextView totalSales, totalPaymentCash, totalPaymentCard,fullReports, totalPaymentPaytm, txtNewCustomer, txtInStock,txtOutStock;
    ArrayList<LastSalesReportsModel> lastSalesReportsModelArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    BarChart barChart;
    ArrayList<String> list;
    LastSalesReportsAdapter lastSalesReportsAdapter;
    private JSONArray jsonArray = new JSONArray();
    String paymentMode, noOfNewCustomer, noOfInStock,noOfOutStock;
    Double totalSale;
    TextView tvTotalInner,title_TotalAmount;
    private ImageView refreshSale;
    SQLiteDatabase mydatabase;

    // last ten report
    ReportsModel reportsModel;
    private Cursor cursor;
    ArrayList<ReportsModel> tReports = new ArrayList<>();
    LastTenReportAdapter transactionReportsAdapter;
    String mTotalSale;
    ImageView open_drawer;

    public Double totalPaymentCashView,totalPaymentPaytmView,totalPaymentCardView;
    Double finalAddedValue;
    int storeID;

    TextView inventory_valueTxt;
    String totalInventoryValueinDB;
    String forinventory = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dashboard_fragment, container, false);
        context = getActivity();
        setHasOptionsMenu(true);
        storeID = Sharepreference.getSharedPreferenceInt(getActivity(),"storeID",0);
        findViewsById();

        drawBarChart();

        open_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });




        // lastSalesReportsModelArrayList = reports();
//        recyclerView = (RecyclerView) rootView.findViewById(R.id.recylerview);
//        lastSalesReportsAdapter = new LastSalesReportsAdapter(lastSalesReportsModelArrayList, context);
//        lastSalesReportsAdapter.notifyDataSetChanged();
//        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(verticalLayoutManager);
//        recyclerView.setAdapter(lastSalesReportsAdapter);


        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.
                            INPUT_METHOD_SERVICE);
                    if (getActivity().getCurrentFocus() != null) {
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                }
                return true;
            }
        });

        seeExpensedataAll();

        recyclerView = rootView.findViewById(R.id.recylerview);
        transactionReportsAdapter = new LastTenReportAdapter(tReports, context);
        transactionReportsAdapter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(transactionReportsAdapter);



        return rootView;
    }

    private void drawBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(16f, 4));
        entries.add(new BarEntry(18f, 5));
        entries.add(new BarEntry(4f, 6));
        entries.add(new BarEntry(18f, 7));
        entries.add(new BarEntry(10f, 8));
        entries.add(new BarEntry(18f, 9));
        entries.add(new BarEntry(8f, 10));
        entries.add(new BarEntry(18f, 11));

        BarDataSet dataset = new BarDataSet(entries, "# Of Sales Total");
        dataset.setColors(new int[]{ContextCompat.getColor(context, R.color.chart1), ContextCompat.getColor(context, R.color.chart2)});

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Jan");
        labels.add("Feb");
        labels.add("Mar");
        labels.add("Apr");
        labels.add("May");
        labels.add("Jun");
        labels.add("Jul");
        labels.add("Aug");
        labels.add("Sep");
        labels.add("Oct");
        labels.add("Nov");
        labels.add("Dec");

        BarData data = new BarData(labels, dataset);
        barChart.setData(data);
    }

    private void findViewsById() {
        barChart = (BarChart) rootView.findViewById(R.id.barchart);
        open_drawer = rootView.findViewById(R.id.open_drawer);
        llPaymentcash = rootView.findViewById(R.id.ll_paymentcash);
        llNewcustomer = rootView.findViewById(R.id.ll_newcustomer);
        llOutofstock = rootView.findViewById(R.id.ll_outofstock);
        llInstock = rootView.findViewById(R.id.ll_instock);
        llPaymentcard = rootView.findViewById(R.id.ll_paymentcard);
        llPaymentcredit = rootView.findViewById(R.id.ll_paymentcredit);
        refreshSale = rootView.findViewById(R.id.refresh_sale);
        totalSales = (TextView) rootView.findViewById(R.id.totalsales);
        totalSalesBar = rootView.findViewById(R.id.circularProgressbar);
        fullReports = rootView.findViewById(R.id.full_reports);
        totalPaymentCash = rootView.findViewById(R.id.total_payment_cash);
        totalPaymentCard = rootView.findViewById(R.id.total_payment_card);
        totalPaymentPaytm = rootView.findViewById(R.id.total_payment_credit);
        txtNewCustomer = rootView.findViewById(R.id.txt_view_new_customer);
        txtInStock = rootView.findViewById(R.id.txt_view_in_stock);
        txtOutStock = rootView.findViewById(R.id.txt_view_out_stock);
        tvTotalInner = rootView.findViewById(R.id.tv);
        title_TotalAmount = rootView.findViewById(R.id.title_TotalAmount);
        inventory_valueTxt = rootView.findViewById(R.id.inventory_valueTxt);


        llPaymentcash.setOnClickListener(this);
        llNewcustomer.setOnClickListener(this);
        llInstock.setOnClickListener(this);
        llOutofstock.setOnClickListener(this);
        llPaymentcard.setOnClickListener(this);
        llPaymentcredit.setOnClickListener(this);


        list = new ArrayList<>();
        list.add("Today");
        list.add("Week");
        list.add("Month");
        list.add("Year");

        refreshSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalSales.setText(String.valueOf(totalSale));
            }
        });

        fullReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullReportTable = new Intent(getActivity(), TransactionReportsActivity.class);
                startActivity(fullReportTable);
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.spinner);
        item.setVisible(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter dataAdapter = new ArrayAdapter(context, R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.dropdown);
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_paymentcash:
                Intent intentPaymentCash = new Intent(getActivity(), PaymentModeActivity.class);
                //totalPaymentCash.setText(String.valueOf(totalPayment));
                intentPaymentCash.putExtra("paymentmode", "Cash");
                startActivity(intentPaymentCash);
                break;

            case R.id.ll_paymentcard:
                Intent intentpaymentcard = new Intent(getActivity(), PaymentModeActivity.class);
                //totalPaymentCard.setText(String.valueOf(totalPayment));
                intentpaymentcard.putExtra("paymentmode", "Card");
                startActivity(intentpaymentcard);
                break;

            case R.id.ll_paymentcredit:
                Intent intentpaymentcredit = new Intent(getActivity(), PaymentModeActivity.class);
                //totalPaymentPaytm.setText(String.valueOf(totalPayment));
                intentpaymentcredit.putExtra("paymentmode", "Paytm");
                startActivity(intentpaymentcredit);
                break;

            case R.id.ll_newcustomer:
                Intent intentNewCustomer = new Intent(getActivity(), NewCustomerActivity.class);
                startActivity(intentNewCustomer);
                break;

            case R.id.ll_outofstock:
                Intent intentoutofstock = new Intent(getActivity(), OutOfStockReportsActivity.class);
                startActivity(intentoutofstock);
                break;

            case R.id.ll_instock:
                Intent intentllinstock = new Intent(getActivity(), StockReportsActivity.class);
                startActivity(intentllinstock);
                break;
            case R.id.full_reports:
                Intent fullReportTable = new Intent(getActivity(), TransactionReportsActivity.class);
                startActivity(fullReportTable);
                break;
            default:
        }
    }

    private void paymentModeTotal(String paymentMode1) {
        String check = paymentMode1;
        Cursor cursor = mydatabase.rawQuery("SELECT SUM(totalAmount) as `TotalSales` FROM tbl_transaction where paymentMode='" + check + "'", null);
        jsonArray = cur2Json(cursor);
        Log.e("itemdatathegun", "customer data " + jsonArray);
        setDataAdptr(jsonArray,check);
    }

    private void totalInventoryValue() {
//        forinventory = "inventory";
        String check = "inventory";
        Cursor cursor = mydatabase.rawQuery("SELECT SUM(unitPrice) as `TotalValue` FROM tbl_product where store_ID='" + storeID + "'", null);
        jsonArray = cur2Json(cursor);
        Log.e("itemdatathegun", "customer data " + jsonArray);
        setDataAdptr(jsonArray,check);
    }

    private void seeNewCustomerdata() {
        String check = "customer";
        try {
            Cursor cursor = mydatabase.rawQuery("SELECT count(*) as `NewCust`  from tbl_custuser where createdOn BETWEEN  datetime('now','localtime','-7 day') AND datetime('now','localtime') ", null);
            Log.d("table customerthegun", cursor.toString());
            jsonArray = cur2Json(cursor);
            Log.e("customernodatathegun::", "customer data " + jsonArray);
            setDataAdptr(jsonArray,check);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seeOutStockdata() {
        String check = "outofstock";
        try {
            Cursor cursor = mydatabase.rawQuery("SELECT count(*) as `OutStock`  from tbl_product where totalstock < 5 AND store_ID='"+storeID+"'", null);
            Log.d("table customerthegun", cursor.toString());
            jsonArray = cur2Json(cursor);
            Log.e("customernodatathegun::", "customer data " + jsonArray);
            setDataAdptr(jsonArray,check);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seeInStockdata() {
        String check = "instock";
        try {
            Cursor cursor = mydatabase.rawQuery("SELECT count(*) as `InStock`  from tbl_product where totalstock > 0 AND store_ID='"+storeID+"'", null);
            Log.d("table customerthegun", cursor.toString());
            jsonArray = cur2Json(cursor);
            Log.e("customernodatathegun::", "customer data " + jsonArray);
            setDataAdptr(jsonArray,check);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDataAdptr(JSONArray jsonArray,String check) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);

                if (check.equals("inventory")){
                    totalInventoryValueinDB = jsonobject.getString("TotalValue");
                }
//                LastSalesReportsModel lastSalesReportsModel = new LastSalesReportsModel();
//                lastSalesReportsModel.setSrno(String.valueOf(i));
//                lastSalesReportsModel.setRecieptno(jsonobject.optString("transaction_ID"));//String.valueOf(jsonobject.optInt("product_ID"));
//                lastSalesReportsModel.setAmount(jsonobject.optString("totalAmount"))
                  else if (check.equals("Cash")) {
                    totalPaymentCashView = jsonobject.optDouble("TotalSales");
                } else if (check.equals("Card")) {
                    totalPaymentCardView = jsonobject.optDouble("TotalSales");
                } else if (check.equals("Paytm")) {
                    totalPaymentPaytmView = jsonobject.optDouble("TotalSales");
                } else if (check.equals("customer")){
                    noOfNewCustomer = jsonobject.optString("NewCust");
                } else if (check.equals("instock")){
                    noOfInStock = jsonobject.optString("InStock");
                } else if (check.equals("outofstock")){
                    noOfOutStock = jsonobject.optString("OutStock");
                }
//                lastSalesReportsModelArrayList.add(lastSalesReportsModel);
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

    @Override
    public void onStart() {
        super.onStart();
        try {
            mydatabase = SQLiteDatabase.openDatabase("/data/data/" + context.getPackageName() + "/newinventory.db", null, 0);
            paymentModeTotal("Cash");
            paymentModeTotal("Card");
            paymentModeTotal("Paytm");
            totalInventoryValue();

            if (totalPaymentCashView == null || totalPaymentCashView.equals("NaN") || totalPaymentCashView.isNaN()){
                totalPaymentCashView = 0.0;
            }
            if (totalPaymentPaytmView == null || totalPaymentPaytmView.equals("NaN") || totalPaymentPaytmView.isNaN()){
                totalPaymentPaytmView = 0.0;
            }
            if (totalPaymentCardView == null || totalPaymentCardView.equals("NaN") || totalPaymentCardView.isNaN()){
                totalPaymentCardView = 0.0;
            }

            totalPaymentCash.setText(String.valueOf(totalPaymentCashView));
            totalPaymentPaytm.setText(String.valueOf(totalPaymentPaytmView));
            totalPaymentCard.setText(String.valueOf(totalPaymentCardView));


            try {
                 finalAddedValue = totalPaymentCashView + totalPaymentPaytmView + totalPaymentCardView;
                //yaha add kara k set karna h
                title_TotalAmount.setText(String.valueOf(finalAddedValue));
                totalSale = (totalPaymentCashView + totalPaymentPaytmView + totalPaymentCardView);
            } catch (NumberFormatException e) {
                 e.printStackTrace();
            }
            totalSales.setText(String.valueOf(finalAddedValue));
            seeNewCustomerdata();
            txtNewCustomer.setText(noOfNewCustomer);
            seeInStockdata();
            txtInStock.setText(noOfInStock);
            seeOutStockdata();
            txtOutStock.setText(noOfOutStock);
            inventory_valueTxt.setText(totalInventoryValueinDB);

            if (lastSalesReportsModelArrayList.size() > 0) { //extra values showing in fragment
                lastSalesReportsModelArrayList.clear();
            }
//            seeExpensedataAll();
        }catch (SQLiteCantOpenDatabaseException e){
            Toast.makeText(context, "Can't create any database please first initialize the database", Toast.LENGTH_SHORT).show();
        }


    }

    private void seeExpensedataAll() {
        try {
            mydatabase = SQLiteDatabase.openDatabase("/data/data/" + context.getPackageName() + "/newinventory.db", null, 0);
            mydatabase.beginTransaction();
            //where store_ID='"+storeID+"'LIMIT 0,15
            //SELECT discount,totalAmount,CGST,itemCount,transaction_ID,createdOn,customer_name,paymentMode,custUser_ID from tbl_transaction where isDeleted=0 AND store_ID="+storeID
            cursor = mydatabase.rawQuery("SELECT paymentMode,totalAmount from tbl_transaction where isDeleted=0 AND store_ID="+ storeID+" LIMIT 0,10", null);
            mydatabase.endTransaction();
            jsonArray = cur2Json(cursor);
            setLastTenTransaction(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setLastTenTransaction(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                reportsModel = new ReportsModel();
                reportsModel.setSrno(String.valueOf(i));
                reportsModel.setPaymode(jsonobject.getString("paymentMode"));
                reportsModel.setTotalamount(jsonobject.getString("totalAmount"));
                tReports.add(reportsModel);
                Math.min(tReports.size(),10);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}