package com.shipgig.thegun.pos.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.adapter.AddItemCartAdapter;
import com.shipgig.thegun.pos.model.AddItemCartModel;

import java.util.ArrayList;

public class CartFragment extends Fragment {
  RecyclerView addCartRecyclerView;
  private AddItemCartAdapter addItemCartAdapter;
  public static ArrayList<AddItemCartModel> addItemCartArray = new ArrayList<>();
  Context context;
  private ArrayList<AddItemCartModel> temparraylist = new ArrayList<>();

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view=inflater.inflate(R.layout.fragment_cart, container, false);
    context=getActivity();

    addCartRecyclerView=view.findViewById(R.id.addCartRecyclerView);
    addItemToCartMethod();



    return view;
  }

  private void addItemToCartMethod() {
    for (int i = 0; i < addItemCartArray.size(); i++) {
      for (int j = i + 1; j < addItemCartArray.size(); j++) {
        if (addItemCartArray.get(i).getProductName().equals(addItemCartArray.get(j).getProductName())) {
          addItemCartArray.get(i).setQuantity(addItemCartArray.get(j).getQuantity());
          addItemCartArray.get(i).setProductPrice(addItemCartArray.get(j).getProductPrice());
          addItemCartArray.get(i).setHsn_Number(addItemCartArray.get(j).getHsn_Number());
          addItemCartArray.get(i).setGst_Percent(addItemCartArray.get(j).getGst_Percent());
          addItemCartArray.remove(j);
          j--;
          Log.d("remove", String.valueOf(addItemCartArray.size()));

        }
      }

    }

    temparraylist.addAll(addItemCartArray);
    addItemCartAdapter = new AddItemCartAdapter(temparraylist, context,1);
    LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    addCartRecyclerView.setLayoutManager(verticalLayoutManager);
    addCartRecyclerView.setAdapter(addItemCartAdapter);
    addItemCartAdapter.notifyDataSetChanged();

  }

}
