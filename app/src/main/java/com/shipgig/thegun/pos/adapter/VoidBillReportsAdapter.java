package com.shipgig.thegun.pos.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.activities.BillDetailActivity;
import com.shipgig.thegun.pos.model.ReportsModel;
import com.shipgig.thegun.pos.model.VoidBillReportsModel;

import java.util.ArrayList;

public class VoidBillReportsAdapter extends RecyclerView.Adapter<VoidBillReportsAdapter.ViewHolder> {
    ArrayList<ReportsModel>reports;
    Context context;

    public VoidBillReportsAdapter(ArrayList<ReportsModel> tReports, Context context) {
        this.context = context;
        this.reports = tReports;
    }



    @Override
    public VoidBillReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.void_bill_reports_details, parent, false);
        return new VoidBillReportsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VoidBillReportsAdapter.ViewHolder holder, int position) {

        holder.txtSrno.setText(reports.get(position).getSrno());
        holder.txtDisc.setText(reports.get(position).getDisc());
        holder.txtTotalAmount.setText(reports.get(position).getTotalamount());
        holder.txtCgst.setText(reports.get(position).getCgst());
        holder.txtQuantity.setText(reports.get(position).getQuantity());
        holder.txtDate.setText(reports.get(position).getDate());

        String sr_no = reports.get(position).getSrno();
        String discount = reports.get(position).getDisc();
        String total_amount = reports.get(position).getTotalamount();
        String paymode = reports.get(position).getPaymode();
        String cgst = reports.get(position).getCgst();
        String quantity = reports.get(position).getQuantity();
        String transaction_id = reports.get(position).getTransactionId();
        String customer_id = reports.get(position).getCustomerId();
        String name = reports.get(position).getCustomer_name();
        String time = reports.get(position).getDate();

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, BillDetailActivity.class);
                intent.putExtra("transaction",transaction_id);
                intent.putExtra("discount",discount);
                intent.putExtra("totalrs",total_amount);
                intent.putExtra("qty",quantity);
                intent.putExtra("paymode",paymode);
                intent.putExtra("cust_id",customer_id);
                intent.putExtra("name",name);
//                context.startActivity(intent);


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

        TextView txtSrno,txtDisc,txtTotalAmount,txtCgst,txtQuantity,txtDate,txtTime,transactioId,customerId,cusName;
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
            txtTime =  itemView.findViewById(R.id.time);
            transactioId =  itemView.findViewById(R.id.transaction_id);
            customerId =  itemView.findViewById(R.id.customer_id);
            view = itemView.findViewById(R.id.slash_eye);
            view.setTypeface(fontAwesomeFont);
        }
    }
}
