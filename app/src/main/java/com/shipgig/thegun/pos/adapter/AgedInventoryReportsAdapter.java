package com.shipgig.thegun.pos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.AgedInventoryReportsModel;

import java.util.ArrayList;

public class AgedInventoryReportsAdapter extends RecyclerView.Adapter<AgedInventoryReportsAdapter.ViewHolder> {
    ArrayList<AgedInventoryReportsModel>reports;
    Context context;

    public AgedInventoryReportsAdapter(ArrayList<AgedInventoryReportsModel> tReports, Context context) {
        this.context = context;
        this.reports = tReports;
    }

    @Override
    public AgedInventoryReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.aged_inventory_details, parent, false);
        return new AgedInventoryReportsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AgedInventoryReportsAdapter.ViewHolder holder, int position) {
        holder.txtProductId.setText(reports.get(position).getProductId());
        holder.txtProductName.setText(reports.get(position).getProductName());
        holder.txtUnitPrice.setText(reports.get(position).getUnitPrice());
        holder.txtCostPrice.setText(reports.get(position).getCostPrice());
        holder.txtExpirydate.setText(reports.get(position).getExpiryDate());

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
