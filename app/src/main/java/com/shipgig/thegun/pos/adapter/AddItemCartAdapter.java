package com.shipgig.thegun.pos.adapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.fxn769.Numpad;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.fragment.CartFragment;
import com.shipgig.thegun.pos.fragment.NewHomeFragment;
import com.shipgig.thegun.pos.model.AddItemCartModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.shipgig.thegun.pos.fragment.CartFragment.addItemCartArray;
import static com.shipgig.thegun.pos.fragment.NewHomeFragment.discountMode;
import static com.shipgig.thegun.pos.fragment.NewHomeFragment.globalTakendiscountofPercent;
import static com.shipgig.thegun.pos.fragment.NewHomeFragment.globalTakendiscountofRupees;
import static com.shipgig.thegun.pos.fragment.NewHomeFragment.removeDisCall;
import static com.shipgig.thegun.pos.fragment.NewHomeFragment.totalDiscount;


public class AddItemCartAdapter extends RecyclerView.Adapter<AddItemCartAdapter.ViewHolder> {
    public static ArrayList<AddItemCartModel> reports;// = new ArrayList<>();
    Context context;
    private int from;
    private StringBuffer sb;
//    private JSONArray jsonArray = new JSONArray();
//    public String gstPercent;
    private Double finalGST;
    JSONArray jsonArray = new JSONArray();
    int totalstock;

    public AddItemCartAdapter(ArrayList<AddItemCartModel> tReports, Context context, int from) {
        this.context = context;
        this.reports = tReports;
        this.from= from;
    }

    @Override
    public AddItemCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (from==1){
             v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_details_layout, parent, false);
        }
        else {
             v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.summary_item, parent, false);
        }

        return new AddItemCartAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AddItemCartAdapter.ViewHolder holder, int position) {

        if (from == 1){
            try {
                holder.srNo.setText(reports.get(position).getSrNo());
                holder.itemName.setText(reports.get(position).getProductName());
                holder.itemPrice.setText(reports.get(position).getProductPrice());
                holder.quantity.setText(reports.get(position).getQuantity());

            }catch (NullPointerException e) {
                e.printStackTrace();
            }
             catch (NumberFormatException e1) {
                e1.printStackTrace();
            }

            double sumQuantity = 0;
            double sumSubTotal=0;
            for(int i = 0; i < reports.size(); i++) {
                try {
                    sumQuantity += Integer.parseInt(reports.get(i).getQuantity());
                    sumSubTotal+= (Double.valueOf(reports.get(i).getProductPrice())* Integer.parseInt(reports.get(i).getQuantity()));
                    getGST();
                }
                catch (NullPointerException e) {
                      e.printStackTrace();
                }
                catch(NumberFormatException e1) {
                    e1.printStackTrace();
                }
            }
            NewHomeFragment.totalAmountInPayButton.setText(String.valueOf(sumSubTotal));
            try {
                if(!discountMode.equals("") && discountMode != null){
                    if (discountMode.equals("percent")){
                        Double purDiscount = (Double.valueOf(globalTakendiscountofPercent)* sumSubTotal)/100;
                        Double afterdiscount = sumSubTotal - purDiscount;
                        NewHomeFragment.totalAmountInPayButton.setText(String.valueOf(afterdiscount));
                        totalDiscount.setText(String.valueOf(purDiscount));
                    }else if (discountMode.equals("rupees")){
                        Double purDiscount = sumSubTotal - Double.valueOf(globalTakendiscountofRupees);
                        NewHomeFragment.totalAmountInPayButton.setText(String.valueOf(purDiscount));
                        totalDiscount.setText(String.valueOf(globalTakendiscountofRupees));
                    }
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            NewHomeFragment.itemInCart.setText(String.valueOf(sumQuantity));
            NewHomeFragment.subTotal.setText(String.valueOf(sumSubTotal));
            NewHomeFragment.cgstAmount.setText(String.valueOf(roundDecimalNumber(finalGST)));

            holder.removeSingleItemFromCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (reports.size()==1) {
                            CartFragment.addItemCartArray.clear();
                        } else {
                            addItemCartArray.remove(holder.getAdapterPosition());
                            NewHomeFragment.totalDiscount.setText("0.0");
                        }

                        reports.remove(holder.getAdapterPosition());

                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());
                        if (reports.size()<1) {
                            CartFragment.addItemCartArray.clear();
                        }

                        try {

                            double sumQuantity = 0;
                            double sumSubTotal = 0;
                            for (int i = 0; i < reports.size(); i++) {
                                sumQuantity += Integer.parseInt(reports.get(i).getQuantity());
                                sumSubTotal += (Integer.parseInt(reports.get(i).getProductPrice()) * Integer.parseInt(reports.get(i).getQuantity()));
                                getGST();
                            }

                            NewHomeFragment.totalAmountInPayButton.setText(String.valueOf(sumSubTotal));
                            try {
                                if(!discountMode.equals("") && discountMode != null){
                                    if (discountMode.equals("percent")){
                                        Double purDiscount = (Double.valueOf(globalTakendiscountofPercent)* sumSubTotal)/100;
                                        Double afterdiscount = sumSubTotal - purDiscount;
                                        NewHomeFragment.totalAmountInPayButton.setText(String.valueOf(afterdiscount));
                                        totalDiscount.setText(String.valueOf(purDiscount));
                                    }else if (discountMode.equals("rupees")){
                                        if (reports.size() != 0){
                                            Double purDiscount = sumSubTotal - Double.valueOf(globalTakendiscountofRupees);
                                            NewHomeFragment.totalAmountInPayButton.setText(String.valueOf(purDiscount));
                                            totalDiscount.setText(String.valueOf(globalTakendiscountofRupees));
                                        }
                                    }
                                }
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }
                            NewHomeFragment.itemInCart.setText(String.valueOf(sumQuantity));
                            NewHomeFragment.subTotal.setText(String.valueOf(sumSubTotal));
//                            NewHomeFragment.totalAmountInPayButton.setText(String.valueOf(sumSubTotal));
                            NewHomeFragment.cgstAmount.setText(String.valueOf(roundDecimalNumber(finalGST)));
                            try {
                                if (reports.size() == 0){
                                    NewHomeFragment.cgstAmount.setText("0.0");
                                    NewHomeFragment.totalDiscount.setText("0.0");
                                    removeDisCall();
                                }
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                        }

                    }catch (ArrayIndexOutOfBoundsException e){
                        Toast.makeText(context, "Please delete item one by one", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }



                }


            });

            holder.editQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog12 = new Dialog(context);

                    dialog12.setContentView(R.layout.dialog_custom_keyboard);
                    dialog12.setTitle("Title...");
                    TextView edtTextQuantity= dialog12.findViewById(R.id.edt_txt_quantity);
//                    int maxQty = Integer.parseInt(reports.get(position).getTotalQty());
                    String productID = reports.get(position).getProduct_Id();
                    getProductQuantityFromTble(productID);
                    TextView availableQty = dialog12.findViewById(R.id.availableQty);
                    availableQty.setVisibility(View.VISIBLE);
                    availableQty.setText("Available Qty :- " + totalstock);

                    Numpad numpad = dialog12.findViewById(R.id.num);
                    numpad.setTextLengthLimit(15);
                    numpad.setBackgroundRes(R.drawable.custome_keyboard_grid_border);
                    numpad.setOnTextChangeListner((String text, int digits_remaining) -> {
                        removeZero(text);
                        edtTextQuantity.setText((sb));
                    });
                    ImageView dialogQuantityClose= dialog12.findViewById(R.id.quantity_dialog_close);
                    dialogQuantityClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog12.dismiss();
                        }
                    });

                    TextView dialogUpdateButton= dialog12.findViewById(R.id.dialog_update_quantity);
                    dialogUpdateButton.setVisibility(View.VISIBLE);
                    dialogUpdateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (edtTextQuantity.getText().toString().equals("") || Integer.parseInt(edtTextQuantity.getText().toString()) < 1) {
                                Toast.makeText(context, "Add some quantity", Toast.LENGTH_SHORT).show();
                            } else {
                                try{

                                    if (Integer.parseInt(edtTextQuantity.getText().toString()) <= totalstock){
                                        reports.get(position).setQuantity(edtTextQuantity.getText().toString());
                                        AddItemCartModel addItemCartModel = new AddItemCartModel();
                                        addItemCartModel.setProductName(reports.get(position).getProductName());
                                        addItemCartModel.setDiscount(reports.get(position).getDiscount());
                                        addItemCartModel.setProduct_Id(reports.get(position).getProduct_Id());
                                        addItemCartModel.setSrNo(reports.get(position).getSrNo());
                                        addItemCartModel.setProductPrice(reports.get(position).getProductPrice());
                                        addItemCartModel.setHsn_Number(reports.get(position).getHsn_Number());
                                        addItemCartModel.setGst_Percent(reports.get(position).getGst_Percent());
                                        addItemCartModel.setQuantity(reports.get(position).getQuantity());
                                        AddItemCartAdapter.reports.set(position, addItemCartModel);
                                        holder.quantity.setText(reports.get(position).getQuantity());

                                        double sumQuantity = 0;
                                        double sumSubTotal = 0;
                                        for (int i = 0; i < reports.size(); i++) {
                                            sumQuantity += Integer.parseInt(reports.get(i).getQuantity());
                                            sumSubTotal += (Integer.parseInt(reports.get(i).getProductPrice()) *
                                                    Integer.parseInt(reports.get(i).getQuantity()));
                                            getGST();
                                        }
                                        NewHomeFragment.totalAmountInPayButton.setText(String.valueOf(sumSubTotal));
                                        try {
                                            if(!discountMode.equals("") && discountMode != null){
                                                if (discountMode.equals("percent")){
                                                    Double purDiscount = (Double.valueOf(globalTakendiscountofPercent)* sumSubTotal)/100;
                                                    Double afterdiscount = sumSubTotal - purDiscount;
                                                    NewHomeFragment.totalAmountInPayButton.setText(String.valueOf(afterdiscount));
                                                    totalDiscount.setText(String.valueOf(purDiscount));
                                                }else if (discountMode.equals("rupees")){
                                                    Double purDiscount = sumSubTotal - Double.valueOf(globalTakendiscountofRupees);
                                                    NewHomeFragment.totalAmountInPayButton.setText(String.valueOf(purDiscount));
                                                    totalDiscount.setText(String.valueOf(globalTakendiscountofRupees));
                                                }
                                            }
                                        }catch (NullPointerException e){
                                            e.printStackTrace();
                                        }
                                        NewHomeFragment.itemInCart.setText(String.valueOf(sumQuantity));
                                        NewHomeFragment.subTotal.setText(String.valueOf(sumSubTotal));
//                                        NewHomeFragment.totalAmountInPayButton.setText(String.valueOf(sumSubTotal));
                                        NewHomeFragment.cgstAmount.setText(String.valueOf(roundDecimalNumber(finalGST)));
                                        dialog12.dismiss();
                                    }else {

                                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                                        dialog.setTitle(Html.fromHtml("<font color='#FF7F27'> Alert..!!! </font>"));
                                        dialog.setCancelable(false);
                                        dialog.setMessage("Sorry, you have exceed limit. Your stock is.. "+totalstock +"  Thank you");
                                        dialog.setPositiveButton(
                                                "Ok",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alertDialog = dialog.create();
                                        alertDialog.show();

                                    }


                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                        }
                    });


                    dialog12.show();

                }
            });

        }

        else {

            try {
                holder.productname.setText(reports.get(position).getProductName());
                holder.productquantity.setText("x "+reports.get(position).getQuantity());
                holder.productprice.setText(reports.get(position).getProductPrice());
                holder.subtotal.setText(String.valueOf(String.valueOf(Double.valueOf(reports.get(position).getProductPrice())* Integer.parseInt(reports.get(position).getQuantity()))));
            }catch (NullPointerException e){
                e.printStackTrace();
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        Log.d("sizeproductr", String.valueOf(reports.size()));

        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName,itemPrice, quantity,srNo;
        ImageView removeSingleItemFromCart,editQuantity;
        TextView productname,productprice,productquantity,subtotal;


        public ViewHolder(View itemView) {
            super(itemView);
            itemName =  itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            quantity = itemView.findViewById(R.id.multi);
            srNo = itemView.findViewById(R.id.srno);
            editQuantity = itemView.findViewById(R.id.edit_quantity_image);
            removeSingleItemFromCart =  itemView.findViewById(R.id.remove_single_item_cart);

            productname =  itemView.findViewById(R.id.productname);
            productprice =  itemView.findViewById(R.id.productprice);
            productquantity =  itemView.findViewById(R.id.productquantity);
            subtotal =  itemView.findViewById(R.id.productsubtotal);

        }
    }

    private void removeZero(String str) {
        int i = 0;
        while (i < str.length() && str.charAt(i) == '0')
            i++;

        sb = new StringBuffer(str);

        sb.replace(0, i, "");

        String s = sb.toString();

    }

    private void getGST() {

        try {
            double totalSumGstAmount = 0;
            for (int i = 0; i < reports.size(); i++){
                Double product_amount = (double) (Double.parseDouble(reports.get(i).getProductPrice())
                        * Integer.parseInt(reports.get(i).getQuantity()));
                Double gstAmount = ((reports.get(i).getGst_Percent()) * product_amount)/100;
                totalSumGstAmount = totalSumGstAmount +gstAmount;
            }
            finalGST =  totalSumGstAmount;
        }catch (NumberFormatException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    private double roundDecimalNumber(Double d){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.valueOf(decimalFormat.format(d));
    }

    //this method for get all quantity from db to show in available quantity.

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

}


