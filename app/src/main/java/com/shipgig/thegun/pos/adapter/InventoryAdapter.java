package com.shipgig.thegun.pos.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shipgig.thegun.pos.MainActivity;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.activities.EditInventoryActivity;
import com.shipgig.thegun.pos.model.ManageInventoryModel;
import java.util.ArrayList;

/**
 * Created by Afroz Ahmad on 25/11/19.
 */
public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

    public static RecyclerViewClickListeners itemListner;
    ManageInventoryModel manageInventoryModel;

    public static ArrayList<ManageInventoryModel> inventoryDetails;
    Context context;
    public String itemName;
    private CallBackUs mCallBacks;


    public InventoryAdapter(ArrayList<ManageInventoryModel> inventoryDetails, Context context, CallBackUs callBackUs){

        this.context = context;
        this.mCallBacks = callBackUs;
        this.inventoryDetails = inventoryDetails;
//        this.itemListner = itemListner;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manage_inventory_details, parent, false);
        return new InventoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        showData((ViewHolder) holder, position);

    }

    @Override
    public int getItemCount() {
        return inventoryDetails.size();
    }

    private void deleteInventoryData(String productid) {

        try{
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + context.getPackageName() + "/newinventory.db", null, 0);
            mydatabase.beginTransaction();
            int result=mydatabase.delete("tbl_product","product_ID"+" =?",new String[]{String.valueOf(productid)});
            mydatabase.setTransactionSuccessful();
            mydatabase.endTransaction();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewSrno, textViewProductName,textViewCustomerID,textViewQuantity,textViewDiscount,textViewUnitPrice;
        LinearLayout ll_background;
        ImageView itemEdit, itemDelete;
        Context contexts;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewSrno = itemView.findViewById(R.id.srno);
            textViewProductName = itemView.findViewById(R.id.productname);
            textViewQuantity = itemView.findViewById(R.id.quantity);
            textViewDiscount = itemView.findViewById(R.id.discount);
            textViewUnitPrice = itemView.findViewById(R.id.unitprice);
            ll_background =  itemView.findViewById(R.id.ll_background);
            itemEdit =  itemView.findViewById(R.id.itemEdit);
            itemDelete =  itemView.findViewById(R.id.delete);
            contexts = itemView.getContext();

        }

        @Override
        public void onClick(View v) {

            itemListner.recyclerViewListClicked(v,inventoryDetails,this.getLayoutPosition());
        }
    }

    private void showData(ViewHolder holder, int position) {

        manageInventoryModel = new ManageInventoryModel();

        holder.textViewSrno.setText(inventoryDetails.get(position).getSrno());
        holder.textViewProductName.setText(inventoryDetails.get(position).getProdict());
        holder.textViewQuantity.setText(inventoryDetails.get(position).getQuantity());
        holder.textViewDiscount.setText("₹"+inventoryDetails.get(position).getDiscount());
        holder.textViewUnitPrice.setText("₹"+inventoryDetails.get(position).getUnitPrice());

        String productname = inventoryDetails.get(position).getProdict();
        String productID = inventoryDetails.get(position).getProductId();

        holder.itemEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.contexts, EditInventoryActivity.class);
                intent.putExtra("forEdit","edit");
                intent.putExtra("fromAdapter","adapter");
                intent.putExtra("intentproductname",productname);
                intent.putExtra("intentproductId",productID);
                context.startActivity(intent);

            }
        });

        holder.itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle(Html.fromHtml("<font color='#FF7F27'>Are you sure to delete?</font>"));
                alertDialog.setCancelable(false);
                alertDialog.setNegativeButton(Html.fromHtml("<font color='#FF7F27'>Cancel</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                    }
                });
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteInventoryData(inventoryDetails.get(position).getProductId());
                        inventoryDetails.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, inventoryDetails.size());
                        mCallBacks.updateInventoryAfterDelete();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("openInventory","inventory");
                        context.startActivity(intent);
                    }
                });
                alertDialog.create();
                alertDialog.show();
            }
        });

    }

    public void filterList(ArrayList<ManageInventoryModel> filteredList) {
        inventoryDetails = filteredList;
        notifyDataSetChanged();
    }

    public interface RecyclerViewClickListeners {
        public void recyclerViewListClicked(View v,Object items, int position);
    }

    public interface CallBackUs {
        void updateInventoryAfterDelete();
    }
}
