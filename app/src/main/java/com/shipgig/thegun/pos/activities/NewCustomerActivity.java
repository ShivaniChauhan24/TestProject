package com.shipgig.thegun.pos.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.adapter.NewCustomerAdapter;
import com.shipgig.thegun.pos.model.NewCustomerModel;
import com.shipgig.thegun.pos.utilities.Sharepreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class NewCustomerActivity extends Activity implements View.OnClickListener {

    private List<NewCustomerModel> customerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewCustomerAdapter newCustomerAdapter;
    ImageView backarrow;
    Toolbar toolbar;
    private JSONArray jsonArray;
     Context context;
     int storeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_new_customer);
        storeID = Sharepreference.getSharedPreferenceInt(NewCustomerActivity.this,"storeID",0);

        recyclerView =  findViewById(R.id.recyclerview);
        backarrow =  findViewById(R.id.backarrow);
        toolbar =  findViewById(R.id.toolbar);
        backarrow.setOnClickListener(this);
        toolbar.setTitle("New Customer");
        context = this;
        seeCustomerdata();
        recycler();




    }
    @Override
    protected void onRestart() {
        super.onRestart();
        customerList.clear();
        seeCustomerdata();
        recycler();
        Log.d("thegun","onrestart");
    }

    private void recycler() {
        newCustomerAdapter = new NewCustomerAdapter(customerList, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(newCustomerAdapter);
    }

    @SuppressLint("WrongConstant")
    private void seeCustomerdata() {
        try {
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/newinventory.db", null, 0);
            mydatabase.beginTransaction();
            Cursor cursor = mydatabase.rawQuery("SELECT custUser_ID, firstName,lastName,gender, email, mobile, createdOn from tbl_custuser where store_ID=" +storeID+" order by createdOn desc", null);
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
                NewCustomerModel newCustomerModel = new NewCustomerModel();
                newCustomerModel.setPersonid(jsonobject.getString("custUser_ID"));
                newCustomerModel.setName(jsonobject.getString("firstName"));
                newCustomerModel.setEmail(jsonobject.getString("email"));
                newCustomerModel.setMobile(jsonobject.getString("mobile"));
                newCustomerModel.setLname(jsonobject.getString("lastName"));
                newCustomerModel.setGender(jsonobject.getString("gender"));
                customerList.add(newCustomerModel);

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
                        Log.e("errorthegun", e.getMessage());
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backarrow:
                finish();
                break;

                default:
        }
    }
}
