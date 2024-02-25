package com.shipgig.thegun.pos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.PaymentMethodReportsModel;

import java.util.ArrayList;

public class PaymentMethodReportsAdapter extends RecyclerView.Adapter<PaymentMethodReportsAdapter.ViewHolder> {
    ArrayList<PaymentMethodReportsModel>reports;
    Context context;

    public PaymentMethodReportsAdapter(ArrayList<PaymentMethodReportsModel> tReports, Context context) {
        this.context = context;
        this.reports = tReports;
    }



    @Override
    public PaymentMethodReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.paymentmethod_reports_details, parent, false);
        return new PaymentMethodReportsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PaymentMethodReportsAdapter.ViewHolder holder, int position) {
        holder.txtSrno.setText(reports.get(position).getSrno());
        holder.txtDisc.setText(reports.get(position).getDisc());
        holder.txtTotalAmount.setText(reports.get(position).getTotalamount());
        holder.txtGst.setText(reports.get(position).getCgst());
        holder.txtQuantity.setText(reports.get(position).getQuantity());
        holder.txtDate.setText(reports.get(position).getDate());
        holder.txtMode.setText(reports.get(position).getPyMode());
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSrno,txtDisc,txtTotalAmount,txtGst,txtQuantity,txtDate,txtMode;
        LinearLayout ll_background;


        public ViewHolder(View itemView) {
            super(itemView);
            ll_background =  itemView.findViewById(R.id.ll_background);
            txtSrno = itemView.findViewById(R.id.srno);
            txtDisc = itemView.findViewById(R.id.disc);
            txtTotalAmount = itemView.findViewById(R.id.totalamount);
            txtGst = itemView.findViewById(R.id.gst);
            txtQuantity = itemView.findViewById(R.id.quantityy);
            txtDate = itemView.findViewById(R.id.date);
            txtMode = itemView.findViewById(R.id.mode);
        }
    }
}
