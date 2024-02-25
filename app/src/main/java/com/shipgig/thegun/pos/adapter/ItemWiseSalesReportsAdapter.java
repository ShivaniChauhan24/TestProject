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
import com.shipgig.thegun.pos.model.ItemwiseReportsModel;

import java.util.ArrayList;

public class ItemWiseSalesReportsAdapter extends RecyclerView.Adapter<ItemWiseSalesReportsAdapter.ViewHolder> {
    ArrayList<ItemwiseReportsModel>reports;
    Context context;
   private ArrayList<String>searchItem;

    public ItemWiseSalesReportsAdapter(ArrayList<ItemwiseReportsModel> tReports, Context context) {
        this.context = context;
        this.reports = tReports;
    }



    @Override
    public ItemWiseSalesReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wise_sales_reports_details, parent, false);
        return new ItemWiseSalesReportsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemWiseSalesReportsAdapter.ViewHolder holder, int position) {
        try {
            holder.txtSrno.setText(reports.get(position).getSrno());
            holder.txtProductId.setText(reports.get(position).getProductid());
            holder.txtProductName.setText(reports.get(position).getProductname());
            holder.txtQuantity.setText(reports.get(position).getQuantity());
        }catch (NullPointerException e) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSrno,txtQuantity,txtProductName,txtProductId;
        LinearLayout ll_background;
        Spinner spinner;


        public ViewHolder(View itemView) {
            super(itemView);
            ll_background =  itemView.findViewById(R.id.ll_background);
            txtSrno = itemView.findViewById(R.id.srno);
            txtProductName = itemView.findViewById(R.id.productname);
            txtProductId = itemView.findViewById(R.id.productid);

            txtQuantity = itemView.findViewById(R.id.quantity);
            spinner =  itemView.findViewById(R.id.spinner);

        }
    }
}
