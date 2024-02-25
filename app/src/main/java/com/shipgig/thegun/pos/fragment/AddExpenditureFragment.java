package com.shipgig.thegun.pos.fragment;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.shipgig.thegun.pos.MainActivity;
import com.shipgig.thegun.pos.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.shipgig.thegun.pos.MainActivity.drawer;


public class AddExpenditureFragment extends Fragment implements View.OnClickListener {

    View myView;
    Context context;
    EditText etdescription, etamount;
    String description, amount;
    Button save;
    String expenseType;
    RadioGroup rg1,rg2;
    private SQLiteDatabase mydatabase;
    ImageView open_drawer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.add_expenditure, container, false);
        context = getActivity();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        myView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.
                            INPUT_METHOD_SERVICE);
                    if (getActivity().getCurrentFocus() != null) {
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                }
                return true;
            }
        });

        init();

        open_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(Gravity.START);
            }
        });

        return myView;
    }


    private void init() {
        save = myView.findViewById(R.id.save);
        etdescription = myView.findViewById(R.id.etdescription);
        etamount = myView.findViewById(R.id.etamount);
        open_drawer = myView.findViewById(R.id.open_drawer);
        save.setOnClickListener(this);
        rg1 = myView.findViewById(R.id.radioGroup1);
        rg2 = myView.findViewById(R.id.radioGroup2);
        rg1.clearCheck();
        rg2.clearCheck();
        rg1.setOnCheckedChangeListener(listener1);
        rg2.setOnCheckedChangeListener(listener2);

    }

    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
          if (checkedId != -1) {
                rg2.setOnCheckedChangeListener(null);
                rg2.clearCheck();
                rg2.setOnCheckedChangeListener(listener2);
                Log.e("XXX2", String.valueOf(checkedId));
            }
         if(checkedId == R.id.inventory_update) {
             expenseType = "Inventory Update Expense";
         } else if (checkedId == R.id.labercharge) {
             expenseType = "Labour Charges";
         } else if (checkedId == R.id.transactioncharge) {
             expenseType = "Transportation Charges";
         } else if (checkedId == R.id.rentcharge) {
             expenseType = "Rental Charges";
         } else if (checkedId == R.id.electricitycharge) {
             expenseType = "Electricity Charges";
         } else if (checkedId == R.id.watercharge) {
             expenseType = "Water Charges";
         } else if (checkedId == R.id.fuelcharge) {
             expenseType = "Fuel Charges";
         }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
           if (checkedId != -1) {
                rg1.setOnCheckedChangeListener(null);
                rg1.clearCheck();
                rg1.setOnCheckedChangeListener(listener1);
                Log.e("XXX21", String.valueOf(checkedId));
            }

            if(checkedId == R.id.donationcharge) {
                expenseType = "Donation Charges";
            } else if (checkedId == R.id.thirdpartypayment) {
                expenseType = "Third Party Payments";
            } else if (checkedId == R.id.telephonecharge) {
                expenseType = "Telephone Charges";
            } else if (checkedId == R.id.tax) {
                expenseType = "Tax";
            } else if (checkedId == R.id.insurancecharge) {
                expenseType = "Insuarance Charge";
            } else if (checkedId == R.id.shrinkcharge) {
                expenseType = "Inventory Damage/Shrink Charge";
            } else if (checkedId == R.id.carrybagcharge) {
                expenseType = "Carry Bags/Boxes";
            } else if (checkedId == R.id.otherscharge) {
                expenseType = "others";
            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                invalidate();
                break;



                default:

        }


    }

    private void invalidate() {
        description = etdescription.getText().toString().trim();
        amount = etamount.getText().toString().trim();

        if (TextUtils.isEmpty(description)) {
            etdescription.setError("*");
        }
        else if (TextUtils.isEmpty(amount)) {
            etamount.setError("*");
        } else {
            insertCustomerdata();
            Toast.makeText(context, "Save Successfully", Toast.LENGTH_SHORT).show();
            rg1.clearCheck();
            rg2.clearCheck();
            etdescription.setText("");
            etamount.setText("");
        }
    }
    int sysUser = 100;

    public boolean insertCustomerdata() {
        description = etdescription.getText().toString().trim();
        amount = etamount.getText().toString().trim();
        mydatabase   = SQLiteDatabase.openDatabase("/data/data/" + getActivity().getPackageName() + "/newinventory.db", null, 0);
        ContentValues contentValues = new ContentValues();
        contentValues.put("expenseType", expenseType);
        contentValues.put("expense_Description",description);
        contentValues.put("amount", amount);
        contentValues.put("store_ID", "1111");
        contentValues.put("sysUser_ID", sysUser++);
        contentValues.put("createdOn",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //contentValues.put("createdOn",  "2018-01-14 12:15:01");
        long result = mydatabase.insertOrThrow("tbl_expenses", null, contentValues);
        Log.d("expenditureInsertthegun", "value" + result);
        Toast.makeText(getContext(),"Saved successfully",Toast.LENGTH_SHORT).show();
        if (result == -1)
            return false;
        else{

            return true;
        }

    }
}
