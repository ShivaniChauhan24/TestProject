package com.shipgig.thegun.pos.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.adapter.ManageCustomerDetailsAdapter;
import com.shipgig.thegun.pos.model.ManageCustomerDetailsModel;
import com.shipgig.thegun.pos.model.ManageInventoryModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static com.shipgig.thegun.pos.MainActivity.drawer;

/**
 * Created by afroz on 12/31/15.
 */
public class ManageCustomerFragment extends Fragment implements ManageCustomerDetailsAdapter.CallBackUs, ManageCustomerDetailsAdapter.RecyclerViewClickListener {// implements ManageCustomerDetailsAdapter.ManageCustomerDetailsAdapterListener {

    View myView;
    ArrayList<ManageCustomerDetailsModel>  data = new ArrayList<>();
    ArrayList<ManageInventoryModel> searchData;

    RecyclerView recyclerView;
    ManageCustomerDetailsAdapter customerDetailsAdpter;
    Context context;
    private JSONArray jsonArray = new JSONArray();
    EditText search;
    ManageCustomerDetailsModel manageCustomerDetailsModel;

    TextView nextBtn, prevBtn;
    TextView pageNumber;
    private Button[] btns;
    private int noOfBtns;
    private int currentPage;

    public  int TOTAL_NUM_ITEMS;
    public  int ITEMS_PER_PAGE=6;
    public int ITEMS_REMAINING;
    public  int LAST_PAGE;
    private int totalPages=0;
    ImageView open_drawer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.manage_customer, container, false);

        setHasOptionsMenu(true);
        seeCustomerEditdata();
        try {
            TOTAL_NUM_ITEMS = jsonArray.length();
        } catch (NullPointerException e) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        }
        pageNumber = myView.findViewById(R.id.number);
        nextBtn =  myView.findViewById(R.id.next);
        prevBtn =  myView.findViewById(R.id.previous);
        recyclerView = myView.findViewById(R.id.v_recyclerView);
        context = getActivity();
        search = myView.findViewById(R.id.search);
        open_drawer = myView.findViewById(R.id.open_drawer);

        nextBtn.setVisibility(View.GONE);
        prevBtn.setVisibility(View.GONE);


        Btnfooter();
        loadList(0);
        CheckBtnBackGroud(0);

//        recycler();



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


        open_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });

        return myView;

    }

    private void recycler() {
        if (data.size()>0) {
            data.clear();
        }
        seeCustomerEditdata();
        customerDetailsAdpter = new ManageCustomerDetailsAdapter(data,context,this::recycler,this);
        customerDetailsAdpter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(customerDetailsAdpter);

    }



    @Override
    public void recyclerViewListClicked(View v, Object items, int position){

    }

    private void filter(String text) {
        ArrayList<ManageCustomerDetailsModel> filteredList = new ArrayList<>();

        for (ManageCustomerDetailsModel item : data) {
            if (item.getFirstName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getLastName().toLowerCase().contains(text.toLowerCase())||
                    item.getMobileNumber().toLowerCase().contains(text.toLowerCase())||
                    item.getCustomerId().toLowerCase().contains(text.toLowerCase()) ||
                    item.getEmailAddress().toLowerCase().contains(text.toLowerCase()) ) {

                filteredList.add(item);
            }
        }
        try {
            customerDetailsAdpter.filterList(filteredList);
        }catch (NullPointerException e) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    private void Btnfooter() {
        int val = TOTAL_NUM_ITEMS%ITEMS_PER_PAGE;
        val = val==0 ? 0:1;


        //val = val==0?0:1;
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
    /**
     * Method for Checking Button Backgrounds
     */
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
        ArrayList<ManageCustomerDetailsModel> sort = new ArrayList<>();

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

        customerDetailsAdpter = new ManageCustomerDetailsAdapter(sort,context, this::Btnfooter,this);
        customerDetailsAdpter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(customerDetailsAdpter);

    }

    private void seeCustomerEditdata() {
        try {
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getContext().getPackageName() + "/newinventory.db", null, 0);
            mydatabase.beginTransaction();
            Cursor cursor = mydatabase.rawQuery("SELECT * from tbl_custuser", null);
            mydatabase.endTransaction();
            jsonArray = cur2Json(cursor);
            Log.e("customer data::", "customer data " + jsonArray);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        recycler();
       customerDetailsAdpter.notifyDataSetChanged();
    }


    public void setDataAdptr(JSONArray jsonArray) {
        searchData=new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                manageCustomerDetailsModel = new ManageCustomerDetailsModel();
                manageCustomerDetailsModel.setSrno(String.valueOf(i));
                manageCustomerDetailsModel.setCustomerId(jsonobject.getString("custUser_ID"));
                manageCustomerDetailsModel.setFirstName(jsonobject.getString("firstName"));
                manageCustomerDetailsModel.setLastName(jsonobject.getString("lastName"));
                manageCustomerDetailsModel.setMobileNumber(jsonobject.getString("mobile"));
                manageCustomerDetailsModel.setGednder(jsonobject.getString("gender"));
                manageCustomerDetailsModel.setEmailAddress(jsonobject.getString("email"));
                data.add(manageCustomerDetailsModel);
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
                        Log.e("parveen112", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;

    }

    @Override
    public void updateCustomerAfterDelete() {
        recycler();

    }
}
