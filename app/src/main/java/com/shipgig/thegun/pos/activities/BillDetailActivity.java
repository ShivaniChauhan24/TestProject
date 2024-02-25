package com.shipgig.thegun.pos.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.shipgig.thegun.pos.MainActivity;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.adapter.BillDetailAdapter;
import com.shipgig.thegun.pos.model.UnitBillModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static com.shipgig.thegun.pos.fragment.NewHomeFragment.itemInCart;


public class BillDetailActivity extends AppCompatActivity implements BillDetailAdapter.CallBackUs{


    ImageView backarrow;
    String transaction_ID,taxes,creatOn,toCheck;
    String totalAmount,discount,name,custID,quantity,payMode;
    TextView transactionIDTxt,customer_nameTxt,customer_idTxt,number_of_itemTxt,
            paymentModeTxt,dicountTxt,totalAmountTxt,bill_cancel;
    Context context;
    private JSONArray jsonArray = new JSONArray();
    UnitBillModal unitBillModal;
    ArrayList<UnitBillModal> myArrayList = new ArrayList<>();
    RecyclerView allItemRecyclerview;
    BillDetailAdapter billDetailAdapter;
    public boolean isClickable = true;

    int totalstock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bill_detail);

        register();

        transaction_ID = getIntent().getStringExtra("transaction");
        totalAmount = getIntent().getStringExtra("totalrs");
        discount = getIntent().getStringExtra("discount");
        name = getIntent().getStringExtra("name");
        custID = getIntent().getStringExtra("cust_id");
        quantity = getIntent().getStringExtra("qty");
        payMode = getIntent().getStringExtra("paymode");
        taxes = getIntent().getStringExtra("taxes");
        toCheck = getIntent().getStringExtra("from_adapter");

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        creatOn = currentDate + " " + currentTime;

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setTextByIntent();
        getItemByUnit(transaction_ID);

        if (toCheck.equals("void")){
            bill_cancel.setVisibility(View.GONE);
        }

        bill_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BillDetailActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#FF7F27'>Confirmation Alert..!!!</font>"));
                builder.setMessage(Html.fromHtml("<font color='#FF7F27'>Are you sure you want to cancel this invoice?</font>"));
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        aboutBillCancel();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

    }

    private void aboutBillCancel() {

        ProgressDialog dialog = new ProgressDialog(BillDetailActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Bill Cancelling..");
        dialog.setMessage("Please wait, your bill cancellation under processing");
        dialog.show();

        updateInToTransactionTable(transaction_ID);
        //this method for get total number of product in database
        for (int i = 0; i < myArrayList.size(); i++){
            String productID = myArrayList.get(i).getProductID();
            int invoiceQty = myArrayList.get(i).getQuantity();
            getProductQuantityFromTble(productID);
            int updateToProductQty = totalstock + invoiceQty;
            updateProductTable(productID,updateToProductQty);
        }
        dialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(BillDetailActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Confirmation Alert..!!!");
        builder.setMessage("Your bill cancelled successfully, Now check in void section");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
             Intent intent = new Intent(BillDetailActivity.this, MainActivity.class);
             intent.putExtra("openInventory","allreport");
             startActivity(intent);
            }
        });
        builder.show();

    }

    private void viewInAdapter() {

        if (toCheck.equals("void")){
            billDetailAdapter = new BillDetailAdapter(myArrayList,this::getItemByUnit,context,2);
            LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            allItemRecyclerview.setLayoutManager(verticalLayoutManager);
            allItemRecyclerview.setAdapter(billDetailAdapter);
            billDetailAdapter.notifyDataSetChanged();

        }else {
            billDetailAdapter = new BillDetailAdapter(myArrayList,this::getItemByUnit,context,1);
            LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            allItemRecyclerview.setLayoutManager(verticalLayoutManager);
            allItemRecyclerview.setAdapter(billDetailAdapter);
            billDetailAdapter.notifyDataSetChanged();
        }

    }

    private void register() {

        context = this;
        transactionIDTxt = findViewById(R.id.transactionIDTxt);
        customer_nameTxt = findViewById(R.id.customer_nameTxt);
        customer_idTxt = findViewById(R.id.customer_idTxt);
        number_of_itemTxt = findViewById(R.id.number_of_itemTxt);
        paymentModeTxt = findViewById(R.id.paymentModeTxt);
        dicountTxt = findViewById(R.id.dicountTxt);
        totalAmountTxt = findViewById(R.id.totalAmountTxt);
        allItemRecyclerview = findViewById(R.id.allItemRecyclerview);
        backarrow = findViewById(R.id.backarrow);
        bill_cancel = findViewById(R.id.bill_cancel);


    }

    private void setTextByIntent() {

        transactionIDTxt.setText(transaction_ID);
        customer_nameTxt.setText(name);
        customer_idTxt.setText(custID);
        number_of_itemTxt.setText(quantity);
        paymentModeTxt.setText(payMode);
        dicountTxt.setText(discount);
        totalAmountTxt.setText(totalAmount);
    }

    private void getItemByUnit(String transaction_id) {

        try {
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/newinventory.db", null, 0);
            mydatabase.beginTransaction();
            String transactionid = "\'"+transaction_id+"\'";
            Cursor cursor = mydatabase.rawQuery("SELECT product_ID,quantity,unitPrice,discount,transaction_ID,product_name from tbl_invoice where transaction_ID="+transactionid, null);
            mydatabase.endTransaction();
            jsonArray = cur2Json(cursor);
            Log.e("customer data::", "customer data " + jsonArray);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
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
                        Log.e("parveen112", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;

    }

    public void setDataAdptr(JSONArray jsonArray) {
        myArrayList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                unitBillModal = new UnitBillModal();
                unitBillModal.setSrnumber(i);
                unitBillModal.setProductName(jsonobject.getString("product_name"));
                unitBillModal.setDiscount(jsonobject.getInt("discount"));
                unitBillModal.setQuantity(jsonobject.getInt("quantity"));
                unitBillModal.setUnitprice(jsonobject.getInt("unitPrice"));
                unitBillModal.setTransactionID(jsonobject.getString("transaction_ID"));
                unitBillModal.setProductID(jsonobject.getString("product_ID"));
                myArrayList.add(unitBillModal);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        viewInAdapter();

    }

    private boolean updateInToTransactionTable(String transactionID) {

        try {
            String transact = "\"" + transactionID + "\"";
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + context.getPackageName() + "/newinventory.db", null, 0);
            ContentValues contentValues = new ContentValues();
            contentValues.put("isDeleted", 1);
            long i = mydatabase.update("tbl_transaction", contentValues, "transaction_ID=" + transact, null);
            Log.d("editThegun", String.valueOf(i));
            if (i == -1)
                return false;
            else {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private void getProductQuantityFromTble(String productID) {

        try {
            String tranaction = "\""+productID+"\"";

            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + context.getPackageName() +
                    "/newinventory.db", null, 0);
            mydatabase.beginTransaction();
            Cursor cursor = mydatabase.rawQuery("SELECT totalstock from tbl_product where product_ID="+tranaction, null);
            mydatabase.endTransaction();
            jsonArray = cur2Json(cursor);
            setData(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean updateProductTable(String productID, int updateQty) {

        try {
            String pdID = "\""+productID+"\"";
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + context.getPackageName() + "/newinventory.db", null, 0);
            ContentValues contentValues = new ContentValues();
            contentValues.put("totalstock", updateQty);
            long i = mydatabase.update("tbl_product", contentValues, "product_ID"+"="+pdID, null);

            if (i == -1)
                return false;
            else{
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public void setData(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                totalstock = jsonobject.getInt("totalstock");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (toCheck.equals("void")){
            Intent intent = new Intent(BillDetailActivity.this,VoidBillReportsActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(BillDetailActivity.this,TransactionReportsActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void updateInvoiceAfterEdit(String string) {
        myArrayList.clear();
        getItemByUnit(transaction_ID);
    }
}
