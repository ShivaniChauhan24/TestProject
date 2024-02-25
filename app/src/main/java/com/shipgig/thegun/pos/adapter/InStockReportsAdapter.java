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
import com.shipgig.thegun.pos.model.InStockReportsModel;


import java.util.ArrayList;

public class InStockReportsAdapter extends RecyclerView.Adapter<InStockReportsAdapter.ViewHolder> {
    ArrayList<InStockReportsModel>reports;
    Context context;

    public InStockReportsAdapter(ArrayList<InStockReportsModel> tReports, Context context) {
        this.context = context;
        this.reports = tReports;
    }



    @Override
    public InStockReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.out_of_stock_details, parent, false);
        return new InStockReportsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(InStockReportsAdapter.ViewHolder holder, int position) {
        holder.txtProductId.setText(reports.get(position).getProductId());
        holder.txtProductName.setText(reports.get(position).getProductName());
        holder.txtUnitPrice.setText(reports.get(position).getUnitPrice());
        holder.txtCostPrice.setText(reports.get(position).getCostPrice());
        holder.txtExpirydate.setText(reports.get(position).getExpiryDate());

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

        TextView txtProductName,txtProductId,txtUnitPrice,txtCostPrice,txtExpirydate;
        LinearLayout ll_background;


        public ViewHolder(View itemView) {
            super(itemView);
            ll_background =  itemView.findViewById(R.id.ll_background);
            txtProductName = itemView.findViewById(R.id.productname);
            txtProductId = itemView.findViewById(R.id.productid);
            txtUnitPrice = itemView.findViewById(R.id.unitprice);
            txtCostPrice = itemView.findViewById(R.id.costprice);
            txtExpirydate = itemView.findViewById(R.id.expirydate);

        }
    }
}
