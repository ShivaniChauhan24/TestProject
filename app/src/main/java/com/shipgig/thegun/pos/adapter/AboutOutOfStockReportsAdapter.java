package com.shipgig.thegun.pos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.AboutOutOfStockReportsModel;
import java.util.ArrayList;

public class AboutOutOfStockReportsAdapter extends RecyclerView.Adapter<AboutOutOfStockReportsAdapter.ViewHolder> {
    ArrayList<AboutOutOfStockReportsModel>reports;
    Context context;

    public AboutOutOfStockReportsAdapter(ArrayList<AboutOutOfStockReportsModel> tReports, Context context) {
        this.context = context;
        this.reports = tReports;
    }



    @Override
    public AboutOutOfStockReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.about_out_of_stock_details, parent, false);
        return new AboutOutOfStockReportsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AboutOutOfStockReportsAdapter.ViewHolder holder, int position) {
        holder.txtProductId.setText(reports.get(position).getProductId());
        holder.txtProductName.setText(reports.get(position).getProductName());
        holder.txtUnitPrice.setText(reports.get(position).getUnitPrice());
        holder.txtCostPrice.setText(reports.get(position).getStock());
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
