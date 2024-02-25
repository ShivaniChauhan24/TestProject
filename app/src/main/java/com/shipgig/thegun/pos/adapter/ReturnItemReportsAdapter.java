package com.shipgig.thegun.pos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.ReturnItemReportsModel;

import java.util.ArrayList;

public class ReturnItemReportsAdapter extends RecyclerView.Adapter<ReturnItemReportsAdapter.ViewHolder> {
    ArrayList<ReturnItemReportsModel>reports;
    Context context;

    public ReturnItemReportsAdapter(ArrayList<ReturnItemReportsModel> tReports, Context context) {
        this.context = context;
        this.reports = tReports;
    }



    @Override
    public ReturnItemReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.return_item_details, parent, false);
        return new ReturnItemReportsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReturnItemReportsAdapter.ViewHolder holder, int position) {
        holder.txtSrno.setText(reports.get(position).getSrno());
        holder.txtTransactionID.setText(reports.get(position).getTransactionId());
        holder.txtCustomerId.setText(reports.get(position).getCustomerId());
        holder.txtPaymentMode.setText(reports.get(position).getPaymentMode());
        holder.txtReturnDate.setText(reports.get(position).getReturnDate());
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSrno,txtTransactionID,txtCustomerId,txtPaymentMode,txtReturnDate;
        LinearLayout ll_background;


        public ViewHolder(View itemView) {
            super(itemView);
            ll_background =  itemView.findViewById(R.id.ll_background);
            txtSrno = itemView.findViewById(R.id.srno);
            txtCustomerId = itemView.findViewById(R.id.customerid);
            txtTransactionID = itemView.findViewById(R.id.transactionid);
            txtPaymentMode = itemView.findViewById(R.id.paymentmode);
            txtReturnDate = itemView.findViewById(R.id.returndate);


        }
    }
}
