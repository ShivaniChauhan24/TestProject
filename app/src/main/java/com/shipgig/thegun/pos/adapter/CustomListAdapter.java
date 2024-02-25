package com.shipgig.thegun.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.GSTModel;
import java.util.ArrayList;


public class CustomListAdapter extends ArrayAdapter<GSTModel>{
    Context context;
    DrawerItemHolder drawerHolder;

    public CustomListAdapter(Context context,int resource,ArrayList<GSTModel> itemsList) {
        super(context, resource, itemsList);

    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                drawerHolder = new DrawerItemHolder();
                view = inflater.inflate(R.layout.hsn_no_listitem,
                        viewGroup, false);

                drawerHolder.hsn_no = (TextView) view.findViewById(R.id.hsn_item);

                view.setTag(drawerHolder);
            } else {
                drawerHolder = (DrawerItemHolder) view.getTag();
            }
            GSTModel model = getItem(position);
            drawerHolder.hsn_no.setText(model.getHsn_no());


            return view;
        }


    private class DrawerItemHolder
    {
        TextView hsn_no;

    }
    }

