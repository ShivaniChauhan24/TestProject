package com.shipgig.thegun.pos.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.activities.BillDetailActivity;
import com.shipgig.thegun.pos.activities.TransactionReportsActivity;
import com.shipgig.thegun.pos.activities.VoidBillReportsActivity;
import com.shipgig.thegun.pos.model.ReportsModel;

import java.util.ArrayList;

public class TransactionReportsAdapter extends RecyclerView.Adapter<TransactionReportsAdapter.ViewHolder> {
    ArrayList<ReportsModel>reports;
    Context context;
    private int from;

    public TransactionReportsAdapter(ArrayList<ReportsModel> tReports, Context context, int from) {
        this.context = context;
        this.reports = tReports;
        this.from = from;
    }



    @Override
    public TransactionReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_reports_details, parent, false);
        return new TransactionReportsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TransactionReportsAdapter.ViewHolder holder, int position) {

        holder.txtSrno.setText(reports.get(position).getSrno());
        holder.txtDisc.setText("₹"+reports.get(position).getDisc());
        holder.txtTotalAmount.setText("₹"+reports.get(position).getTotalamount());
        holder.txtCgst.setText("₹"+reports.get(position).getCgst());
        holder.txtQuantity.setText(reports.get(position).getQuantity());
        holder.txtDate.setText(reports.get(position).getDate());


        String discount = reports.get(position).getDisc();
        String total_amount = reports.get(position).getTotalamount();
        String paymode = reports.get(position).getPaymode();
        String cgst = reports.get(position).getCgst();
        String quantity = reports.get(position).getQuantity();
        String transaction_id = reports.get(position).getTransactionId();
        String customer_id = reports.get(position).getCustomerId();
        String name = reports.get(position).getCustomer_name();


            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, BillDetailActivity.class);
                    intent.putExtra("transaction", transaction_id);
                    intent.putExtra("discount", discount);
                    intent.putExtra("totalrs", total_amount);
                    intent.putExtra("qty", quantity);
                    intent.putExtra("paymode", paymode);
                    intent.putExtra("cust_id", customer_id);
                    intent.putExtra("name", name);
                    intent.putExtra("taxes", cgst);
                    if (from == 1){
                        intent.putExtra("from_adapter", "transaction");
                    }
                    else {
                        intent.putExtra("from_adapter", "void");
                    }
                    context.startActivity(intent);

//                holder.view.setText(context.getText(R.string.font_awesom_eye_open));
//                holder.dialog.setContentView(R.layout.bill_detail_item);
//                TextView customername = holder.dialog.findViewById(R.id.customer_name);
//                TextView headingName = holder.dialog.findViewById(R.id.customer_name_dialog);
//                TextView customerIds = holder.dialog.findViewById(R.id.customer_id);
//                TextView transactionIds = holder.dialog.findViewById(R.id.Transaction_id);
//                TextView totalItems = holder.dialog.findViewById(R.id.items_in_cart);
//                TextView subTotals = holder.dialog.findViewById(R.id.sub_total);
//                TextView taxes = holder.dialog.findViewById(R.id.cgst_amount);
//                TextView dates = holder.dialog.findViewById(R.id.date);
//                TextView totalAmounts = holder.dialog.findViewById(R.id.total_amount);
//                TextView total_discount = holder.dialog.findViewById(R.id.total_discount);
//                TextView pay_mode = holder.dialog.findViewById(R.id.paymentMode);
//
//                try{
//                    customername.setText(name);
//                    headingName.setText(name);
//                    customerIds.setText(customer_id);
//                    transactionIds.setText(transaction_id);
//                    totalAmounts.setText(total_amount);
//                    totalItems.setText(quantity);
//                    subTotals.setText(total_amount);
//                    taxes.setText(cgst);
//                    dates.setText(time);
//                    pay_mode.setText(paymode);
//                    total_discount.setText(discount);
//
//                }catch (NullPointerException e){
//                    Toast.makeText(context, "Not set in preview :-"+e, Toast.LENGTH_SHORT).show();
//                }
//
//                holder.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                holder.dialog.setCancelable(false);
//                holder.dialog.show();
//                ImageView imageView = holder.dialog.findViewById(R.id.imageView);
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        holder.view.setText(context.getText(R.string.font_awesom_eye_slash));
//                        holder.dialog.dismiss();
//
//                    }
//                });
                }
            });

    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSrno,txtDisc,txtTotalAmount,txtCgst,txtQuantity,txtDate,transactioId,customerId,cusName;
        LinearLayout ll_background;
        TextView view;
        Dialog dialog = new Dialog(context);


        public ViewHolder(View itemView) {
            super(itemView);
            Typeface fontAwesomeFont = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
            ll_background =  itemView.findViewById(R.id.ll_background);
            cusName =  itemView.findViewById(R.id.customer_name);
            txtSrno =  itemView.findViewById(R.id.srno);
            txtDisc =  itemView.findViewById(R.id.disc);
            txtTotalAmount =  itemView.findViewById(R.id.totalamount);
//            paymode =  itemView.findViewById(R.id.mode);
            txtCgst =  itemView.findViewById(R.id.cgst);
            txtQuantity =  itemView.findViewById(R.id.quantity);
            txtDate =  itemView.findViewById(R.id.date);
            transactioId =  itemView.findViewById(R.id.transaction_id);
            customerId =  itemView.findViewById(R.id.customer_id);
            view = itemView.findViewById(R.id.slash_eye);
            view.setTypeface(fontAwesomeFont);


        }

    }
}
