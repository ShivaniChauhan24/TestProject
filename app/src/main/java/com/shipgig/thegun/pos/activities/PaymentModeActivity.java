package com.shipgig.thegun.pos.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.PaymentModeModel;
import com.shipgig.thegun.pos.utilities.Sharepreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PaymentModeActivity extends Activity implements View.OnClickListener {

    public  int totalPayment;
    Intent intent;
    String paymentMode;
    Context context;
    RecyclerView recyclerView;
    PaymentModeAdapter paymentModeAdapter;
    ImageView backarrow;
    ArrayList<PaymentModeModel> paymentModeModels= new ArrayList<>();
    private SQLiteDatabase mydatabase;
    Cursor cursor;
    JSONArray jsonArray;
    PaymentModeModel paymentModeModel;
    int storeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.paymentmode_activity);
        context = this;

        storeID = Sharepreference.getSharedPreferenceInt(this,"storeID",0);
        intent = getIntent();
        if(intent!=null){
            paymentMode = intent.getStringExtra("paymentmode");
        }
        init();
        seePaymentModedata(paymentMode);
        recycler();


    }

    private void init() {
        backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(this);
        recyclerView = findViewById(R.id.recylerview);

        mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/newinventory.db", null, 0);

    }

    public void recycler() {
        paymentModeAdapter = new PaymentModeAdapter(paymentModeModels, context);
        paymentModeAdapter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(paymentModeAdapter);
    }

    private void seePaymentModedata(String paymentMode) {
        try {
            //totalAmount,transaction_ID,itemCount,totalQty,createdOn,paymentMode
            cursor = mydatabase.rawQuery( "SELECT totalAmount,transaction_ID,itemCount,totalQty,createdOn,paymentMode FROM tbl_transaction where paymentMode='"+paymentMode+"'"+"AND store_ID="+storeID,null);
            jsonArray = cur2Json(cursor);
            if (jsonArray.length() != 0) {
                setDataAdptr(jsonArray);
            }
            else {
                Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show();
            }
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

                paymentModeModel = new PaymentModeModel();
                paymentModeModel.setSrno(String.valueOf(i));
                paymentModeModel.setAmount(String.valueOf(jsonobject.optInt("totalAmount")));
                paymentModeModel.setTrans(jsonobject.optString("transaction_ID"));
                paymentModeModel.setQuantity(jsonobject.optString("itemCount"));
                paymentModeModel.setPayMode(jsonobject.optString("paymentMode"));
                paymentModeModel.setDate(jsonobject.optString("createdOn"));
                totalPayment = (totalPayment+(jsonobject.optInt("totalAmount")));
                paymentModeModels.add(paymentModeModel);

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
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.backarrow:
                finish();
                break;
                default:
        }
    }

    public class PaymentModeAdapter extends RecyclerView.Adapter<PaymentModeAdapter.ViewHolder> {
        ArrayList<PaymentModeModel>reports;
        Context context;

        public PaymentModeAdapter(ArrayList<PaymentModeModel> tReports, Context context) {
            this.context = context;
            this.reports = tReports;
        }



        @Override
        public PaymentModeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.paymentmode_details, parent, false);
            return new PaymentModeAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(PaymentModeAdapter.ViewHolder holder, int position) {
            holder.txtSrno.setText(reports.get(position).getSrno());
            holder.txtMode.setText(reports.get(position).getPayMode());
            holder.txtAmount.setText(reports.get(position).getAmount());
            holder.txtQuantity.setText(reports.get(position).getQuantity());
            holder.txtDate.setText(reports.get(position).getDate());
            holder.txtTrans.setText(""+reports.get(position).getTrans());

            if(position%2==1){
                holder.ll_background.setBackgroundColor(Color.parseColor("#eeeeee"));
            }

            else{
                holder.ll_background.setBackgroundColor(Color.parseColor("#fafafa"));
            }
        }

        @Override
        public int getItemCount() {
            return reports.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView txtSrno,txtAmount,txtQuantity,txtDate,txtMode,txtTrans;
            LinearLayout ll_background;


            public ViewHolder(View itemView) {
                super(itemView);
                ll_background =  itemView.findViewById(R.id.ll_background);
                txtSrno = itemView.findViewById(R.id.srno);
                txtAmount = itemView.findViewById(R.id.amount);
                txtQuantity = itemView.findViewById(R.id.quantity);
                txtDate = itemView.findViewById(R.id.date);
                txtMode = itemView.findViewById(R.id.paymode);
                txtTrans = itemView.findViewById(R.id.transaction);
            }
        }
    }

}
