package com.shipgig.thegun.pos.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shipgig.thegun.pos.MainActivity;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.activities.EditCustomer;
import com.shipgig.thegun.pos.model.ManageCustomerDetailsModel;
import com.shipgig.thegun.pos.utilities.Sharepreference;

import java.util.ArrayList;


public class ManageCustomerDetailsAdapter extends RecyclerView.Adapter<ManageCustomerDetailsAdapter.ViewHolder> {//implements Filterable {

private static RecyclerViewClickListener itemListener;
    ManageCustomerDetailsModel manageCustomerDetailsModel;

public static ArrayList<ManageCustomerDetailsModel> customerDetails;
    Context context;
    public String name;
    private CallBackUs mCallBackus;

    public ManageCustomerDetailsAdapter(ArrayList<ManageCustomerDetailsModel> customerDetails, Context context, CallBackUs CallBackus,RecyclerViewClickListener itemListener) {
        this.context = context;
        this.mCallBackus=CallBackus;
        this.customerDetails = customerDetails;
        this.itemListener = itemListener;
    }


    @Override
    public ManageCustomerDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manage_customer_details, parent, false);
        return new ManageCustomerDetailsAdapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ManageCustomerDetailsAdapter.ViewHolder holder, int position) {

        showData((ViewHolder) holder,position);

    }

    private void deleteInventoryData(String customerId) {

        try{
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + context.getPackageName() + "/newinventory.db", null, 0);
            mydatabase.beginTransaction();

            int result=mydatabase.delete("tbl_custuser","custUser_ID"+" =?",new String[]{String.valueOf(customerId)});
            mydatabase.setTransactionSuccessful();

            mydatabase.endTransaction();

        }catch (Exception e){
           e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return customerDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       TextView textViewName, textViewCustomerID, textViewLastName,
               textViewMobile, textViewGender, textViewSrno,  email;
       LinearLayout ll_background;
       ImageView itemEdit,itemDel;
       ViewHolder(final View itemView) {
           super(itemView);
           textViewSrno =  itemView.findViewById(R.id.srno);
           textViewName =  itemView.findViewById(R.id.firstname);
           textViewCustomerID =  itemView.findViewById(R.id.customerid);
           textViewLastName =  itemView.findViewById(R.id.lastname);
           textViewMobile =  itemView.findViewById(R.id.mobile);
           textViewGender =  itemView.findViewById(R.id.gender);
           email =  itemView.findViewById(R.id.email);
           itemDel =  itemView.findViewById(R.id.itemDel);
           ll_background =  itemView.findViewById(R.id.ll_background);
           itemEdit =  itemView.findViewById(R.id.itemEdit);

       }

       @Override
       public void onClick(View v) {
           itemListener.recyclerViewListClicked(v,manageCustomerDetailsModel, this.getLayoutPosition());
           Toast.makeText(context,textViewName.getText().toString(),Toast.LENGTH_SHORT).show();
           name=textViewName.getText().toString();
           Sharepreference.setSharedPreferenceString(context,"userid",name);

       }

   }


   private void showData(ViewHolder holder, int position){

       manageCustomerDetailsModel = customerDetails.get(position);
       holder.textViewSrno.setText(customerDetails.get(position).getSrno());
       holder.textViewCustomerID.setText(customerDetails.get(position).getCustomerId());
       holder.textViewName.setText(customerDetails.get(position).getFirstName());
       holder.textViewLastName.setText(customerDetails.get(position).getLastName());
       holder.textViewMobile.setText(customerDetails.get(position).getMobileNumber());
       holder.textViewGender.setText(customerDetails.get(position).getGednder());
       holder.email.setText(customerDetails.get(position).getEmailAddress());

       String custId = customerDetails.get(position).getCustomerId();
       String firstName = customerDetails.get(position).getFirstName();
       String lastname = customerDetails.get(position).getLastName();
       String mail = customerDetails.get(position).getEmailAddress();
       String phone = customerDetails.get(position).getMobileNumber();
       String gender = customerDetails.get(position).getGednder();

       holder.itemEdit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(context, EditCustomer.class);
               intent.putExtra("id",custId);
               intent.putExtra("firstName",firstName);
               intent.putExtra("lastname",lastname);
               intent.putExtra("email",mail);
               intent.putExtra("phonenumber",phone);
               intent.putExtra("sex",gender);
               context.startActivity(intent);
           }
       });

       holder.itemDel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
               alertDialog.setTitle(Html.fromHtml("<font color='#FF7F27'>Are you sure to delete?</font>"));
               alertDialog.setCancelable(false);
               alertDialog.setNegativeButton(Html.fromHtml("<font color='#FF7F27'>Cancel</font>"), new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               });
               alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       deleteInventoryData(customerDetails.get(position).getCustomerId());
                       customerDetails.remove(position);
                       notifyItemRemoved(position);
                       notifyItemRangeChanged(position, customerDetails.size());
                       mCallBackus.updateCustomerAfterDelete();

                   }
               });
               alertDialog.create();

               alertDialog.show();
           }
       });
   }


   public void filterList(ArrayList<ManageCustomerDetailsModel> filteredList) {
        customerDetails = filteredList;
        notifyDataSetChanged();
    }

    public interface RecyclerViewClickListener {
        public void recyclerViewListClicked(View v,Object items, int position);
    }

    public interface CallBackUs {
        void updateCustomerAfterDelete();

    }

}
