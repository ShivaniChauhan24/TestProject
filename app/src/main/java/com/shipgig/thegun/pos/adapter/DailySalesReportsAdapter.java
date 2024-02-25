package com.shipgig.thegun.pos.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.DailySalesReportsModel;

import java.util.ArrayList;

public class DailySalesReportsAdapter extends RecyclerView.Adapter<DailySalesReportsAdapter.ViewHolder> {

    ArrayList<DailySalesReportsModel> dailySalesReportsModels;
    Context context;
    ArrayList<String>searchItem;

    public DailySalesReportsAdapter(ArrayList<DailySalesReportsModel> dailySalesReportsModels, Context context,  ArrayList<String>list) {
        this.context = context;
        this.dailySalesReportsModels = dailySalesReportsModels;
        this.searchItem = list;
    }


    @Override
    public DailySalesReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_sales_reports_details, parent, false);
        return new DailySalesReportsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DailySalesReportsAdapter.ViewHolder holder, int position) {

        holder.srno.setText(dailySalesReportsModels.get(position).getSrno());
        holder.transactionType.setText(dailySalesReportsModels.get(position).getTransactionType());
        holder.Gst.setText("5.0");//setText(dailySalesReportsModels.get(position).getGST());
        holder.TotalAmount.setText(dailySalesReportsModels.get(position).getTotalAmount());
        holder.TotalQty.setText(dailySalesReportsModels.get(position).getTotalQty());
        holder.Time.setText(dailySalesReportsModels.get(position).getTime());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.simple_drop_down,R.id.text1, searchItem);
        holder.spinner.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return dailySalesReportsModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView srno,transactionType,Gst,TotalAmount,TotalQty,Time;
        LinearLayout ll_background;
        Spinner spinner;

        ViewHolder(View itemView) {
            super(itemView);

            ll_background =  itemView.findViewById(R.id.ll_background);
            srno = itemView.findViewById(R.id.srno);
            transactionType = itemView.findViewById(R.id.transactionType);
            Gst = itemView.findViewById(R.id.gst);
            TotalAmount = itemView.findViewById(R.id.totalamount);
            TotalQty = itemView.findViewById(R.id.totalqty);
            Time = itemView.findViewById(R.id.time);
            spinner =  itemView.findViewById(R.id.spinner);
        }
    }
}
