package com.shipgig.thegun.pos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.GstReportsModel;

import java.util.ArrayList;

public class GstReportsAdapter extends RecyclerView.Adapter<GstReportsAdapter.ViewHolder> {

    ArrayList<GstReportsModel> reports;
    Context context;

    public GstReportsAdapter(ArrayList<GstReportsModel> Reports, Context context) {
        this.context = context;
        this.reports = Reports;
    }



    @Override
    public GstReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gst_reports_details, parent, false);
        return new GstReportsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GstReportsAdapter.ViewHolder holder, int position) {
        holder.txtSrno.setText(reports.get(position).getSrno());
        holder.txtHsnno.setText(reports.get(position).getHsnno());
        holder.txtDescription.setText(reports.get(position).getDescription());
        holder.txtUqc.setText(reports.get(position).getUqc());
        holder.txtTotalQuantity.setText(reports.get(position).getTotalQuantiy());
        holder.txtTotalvalue.setText(reports.get(position).getTotalValue());
        holder.txtIgst.setText(reports.get(position).getIgst());
        holder.txtCgst.setText(reports.get(position).getCgst());
        holder.txtSgst.setText(reports.get(position).getSgst());
        holder.txtCess.setText(reports.get(position).getCess());

    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSrno,txtHsnno,txtDescription,txtUqc,txtTotalvalue,txtTotalQuantity,txtIgst,txtCgst,txtSgst,txtCess;
        LinearLayout ll_background;


        public ViewHolder(View itemView) {
            super(itemView);
            ll_background =  itemView.findViewById(R.id.ll_background);
            txtSrno = itemView.findViewById(R.id.srno);
            txtHsnno = itemView.findViewById(R.id.hsnno);
            txtDescription = itemView.findViewById(R.id.description);
            txtUqc = itemView.findViewById(R.id.uqc);
            txtTotalQuantity = itemView.findViewById(R.id.quantity);
            txtTotalvalue = itemView.findViewById(R.id.totalvalue);
            txtIgst = itemView.findViewById(R.id.igst);
            txtCgst = itemView.findViewById(R.id.cgst);
            txtSgst = itemView.findViewById(R.id.sgst);
            txtCess = itemView.findViewById(R.id.cess);

        }
    }
}
