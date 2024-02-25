package com.shipgig.thegun.pos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.LastSalesReportsModel;

import java.util.ArrayList;

public class LastSalesReportsAdapter extends RecyclerView.Adapter<LastSalesReportsAdapter.ViewHolder> {

    ArrayList<LastSalesReportsModel>reports;
    Context context;


    public LastSalesReportsAdapter(ArrayList<LastSalesReportsModel> lastSalesReportsModelArrayList, Context context) {
        this.context = context;
        this.reports = lastSalesReportsModelArrayList;
    }

    @Override
    public LastSalesReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.last_sales_details, parent, false);
        return new LastSalesReportsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LastSalesReportsAdapter.ViewHolder holder, int position) {
        holder.srno.setText(reports.get(position).getSrno());
        holder.recept.setText(reports.get(position).getRecieptno());
        holder.txtTotalSale.setText(reports.get(position).getAmount());

    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView recept,txtTotalSale,srno;

        public ViewHolder(View itemView) {
            super(itemView);
            srno = itemView.findViewById(R.id.srno);
            recept = itemView.findViewById(R.id.receipt);
            txtTotalSale = itemView.findViewById(R.id.totalamount);


        }
    }
}
