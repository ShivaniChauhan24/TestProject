package com.shipgig.thegun.pos.adapter;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.ManageCustomerDetailsModel;
import java.util.ArrayList;
import java.util.List;

public class AutoCompleteCustomerAdapter extends ArrayAdapter<ManageCustomerDetailsModel> {
    private List<ManageCustomerDetailsModel> customerListFull;
    public static String customerIdTr;
    public AutoCompleteCustomerAdapter(@NonNull Context context, @NonNull List<ManageCustomerDetailsModel> customerList) {
        super(context, 0, customerList);
        customerListFull = new ArrayList<>(customerList);
    }
 
    @NonNull
    @Override
    public Filter getFilter() {
        return customerFilter;
    }
 
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.manage_customer_actv, parent, false
            );
        }

        TextView textViewName =  convertView.findViewById(R.id.firstname);
        TextView  textViewCustomerID = (TextView) convertView.findViewById(R.id.customerid);
        TextView textViewLastName = (TextView) convertView.findViewById(R.id.lastname);

        ManageCustomerDetailsModel countryItem = getItem(position);
 
        if (countryItem != null) {
            textViewName.setText(countryItem.getFirstName());
            textViewCustomerID.setText(countryItem.getCustomerId());
            textViewLastName.setText(countryItem.getLastName());

        }
 
        return convertView;
    }
    private Filter customerFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<ManageCustomerDetailsModel> suggestions = new ArrayList<>();
 
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(customerListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
 
                for (ManageCustomerDetailsModel item : customerListFull) {
                    if (item.getFirstName().toLowerCase().contains(filterPattern) || item.getMobileNumber().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }
 
            results.values = suggestions;
            results.count = suggestions.size()-1;
 
            return results;
        }
 
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }
 
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            customerIdTr= ((ManageCustomerDetailsModel)resultValue).getCustomerId();
            Log.d("cidtheguna",customerIdTr);
            return ((ManageCustomerDetailsModel) resultValue).getFirstName();
        }
    };
}