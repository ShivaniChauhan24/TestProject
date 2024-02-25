package com.shipgig.thegun.pos.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.activities.EditInventoryActivity;
import com.shipgig.thegun.pos.inventoryAPI.Result;
import com.shipgig.thegun.pos.model.ManageInventoryModel;

import java.util.ArrayList;


public class ManageInventoryDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;


    private ArrayList<ManageInventoryModel> manageInventoriesDetails;
    Context context;
    private CallBackUs mCallBackus;
    Bitmap bitmap;


    public ManageInventoryDetailsAdapter(ArrayList<ManageInventoryModel> manageInventories, Context context,CallBackUs CallBackus) {
        Log.d("adaptersize", String.valueOf(manageInventories.size()));
        this.context = context;
        this.manageInventoriesDetails = manageInventories;
        this.mCallBackus=CallBackus;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM){
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_inventory_details, parent, false);
            return new ItemViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder){

            populateItemRows((ItemViewHolder) viewHolder, position);
        }else if (viewHolder instanceof LoadingViewHolder){
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return manageInventoriesDetails.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSrno, textViewProductName,textViewCustomerID,textViewQuantity,textViewDiscount,textViewUnitPrice;
        LinearLayout ll_background;
        ImageView itemEdit, itemDelete;
        Context contexts;

        ItemViewHolder(View itemView) {
            super(itemView);
            textViewSrno = (TextView)itemView.findViewById(R.id.srno);
            textViewProductName = (TextView)itemView.findViewById(R.id.productname);
            textViewQuantity = (TextView)itemView.findViewById(R.id.quantity);
            textViewDiscount = (TextView)itemView.findViewById(R.id.discount);
            textViewUnitPrice = (TextView)itemView.findViewById(R.id.unitprice);
            ll_background = (LinearLayout) itemView.findViewById(R.id.ll_background);
            itemEdit =  itemView.findViewById(R.id.itemEdit);
            itemDelete =  itemView.findViewById(R.id.delete);
            contexts = itemView.getContext();
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {


        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        String productname = manageInventoriesDetails.get(position).getProductName();
        String productID = manageInventoriesDetails.get(position).getProductId();

        viewHolder.textViewSrno.setText(manageInventoriesDetails.get(position).getProductId());
        viewHolder.textViewProductName.setText(manageInventoriesDetails.get(position).getProductName());
        viewHolder.textViewQuantity.setText(manageInventoriesDetails.get(position).getTotalStock());
        viewHolder.textViewDiscount.setText(manageInventoriesDetails.get(position).getDiscount());
        viewHolder.textViewUnitPrice.setText(manageInventoriesDetails.get(position).getUnitPrice());

//        String productname = manageInventoriesDetails.get(position).getProductName();
//        String productID = manageInventoriesDetails.get(position).getProductID();

        viewHolder.itemEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialogBox.ManageCustomer("");
                Intent intent = new Intent(viewHolder.contexts, EditInventoryActivity.class);
                intent.putExtra("forEdit","edit");
                intent.putExtra("fromAdapter","adapter");
                intent.putExtra("intentproductname",productname);
                intent.putExtra("intentproductId",productID);
                viewHolder.contexts.startActivity(intent);
            }
        });

        viewHolder.itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(viewHolder.contexts);
                alertDialog.setTitle(Html.fromHtml("<font color='#FF7F27'>Are you sure you want to delete?</font>"));
                alertDialog.setCancelable(false);
                alertDialog.setNegativeButton(Html.fromHtml("<font color='#FF7F27'>Cancel</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteInventoryData(manageInventoriesDetails.get(position).getProductName());
                        manageInventoriesDetails.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, manageInventoriesDetails.size());
                        mCallBackus.updateInventoryAfterDelete();


                    }
                });
                alertDialog.create();

                alertDialog.show();

            }
        });

    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
    // display progress bar
    }




    @SuppressLint("WrongConstant")
    private void deleteInventoryData(String productId) {


        try {
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + context.getPackageName() + "/newinventory.db", null, 0);
            mydatabase.beginTransaction();
            int result=mydatabase.delete("tbl_product","productName"+" =?",new String[]{String.valueOf(productId)});
            mydatabase.setTransactionSuccessful();
            mydatabase.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void m1() {
        Toast.makeText(context, "interface deselect", Toast.LENGTH_SHORT).show();
    }
    @Override
    public int getItemCount() {
        return manageInventoriesDetails.size();
    }

    public void filterList(ArrayList<ManageInventoryModel> filteredList) {
        manageInventoriesDetails = filteredList;
        notifyDataSetChanged();
    }

    public interface CallBackUs {
        void updateInventoryAfterDelete();

    }

}
