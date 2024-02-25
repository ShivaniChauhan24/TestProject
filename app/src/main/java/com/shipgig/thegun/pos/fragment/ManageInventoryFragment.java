package com.shipgig.thegun.pos.fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shipgig.thegun.pos.MainActivity;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.adapter.InventoryAdapter;
import com.shipgig.thegun.pos.adapter.ManageCustomerDetailsAdapter;
import com.shipgig.thegun.pos.adapter.ManageInventoryDetailsAdapter;
import com.shipgig.thegun.pos.model.AboutOutOfStockReportsModel;
import com.shipgig.thegun.pos.model.ManageCustomerDetailsModel;
import com.shipgig.thegun.pos.model.ManageInventoryModel;
import com.shipgig.thegun.pos.utilities.Sharepreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.shipgig.thegun.pos.MainActivity.drawer;

/**
 * Created by Afroz on 18/07/19.
 */
public class ManageInventoryFragment extends Fragment implements InventoryAdapter.CallBackUs  {

    View myView;
    ImageView open_drawer;
    ArrayList<ManageInventoryModel> data = new ArrayList<>();
    Context  context;
    RecyclerView recyclerView;
    public JSONArray jsonArrayInventory = new JSONArray();
    InventoryAdapter inventoryAdapter;
    EditText search;
    ManageInventoryModel manageInventoryModel;


    TextView nextBtn, prevBtn;
    TextView pageNumber;
    private Button[] btns;
    private int noOfBtns;
    private int currentPage;

    public  int TOTAL_NUM_ITEMS;
    public  int ITEMS_PER_PAGE=7;
    public int ITEMS_REMAINING;
    public  int LAST_PAGE;
    private int totalPages=0;
    int storeID;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.manage_inventory, container, false);

        storeID = Sharepreference.getSharedPreferenceInt(getActivity(),"storeID",0);
        init();
        open_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });

        seeInventorydata();

        try {
            TOTAL_NUM_ITEMS = jsonArrayInventory.length();
        } catch (NullPointerException e) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        }
        pageNumber = myView.findViewById(R.id.number);
        nextBtn =  myView.findViewById(R.id.next);
        prevBtn =  myView.findViewById(R.id.previous);
        recyclerView = myView.findViewById(R.id.v_recyclerView);
        search = myView.findViewById(R.id.search);
        open_drawer = myView.findViewById(R.id.open_drawer);

        nextBtn.setVisibility(View.GONE);
        prevBtn.setVisibility(View.GONE);


        Btnfooter();
        loadList(0);
        CheckBtnBackGroud(0);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


        return myView;
    }


    private void init() {
        context = getActivity();
        open_drawer = myView.findViewById(R.id.open_drawer);
        recyclerView = myView.findViewById(R.id.v_recyclerView);
        search = myView.findViewById(R.id.search);
        pageNumber = myView.findViewById(R.id.number);
        nextBtn =  myView.findViewById(R.id.next);
        prevBtn =  myView.findViewById(R.id.previous);
    }



    private void recycler() {
        if (data.size()>0) {
            data.clear();
        }
        seeInventorydata();
        inventoryAdapter = new InventoryAdapter(data,context,this);
        inventoryAdapter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(inventoryAdapter);


    }

    private void filter(String text) {
        ArrayList<ManageInventoryModel> filteredList = new ArrayList<>();

        for (ManageInventoryModel item : data) {
            if (item.getProdict().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        try {
            inventoryAdapter.filterList(filteredList);
        }catch (NullPointerException e) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    private void Btnfooter() {
        int val = TOTAL_NUM_ITEMS%ITEMS_PER_PAGE;
        val = val==0 ? 0:1;

        noOfBtns=TOTAL_NUM_ITEMS/ITEMS_PER_PAGE+val;

        LinearLayout ll = myView.findViewById(R.id.btnLay);

        btns    =new Button[noOfBtns];

        for(int i=0;i<noOfBtns;i++)
        {
            btns[i] =   new Button(context);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            btns[i].setText(""+(i+1));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.addView(btns[i], lp);

            int finalI1 = i;

            int finalI = i;

            final int j = i;
            btns[j].setOnClickListener(new View.OnClickListener() {

                public void onClick(View v)
                {
                    loadList(j);
                    CheckBtnBackGroud(j);
                }
            });
        }

    }

    private void CheckBtnBackGroud(int index) {
        pageNumber.setText("Page "+(index+1)+" of "+noOfBtns);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                new LinearLayout.LayoutParams(55,45)
        );
        params.setMargins(5, 0, 0, 0);

        for(int i=0;i<noOfBtns;i++)
        {
            if(i==index)
            {
                btns[i].setBackground(getResources().getDrawable(R.drawable.pagignation_button_border));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
                btns[i].setBackgroundResource(R.drawable.pagignation_button_border);
                btns[i].setBackgroundColor(getResources().getColor(R.color.humburg_icon_color));
                btns[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, 15);
                btns[i].setLayoutParams(params);
            }
            else
            {
                btns[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.pagignation_button_border));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
                btns[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, 15);
                btns[i].setLayoutParams(params);
            }
        }

    }

    private void loadList(int number) {
        ArrayList<ManageInventoryModel> sort = new ArrayList<>();

        int start = number * ITEMS_PER_PAGE;
        for(int i=start;i<(start)+ITEMS_PER_PAGE;i++)
        {
            if(i<data.size())
            {
                sort.add(data.get(i));

            }
            else
            {
                break;
            }
        }

        inventoryAdapter = new InventoryAdapter(sort,context,this);
        inventoryAdapter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(inventoryAdapter);

    }

    public void seeInventorydata() {

        try {
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getContext().getPackageName() + "/newinventory.db", null, 0);
//            mydatabase.beginTransaction();
            Cursor cursor = mydatabase.rawQuery("SELECT G.gst_Cat_ID,P.productName,P.HSN_ID,P.product_ID,P.unitPrice,P.discount,P.totalstock,P.totalstock,P.category,P.image_path,P.category " +
                    "from tbl_product as P LEFT JOIN tbl_gst as G ON P.HSN_ID = G.HSN_NO where P.store_ID='"+storeID+"'", null);
//            mydatabase.endTransaction();
            jsonArrayInventory = cur2Json(cursor);
            setDataAdptr(jsonArrayInventory);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
//        recycler();
//        inventoryAdapter.notifyDataSetChanged();
    }

    public void setDataAdptr(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                manageInventoryModel = new ManageInventoryModel();
                manageInventoryModel.setSrno((String.valueOf(i)));
                manageInventoryModel.setProdict(jsonobject.optString("productName"));
                manageInventoryModel.setQuantity(jsonobject.optString("totalstock"));
                manageInventoryModel.setProductId(jsonobject.optString("product_ID"));
                manageInventoryModel.setDiscount(jsonobject.optString("discount"));
                manageInventoryModel.setUnitPrice(jsonobject.optString("unitPrice"));
                data.add(manageInventoryModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public JSONArray cur2Json(Cursor cursor) {
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),
                                cursor.getString(i));
                    } catch (Exception e) {
                        Log.e("TestingFor", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;

    }

//    int startItem;
//    int numOfData;
//    public ArrayList<ManageInventoryModel> generatePage(int currentPage, JSONArray jsonArray) {
//        startItem=currentPage*ITEMS_PER_PAGE;//+1;
//        numOfData=ITEMS_PER_PAGE;
//        data= new ArrayList<>();
//        if (currentPage==LAST_PAGE && ITEMS_REMAINING>0)
//        {
//            for (int i=startItem;i<startItem+ITEMS_REMAINING;i++)
//            {
//                JSONObject jsonobject = null;
//                try {
//                    jsonobject = jsonArray.getJSONObject(i);
//                    manageInventoryModel = new ManageInventoryModel();
//                    manageInventoryModel.setSrno((String.valueOf(i)));
//                    manageInventoryModel.setProdict(jsonobject.optString("productName"));
//                    manageInventoryModel.setQuantity(jsonobject.optString("totalstock"));
//                    manageInventoryModel.setProductId(jsonobject.optString("product_ID"));
//                    manageInventoryModel.setDiscount(jsonobject.optString("discount"));
//                    manageInventoryModel.setUnitPrice(jsonobject.optString("unitPrice"));
//                    data.add(manageInventoryModel);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }else
//        {
//            for (int i=startItem;i<startItem+numOfData;i++)
//            {
//                JSONObject jsonobject = null;
//                try {
//                    jsonobject = jsonArray.getJSONObject(i);
//                    manageInventoryModel = new ManageInventoryModel();
//                    manageInventoryModel.setSrno((String.valueOf(i)));
//                    manageInventoryModel.setProdict(jsonobject.optString("productName"));
//                    manageInventoryModel.setQuantity(jsonobject.optString("totalstock"));
//                    manageInventoryModel.setProductId(jsonobject.optString("product_ID"));
//                    manageInventoryModel.setDiscount(jsonobject.optString("discount"));
//                    manageInventoryModel.setUnitPrice(jsonobject.optString("unitPrice"));
//                    data.add(manageInventoryModel);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return data;
//    }

    @Override
    public void updateInventoryAfterDelete() {
            recycler();
    }

}