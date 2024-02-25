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

public class LasttenSalesReportsAdapter extends RecyclerView.Adapter<LasttenSalesReportsAdapter.ViewHolder> {
    ArrayList<LastSalesReportsModel>reports;
    Context context;

    public LasttenSalesReportsAdapter(Context context, ArrayList<LastSalesReportsModel> lastSalesReportsModelArrayList) {
        this.context = context;
        this.reports = lastSalesReportsModelArrayList;
    }


    @Override
    public LasttenSalesReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.last_sales_details, parent, false);
        return new LasttenSalesReportsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LasttenSalesReportsAdapter.ViewHolder holder, int position) {
        holder.txtSrno.setText(reports.get(position).getSrno());
        holder.txtRecipt.setText(reports.get(position).getRecieptno());
        holder.txtamount.setText(reports.get(position).getAmount());
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSrno,txtRecipt,txtamount;

        public ViewHolder(View itemView) {
            super(itemView);
            txtSrno = itemView.findViewById(R.id.srno);
            txtRecipt = itemView.findViewById(R.id.receipt);
            txtamount = itemView.findViewById(R.id.totalamount);
        }
    }
}
