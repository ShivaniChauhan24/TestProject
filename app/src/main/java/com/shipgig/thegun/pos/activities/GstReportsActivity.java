package com.shipgig.thegun.pos.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.adapter.CustomSpinnerAdapter;
import com.shipgig.thegun.pos.adapter.GstReportsAdapter;
import com.shipgig.thegun.pos.model.GstReportsModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class GstReportsActivity extends Activity implements View.OnClickListener{


    ArrayList<GstReportsModel> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    Context context;
    private JSONArray jsonArray = new JSONArray();
    GstReportsAdapter gstReportsAdapter;
    ImageView backarrow;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.gst_reports);

        init();
//        initCustomSpinner();
        seeCustomerdata();
        context = this;

        gstReportsAdapter = new GstReportsAdapter(arrayList, context);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(gstReportsAdapter);

        try {
            TOTAL_NUM_ITEMS = jsonArray.length();
        } catch (NullPointerException e) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        }


        Btnfooter();
        loadList(0);
        CheckBtnBackGroud(0);



    }
    private void toggleButtons() {
        if (currentPage == totalPages) {
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(true);
        } else if (currentPage == 0) {
            prevBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        } else if (currentPage >= 1 && currentPage <= totalPages) {
            nextBtn.setEnabled(true);
            prevBtn.setEnabled(true);
        }
    }

    private void init() {
        pageNumber = findViewById(R.id.number);
        nextBtn =  findViewById(R.id.next);
        prevBtn =  findViewById(R.id.previous);
        backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(this);
        recyclerView = findViewById(R.id.recylerview);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backarrow:
                finish();
                break;
        }
    }

    private void initCustomSpinner() {

        Spinner spinnerCustom=  findViewById(R.id.transactionReportsSpinner);
        ArrayList<String> gender = new ArrayList<String>();
        gender.add("Choose ReportsModel");
        gender.add("Daily");
        gender.add("Weekly");
        gender.add("monthly");
        gender.add("Early");
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(this,gender);
        spinnerCustom.setAdapter(customSpinnerAdapter);
        spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                if(position > 0){
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + item, Toast.LENGTH_SHORT)
                            .show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @SuppressLint("WrongConstant")
    private void seeCustomerdata() {
        try {
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/newinventory.db", null, 0);
            mydatabase.beginTransaction();
            Cursor cursor = mydatabase.rawQuery("SELECT tp.HSN_ID, tg.Item_Desc, tg.Item, tp.gst_ID as UQC,count(tp.HSN_ID) as TotalQuantity, SUM(tp.unitPrice) as TotalValue , SUM((tp.unitPrice*tg.igst)/100)  as IGST, SUM((tp.unitPrice*tg.igst)/100)/2  as CGST, SUM((tp.unitPrice*tg.igst)/100)/2  as SGST, 0 as Cess from tbl_transaction tt JOIN tbl_invoice ti on tt.transaction_ID = ti.transaction_ID JOIN tbl_product tp on tp.product_ID = ti.product_ID and tp.unitPrice is NOT NULL JOIN tbl_gst tg on tp.gst_ID = tg.gst_ID WHERE tt.isDeleted = 0 and tt.store_ID = ?  and tt.transactionType = \"Sales\"  and ti.isReturned = 0 and tt.isVoid = 0 and tt.createdOn BETWEEN datetime('now','-1 month') AND datetime('now','localtime') GROUP BY tp.HSN_ID", null);
            mydatabase.endTransaction();
            jsonArray = cur2Json(cursor);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setDataAdptr(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                GstReportsModel gstReportsModel = new GstReportsModel();
                gstReportsModel.setSrno(String.valueOf(i));//jsonobject.getString("gst_ID");
                gstReportsModel.setHsnno(jsonobject.getString("HSN_ID"));
                gstReportsModel.setDescription(jsonobject.getString("Item_Desc"));
                gstReportsModel.setUqc(jsonobject.getString("UQC"));
                gstReportsModel.setTotalQuantiy(jsonobject.getString("TotalQuantity"));
                gstReportsModel.setTotalValue(jsonobject.getString("TotalValue"));
                gstReportsModel.setIgst(jsonobject.getString("IGST"));
                gstReportsModel.setCgst(jsonobject.getString("CGST"));
                gstReportsModel.setSgst(jsonobject.getString("SGST"));
                gstReportsModel.setCess(jsonobject.getString("Cess"));

                arrayList.add(gstReportsModel);

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
                        Log.e("thegun", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;

    }

    int startItem;
    int numOfData;
    public ArrayList<GstReportsModel> generatePage(int currentPage, JSONArray jsonArray)
    {
        startItem=currentPage*ITEMS_PER_PAGE;//+1;
        numOfData=ITEMS_PER_PAGE;
        arrayList= new ArrayList<>();
        if (currentPage==LAST_PAGE && ITEMS_REMAINING>0)
        {
            for (int i=startItem;i<startItem+ITEMS_REMAINING;i++)
            {
                JSONObject jsonobject = null;
                try {
                    jsonobject = jsonArray.getJSONObject(i);
                    GstReportsModel gstReportsModel = new GstReportsModel();
                    gstReportsModel.setSrno(String.valueOf(i));//jsonobject.getString("gst_ID");
                    gstReportsModel.setHsnno(jsonobject.getString("HSN_ID"));
                    gstReportsModel.setDescription(jsonobject.getString("Item_Desc"));
                    gstReportsModel.setUqc(jsonobject.getString("UQC"));
                    gstReportsModel.setTotalQuantiy(jsonobject.getString("TotalQuantity"));
                    gstReportsModel.setTotalValue(jsonobject.getString("TotalValue"));
                    gstReportsModel.setIgst(jsonobject.getString("IGST"));
                    gstReportsModel.setCgst(jsonobject.getString("CGST"));
                    gstReportsModel.setSgst(jsonobject.getString("SGST"));
                    gstReportsModel.setCess(jsonobject.getString("Cess"));

                    arrayList.add(gstReportsModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }else
        {
            for (int i=startItem;i<startItem+numOfData;i++)
            {
                JSONObject jsonobject = null;
                try {
                    jsonobject = jsonArray.getJSONObject(i);
                    GstReportsModel gstReportsModel = new GstReportsModel();
                    gstReportsModel.setSrno(String.valueOf(i));//jsonobject.getString("gst_ID");
                    gstReportsModel.setHsnno(jsonobject.getString("HSN_ID"));
                    gstReportsModel.setDescription(jsonobject.getString("Item_Desc"));
                    gstReportsModel.setUqc(jsonobject.getString("UQC"));
                    gstReportsModel.setTotalQuantiy(jsonobject.getString("TotalQuantity"));
                    gstReportsModel.setTotalValue(jsonobject.getString("TotalValue"));
                    gstReportsModel.setIgst(jsonobject.getString("IGST"));
                    gstReportsModel.setCgst(jsonobject.getString("CGST"));
                    gstReportsModel.setSgst(jsonobject.getString("SGST"));
                    gstReportsModel.setCess(jsonobject.getString("Cess"));

                    arrayList.add(gstReportsModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return arrayList;
    }


    private void Btnfooter() {
        int val = TOTAL_NUM_ITEMS%ITEMS_PER_PAGE;
        val = val==0 ? 0:1;

        noOfBtns=TOTAL_NUM_ITEMS/ITEMS_PER_PAGE+val;

        LinearLayout ll = (LinearLayout)findViewById(R.id.btnLay);

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
        ArrayList<GstReportsModel> sort = new ArrayList<>();

        int start = number * ITEMS_PER_PAGE;
        for(int i=start;i<(start)+ITEMS_PER_PAGE;i++)
        {
            if(i<arrayList.size())
            {
                sort.add(arrayList.get(i));

            }
            else
            {
                break;
            }
        }
        gstReportsAdapter = new GstReportsAdapter(sort,context);
        gstReportsAdapter.notifyDataSetChanged();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(gstReportsAdapter);

    }



}
