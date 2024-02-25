package com.shipgig.thegun.pos.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.activities.EditCustomer;
import com.shipgig.thegun.pos.model.NewCustomerModel;

import java.util.List;

public class NewCustomerAdapter extends RecyclerView.Adapter<NewCustomerAdapter.MyViewHolder> {

    private List<NewCustomerModel> customerList;
     Context context;
    NewCustomerModel newCustomerModel;

    public NewCustomerAdapter(List<NewCustomerModel> customerList, Context context) {
        this.context = context;
        this.customerList = customerList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView personid, name, email,mobile,itemEdit,fname,lname,gender;
        LinearLayout ll_background;


        public MyViewHolder(View view) {
            super(view);
            personid = view.findViewById(R.id.personid);
            name =  view.findViewById(R.id.name);
            email =  view.findViewById(R.id.email);
            mobile =  view.findViewById(R.id.mobile);
            fname =  view.findViewById(R.id.firstname);
            lname =  view.findViewById(R.id.lastname);
            gender =  view.findViewById(R.id.gender);
            ll_background =  view.findViewById(R.id.ll_background);
            itemEdit =view.findViewById(R.id.itemEdit);

            itemEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditCustomer.class);
                    intent.putExtra("id",personid.getText().toString());
                    intent.putExtra("firstName",fname.getText().toString());
                    intent.putExtra("lastname",lname.getText().toString());
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("phonenumber",mobile.getText().toString());
                    intent.putExtra("sex",gender.getText().toString());
                    context.startActivity(intent);

                }
            });
        }
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_customer_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        newCustomerModel = customerList.get(position);
        holder.personid.setText(""+newCustomerModel.getPersonid());
        holder.name.setText(newCustomerModel.getName()+" "+newCustomerModel.getLname());
        holder.email.setText(newCustomerModel.getEmail());
        holder.mobile.setText(newCustomerModel.getMobile());
        holder.fname.setText(newCustomerModel.getName());
        holder.lname.setText(newCustomerModel.getLname());
        holder.gender.setText(newCustomerModel.getGender());




        if(position%2==1){
            holder.ll_background.setBackgroundColor(Color.parseColor("#eeeeee"));
        }

        else{
            holder.ll_background.setBackgroundColor(Color.parseColor("#fafafa"));
        }

    }

    @Override
    public int getItemCount() {
         return customerList.size();
    }
}