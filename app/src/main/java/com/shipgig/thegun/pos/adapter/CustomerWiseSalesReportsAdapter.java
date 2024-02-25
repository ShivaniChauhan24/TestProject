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
import android.widget.Toast;

import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.CustomerwiseReportsModel;

import java.util.ArrayList;

public class CustomerWiseSalesReportsAdapter extends RecyclerView.Adapter<CustomerWiseSalesReportsAdapter.ViewHolder> {
    ArrayList<CustomerwiseReportsModel>reports;
    Context context;
    private ArrayList<String>searchItem;

    public CustomerWiseSalesReportsAdapter(ArrayList<CustomerwiseReportsModel> tReports, Context context) {
        this.context = context;
        this.reports = tReports;
    }



    @Override
    public CustomerWiseSalesReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_wise_sales_reports_details, parent, false);
        return new CustomerWiseSalesReportsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomerWiseSalesReportsAdapter.ViewHolder holder, int position) {
        try {
            holder.txtSrno.setText(reports.get(position).getSrno());
            holder.txttCustomerId.setText(reports.get(position).getCustomerid());
            holder.txtCustomerName.setText(reports.get(position).getName());
            holder.txtGender.setText(reports.get(position).getGender());
            holder.txtStoreId.setText(reports.get(position).getStoreId());
            holder.txtTotaltransaction.setText(reports.get(position).getTotaltransaction());
            holder.txtToalsales.setText(reports.get(position).getTotalsale());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.simple_drop_down, R.id.text1, searchItem);
            holder.spinner.setAdapter(adapter);
        } catch(NullPointerException n) {
            Toast.makeText(context, "There is data for customer", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSrno,txttCustomerId,txtCustomerName,txtGender,txtStoreId,txtTotaltransaction,txtToalsales;
        LinearLayout ll_background;
        Spinner spinner;


        public ViewHolder(View itemView) {
            super(itemView);
            ll_background =  itemView.findViewById(R.id.ll_background);
            txtSrno = itemView.findViewById(R.id.srno);
            txttCustomerId = itemView.findViewById(R.id.customerid);
            txtCustomerName = itemView.findViewById(R.id.customername);
            txtGender = itemView.findViewById(R.id.gender);
            txtStoreId = itemView.findViewById(R.id.storeid);
            txtTotaltransaction = itemView.findViewById(R.id.totaltransaction);
            txtToalsales = itemView.findViewById(R.id.totalsale);
            spinner = itemView.findViewById(R.id.spinner);

        }
    }
}
