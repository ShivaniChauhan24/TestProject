package com.shipgig.thegun.pos.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
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
import com.shipgig.thegun.pos.model.ReportsModel;

import java.util.ArrayList;

/**
 * Created by Afroz Ahmad on 26/9/19.
 */
public class LastTenReportAdapter extends RecyclerView.Adapter<LastTenReportAdapter.ViewHolder> {
    ArrayList<ReportsModel> reports;
    Context context;

    String sr_no,discount,total_amount,paymode,sgst,cgst,quantity,date,time,transaction_id,customer_id,name;

    public LastTenReportAdapter(ArrayList<ReportsModel> tReports, Context context) {
        this.context = context;
        this.reports = tReports;
    }



    @Override
    public LastTenReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.last_ten_item, parent, false);
        return new LastTenReportAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtSrno.setText(reports.get(position).getSrno());
        holder.txtTotalAmount.setText(reports.get(position).getTotalamount());
        holder.paymode.setText(reports.get(position).getPaymode());

    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSrno,txtTotalAmount,paymode;


        public ViewHolder(View itemView) {
            super(itemView);
            txtSrno =  itemView.findViewById(R.id.srno);
            txtTotalAmount =  itemView.findViewById(R.id.totalamount);
            paymode =  itemView.findViewById(R.id.pay_mode);

        }
    }
}

