package com.shipgig.thegun.pos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.ProfitReportsModel;

import java.util.ArrayList;

public class ProfitReportsAdapter extends RecyclerView.Adapter<ProfitReportsAdapter.ViewHolder> {

    ArrayList<ProfitReportsModel>reports;
    Context context;

    public ProfitReportsAdapter(ArrayList<ProfitReportsModel> tReports, Context context) {
        this.context = context;
        this.reports = tReports;
    }



    @Override
    public ProfitReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profit_reports_details, parent, false);
        return new ProfitReportsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProfitReportsAdapter.ViewHolder holder, int position) {
        holder.txtSrno.setText(reports.get(position).getSrno());
        holder.txtTransactionID.setText(reports.get(position).getTransactionId());
        holder.txtCustomerId.setText(reports.get(position).getCustomerId());
        holder.txtTotalSale.setText(reports.get(position).getSaleTotal());
        holder.txtProfit.setText(reports.get(position).getProfit());

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

        TextView txtSrno,txtTransactionID,txtProfit,txtCustomerId,txtTotalSale;
        LinearLayout ll_background;


        public ViewHolder(View itemView) {
            super(itemView);
            ll_background =  itemView.findViewById(R.id.ll_background);
            txtSrno = itemView.findViewById(R.id.srno);
            txtCustomerId = itemView.findViewById(R.id.customerid);
            txtTransactionID = itemView.findViewById(R.id.transactionid);
            txtProfit = itemView.findViewById(R.id.profit);
            txtTotalSale = itemView.findViewById(R.id.saletotal);


        }
    }
}
