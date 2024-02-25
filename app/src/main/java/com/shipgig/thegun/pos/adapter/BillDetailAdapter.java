package com.shipgig.thegun.pos.adapter;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.ReportsModel;
import com.shipgig.thegun.pos.model.UnitBillModal;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Afroz Ahmad on 27/11/19.
 */
public class BillDetailAdapter extends RecyclerView.Adapter<BillDetailAdapter.ViewHolder> {

    ArrayList<UnitBillModal> unitBillModalArrayList;
    private CallBackUs mCallBackus;
    Context context;
    int from;

    JSONArray jsonArray = new JSONArray();
    ReportsModel reportsModel;

    int db_ItemCount,db_TotalAmount,totalstock;

    public BillDetailAdapter(ArrayList<UnitBillModal> unitBillModalArrayList,CallBackUs CallBackus, Context context,int from) {
        this.unitBillModalArrayList = unitBillModalArrayList;
        this.mCallBackus=CallBackus;
        this.context = context;
        this.from = from;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_detail_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.srno.setText(String.valueOf(unitBillModalArrayList.get(position).getSrnumber()));
        holder.productname.setText(unitBillModalArrayList.get(position).getProductName());
        holder.bill_Qty.setText(String.valueOf(unitBillModalArrayList.get(position).getQuantity()));
        holder.bill_UnitPrice.setText(String.valueOf(unitBillModalArrayList.get(position).getUnitprice()));
        holder.bill_discount.setText(String.valueOf(unitBillModalArrayList.get(position).getDiscount()));

        String pName = unitBillModalArrayList.get(position).getProductName();
        String transaction_id = unitBillModalArrayList.get(position).getTransactionID();
        String productID = unitBillModalArrayList.get(position).getProductID();
        int sellQty = unitBillModalArrayList.get(position).getQuantity();
        int singlePrice = unitBillModalArrayList.get(position).getUnitprice();

        if (from != 2){

            holder.bill_return.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getTransactionDetail(transaction_id);
                    holder.dialog.setContentView(R.layout.edit_item_to_return);
                    EditText inputQTY = holder.dialog.findViewById(R.id.inputQty);
                    TextView prevQTY = holder.dialog.findViewById(R.id.prev_Qty);
                    Button submit = holder.dialog.findViewById(R.id.submit);
                    prevQTY.setText(String.valueOf(sellQty));

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int finalItemCount,finalTotalAmount,invoiceNewQty,invoiceNewPrice;
                            String inputNumber = inputQTY.getText().toString();
                            if (Integer.parseInt(inputNumber) > sellQty){
                                inputQTY.setError("Enter valid return item");
                            }
                            else {
                                try{
                                    getProductQuantityFromTble(productID);
                                    invoiceNewQty = sellQty - Integer.parseInt(inputNumber);
                                    invoiceNewPrice = db_TotalAmount - (Integer.parseInt(inputNumber) * singlePrice);
                                    updateInvoiceTbl(invoiceNewQty,productID);
                                    mCallBackus.updateInvoiceAfterEdit(transaction_id);
                                    finalItemCount = db_ItemCount - Integer.parseInt(inputNumber);
                                    finalTotalAmount = invoiceNewPrice;
                                    updateTransactiontbl(finalItemCount,finalTotalAmount,transaction_id);
                                    totalstock = totalstock + Integer.parseInt(inputNumber);
                                    updateProductTable(productID,totalstock);
                                }catch (NumberFormatException e){
                                    e.printStackTrace();
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                }
                                holder.dialog.dismiss();

                            }

                        }
                    });


                    ImageView imageView = holder.dialog.findViewById(R.id.imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.dialog.dismiss();

                        }
                    });
                    holder.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    holder.dialog.setCancelable(false);
                    holder.dialog.show();
                }
            });

        }

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

    private void getTransactionDetail(String transaction_id) {

        try {
            String tranaction = "\""+transaction_id+"\"";

            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + context.getPackageName() +
                    "/newinventory.db", null, 0);
            mydatabase.beginTransaction();
            Cursor cursor = mydatabase.rawQuery("SELECT itemCount,totalAmount from tbl_transaction where transaction_ID="+tranaction, null);
            mydatabase.endTransaction();
            jsonArray = cur2Json(cursor);
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

    public void setDataAdptr(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                reportsModel = new ReportsModel();
                db_ItemCount = jsonobject.getInt("itemCount");
                db_TotalAmount = jsonobject.getInt("totalAmount");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
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

    private boolean updateTransactiontbl(int count, int amount,String transaction_id) {
        String tranaction = "\""+transaction_id+"\"";
        SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + context.getPackageName() + "/newinventory.db", null, 0);
        ContentValues contentValues = new ContentValues();
        contentValues.put("itemCount", count);
        contentValues.put("totalAmount", amount);
        long i = mydatabase.update("tbl_transaction", contentValues, " transaction_ID="+tranaction, null);
        Log.d("editThegun", String.valueOf(i));

        if (i == -1)
            return false;
        else{
            return true;
        }
    }

    private boolean updateInvoiceTbl(int updateQty,String productid) {
        try {
            String pdID = "\""+productid+"\"";
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + context.getPackageName() + "/newinventory.db", null, 0);
            ContentValues contentValues = new ContentValues();
            contentValues.put("quantity", updateQty);
            long i = mydatabase.update("tbl_invoice", contentValues, "product_ID"+"="+pdID, null);

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

    @Override
    public int getItemCount() {
        return unitBillModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView srno,productname,bill_Qty,bill_UnitPrice,bill_discount,bill_return;
        Dialog dialog = new Dialog(context);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            srno =  itemView.findViewById(R.id.sr_number);
            productname =  itemView.findViewById(R.id.productname);
            bill_Qty =  itemView.findViewById(R.id.bill_Qty);
            bill_UnitPrice =  itemView.findViewById(R.id.bill_UnitPrice);
            bill_discount =  itemView.findViewById(R.id.bill_discount);
            bill_return =  itemView.findViewById(R.id.bill_return);
        }
    }

    public interface CallBackUs {
        void updateInvoiceAfterEdit(String string);

    }
}
