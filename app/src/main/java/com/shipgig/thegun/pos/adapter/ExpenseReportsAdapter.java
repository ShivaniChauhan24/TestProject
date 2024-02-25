package com.shipgig.thegun.pos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.ExpenseReportsModel;

import java.util.ArrayList;

public class ExpenseReportsAdapter extends RecyclerView.Adapter<ExpenseReportsAdapter.ViewHolder> {
    ArrayList<ExpenseReportsModel> reports;
    Context context;

    public ExpenseReportsAdapter(ArrayList<ExpenseReportsModel> tReports, Context context) {
        this.context = context;
        this.reports = tReports;
    }

    @Override
    public ExpenseReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_reports_details, parent, false);
        return new ExpenseReportsAdapter.ViewHolder(v);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ExpenseReportsAdapter.ViewHolder holder, int position) {
        holder.txtSrno.setText(reports.get(position).getSrno());
        holder.txtUserID.setText(reports.get(position).getUserId());
        holder.txtExpenseType.setText(reports.get(position).getExpenseType());
        holder.txtExpenseDescription.setText(reports.get(position).getExpenseDescription());
        holder.txtAmount.setText(reports.get(position).getAmount());
        holder.txtCreateOn.setText(reports.get(position).getCreateOn());

    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSrno,txtUserID,txtExpenseType,txtExpenseDescription,txtAmount,txtCreateOn;
        LinearLayout ll_background;


        public ViewHolder(View itemView) {
            super(itemView);
            ll_background =  itemView.findViewById(R.id.ll_background);
            txtSrno = itemView.findViewById(R.id.srno);
            txtUserID = itemView.findViewById(R.id.userid);
            txtExpenseType = itemView.findViewById(R.id.expensetype);
            txtExpenseDescription = itemView.findViewById(R.id.expensedescription);
            txtAmount = itemView.findViewById(R.id.amount);
            txtCreateOn = itemView.findViewById(R.id.createon);

        }
    }

}
