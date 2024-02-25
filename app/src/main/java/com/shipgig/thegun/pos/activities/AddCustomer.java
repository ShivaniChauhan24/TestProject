package com.shipgig.thegun.pos.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.shipgig.thegun.pos.MainActivity;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.AddCustomerDetails;
import com.shipgig.thegun.pos.utilities.Sharepreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public class AddCustomer extends Activity implements View.OnClickListener {
    ImageView backarrow;
    EditText eTfirstname, eTlastname, eTphone, eTemail;
    String firstname, lasname, phone, email;
    TextView createcustomer;
    Spinner spinnerCustom;
    String item,gender;
    public JSONArray jsonArray = new JSONArray();
    SQLiteDatabase mydatabase;
    RadioGroup rg_Select_Gender;
    LinearLayout main_container;
    RadioButton rb_male,rb_female;
    String check;
    String creatOn;

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String EMAIL_MSG = "invalid email";
    private static final String PHONE_REGEX = "^[0-9]{10}$";
    private static final String PHONE_MSG = "invalid number";

    private String blockCharacterSet = "~#^|$%&*!:'@,./?+=-_)({}[]\";";

    int storeID,systemID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.add_new_customer);

        this.getWindow().setGravity(Gravity.CENTER);
        this.setFinishOnTouchOutside(true);

        main_container = findViewById(R.id.main_container);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);

        storeID = Sharepreference.getSharedPreferenceInt(AddCustomer.this,"storeID",0);
        systemID = Sharepreference.getSharedPreferenceInt(AddCustomer.this,"user_system",0);


        init();
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = formatter.format(todayDate);
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        creatOn = currentDate + " " + currentTime;

    }

    private void init() {
        eTfirstname =  findViewById(R.id.firstname);
        eTfirstname.setFilters(new InputFilter[] { filter });
        eTlastname =  findViewById(R.id.lastname);
        eTlastname.setFilters(new InputFilter[] { filter });
        eTphone =  findViewById(R.id.phone);
        eTemail =  findViewById(R.id.email);
        rg_Select_Gender =  findViewById(R.id.rg_Select_Gender);
        createcustomer =  findViewById(R.id.createcustomer);
        backarrow =  findViewById(R.id.backarrow);
        backarrow.setOnClickListener(this);
        createcustomer.setOnClickListener(this);



        rg_Select_Gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.rb_male) {
                    gender = "Male";
                } else if (i==R.id.rb_female) {
                    gender = "Female";
                }else {
                    Toast.makeText(AddCustomer.this, "Please select gender*", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backarrow:
                hideKeyboard(this);
                finish();
                break;

            case R.id.createcustomer:
                isValidate();

                break;

            case R.id.cancel:
                break;
        }
    }


    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }

    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        editText.setError(null);
        if ( required && !hasText(editText) ) return false;

        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        }

        return true;
    }

    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        if (text.length() == 0) {
            editText.setError("Error");
            return false;
        }

        return true;
    }


    @SuppressLint("WrongConstant")
    private void isValidate() {
        firstname = eTfirstname.getText().toString().trim();
        lasname = eTlastname.getText().toString().trim();
        phone = eTphone.getText().toString().trim();
        email = eTemail.getText().toString().trim();


        int selectedId = rg_Select_Gender.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);

        if (firstname.equals("")) {
            eTfirstname.setError("First Name is required.");
        }
        else if (selectedId <= 0){
            rb_female.setError("Please select gender");
        }
        else {
            if (lasname.equalsIgnoreCase("")){
                lasname = "NA";
            }
            if (phone.equalsIgnoreCase("")){
                phone = "NA";
            }
            if (email.equalsIgnoreCase("")){
                email = "NA";
            }else if (!validateUsername(email)){
                Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
                return;
            }
            insertCustomerdata();
        }
    }


    private boolean validateUsername(String email) {

        if (email.length() < 4 || email.length() > 30) {
            eTemail.setError("Email Must consist of 4 to 30 characters");
            return false;
        } else if (!email.matches("^[A-za-z0-9.@]+")) {
            eTemail.setError("Only . and @ characters allowed");
            return false;
        } else if (!email.contains("@") || !email.contains(".")) {
            eTemail.setError("Email must contain @ and .");
            return false;
        }
        return true;
    }

    public void setDataAdptr(JSONArray jsonArray) {
        List<AddCustomerDetails> data = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                Toast.makeText(AddCustomer.this, "data value of database::" + jsonobject.toString(), Toast.LENGTH_SHORT).show();
                AddCustomerDetails products = new AddCustomerDetails();
                products.invoice_ID = jsonobject.getString("invoice_ID");
                products.store_ID = jsonobject.getString("store_ID");
                products.transaction_ID = jsonobject.getString("transaction_ID");
                data.add(products);
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return true;
    }


    public boolean insertCustomerdata() {

        mydatabase   = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/newinventory.db", null, 0);
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName", firstname.trim());
        contentValues.put("middleName", "default");
        contentValues.put("lastName", lasname.trim());
        contentValues.put("email", email.trim());
        contentValues.put("mobile", phone.trim());
        contentValues.put("phone", "2949400");
        contentValues.put("DOB", "2018-01-13 18:08:00");
        contentValues.put("gender", gender.trim());
        contentValues.put("store_ID", storeID);
        contentValues.put("registered_store", "3232");
        contentValues.put("emailPromoEnabled", "111");
        contentValues.put("createdOn", creatOn);
        contentValues.put("modifiedOn", "2018-11-11 18:09:00");
        contentValues.put("deletedOn", "2018-11-11 18:09:00");
        contentValues.put("isDeleted", "1");
        contentValues.put("dateOfLatestActivity", "2018-01-13 18:08:00");
        contentValues.put("deletedBy", "1");
        contentValues.put("sysUser_ID", systemID);
        contentValues.put("modifiedBy", "12");
        long result = mydatabase.insertOrThrow("tbl_custuser", null, contentValues);
        Log.d("customerInsertthegun", "value" + result);
        Toast.makeText(this,"Record inserted successfully",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddCustomer.this, MainActivity.class);
        startActivity(intent);
        if (result == -1)
            return false;
        else{
            return true;
        }
    }


    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

}
