package com.shipgig.thegun.pos.activities;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.shipgig.thegun.pos.R;

public class EditCustomer extends Activity implements View.OnClickListener{

    ImageView backarrow;
    EditText eTfirstname,eTlastname,eTphone,eTemail;
    String firstname,lasname,phone,email;
    TextView createcustomer,cancel;
    String item,id;
    String oFirstName,olastName,oPhone,oMail,oSex;

    String check;

    RadioGroup editCustomer_radiogroup;
    RadioButton rb_male, rb_female;

    private String blockCharacterSet = "~#^|$%&*!:'@,./?+=-_)({}[]\";";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_customer);

        editCustomer_radiogroup = findViewById(R.id.editCustomer_radiogroup);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);

        init();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        Toolbar toolbar =  findViewById(R.id.toolbar);
        eTfirstname =  findViewById(R.id.firstname);
        eTfirstname.setFilters(new InputFilter[] { filter });
        eTlastname =  findViewById(R.id.lastname);
        eTlastname.setFilters(new InputFilter[] { filter });
        eTphone =  findViewById(R.id.phone);
        eTemail = findViewById(R.id.email);
        cancel = findViewById(R.id.cancel);
        createcustomer = findViewById(R.id.createcustomer);
        toolbar.setTitle("Edit Customer");
        backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(this);
        createcustomer.setOnClickListener(this);


        Intent intent = getIntent();
        id= intent.getStringExtra("id");
        oFirstName= intent.getStringExtra("firstName");
        oPhone= intent.getStringExtra("phonenumber");
        olastName= intent.getStringExtra("lastname");
        oMail= intent.getStringExtra("email");
        item= intent.getStringExtra("sex");


        if (oPhone.equals("NA") || olastName.equals("NA")||oMail.equals("NA")){
            eTphone.setHint(oPhone);
            eTlastname.setHint(olastName);
            eTemail.setHint(oMail);
        }
        else {
            eTphone.setText(oPhone);
            eTlastname.setText(olastName);
            eTemail.setText(oMail);
        }
        eTfirstname.setText(oFirstName);


        if (item.equals("Male")){
            rb_male.setChecked(true);
        }
        else  {
            rb_female.setChecked(true);
        }

        editCustomer_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_male){
                    item = "Male";
                }
                else {
                    item = "Female";
                }
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backarrow:
                hideKeyboard(this);
                finish();
                break;

            case R.id.createcustomer:
                isValidate();
                break;
        }
    }

    private void isValidate() {


        firstname = eTfirstname.getText().toString().trim();
        lasname = eTlastname.getText().toString().trim();
        phone = eTphone.getText().toString().trim();
        email = eTemail.getText().toString().trim();

        int selectedId = editCustomer_radiogroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);

        if(firstname.equals("")){
            eTfirstname.setError("First Name is required.");
        }
        else {
            updateCustomer();
            Toast.makeText(this,"Update Successfully!",Toast.LENGTH_SHORT).show();
            finish();


        }
    }

    private boolean validateEmail() {

        check = eTemail.getText().toString();

        if (check.length() < 4 || check.length() > 30) {
            eTemail.setError("Email Must consist of 4 to 30 characters");
            return false;
        } else if (!check.matches("^[A-za-z0-9.@]+")) {
            eTemail.setError("Only . and @ characters allowed");
            return false;
        } else if (!check.contains("@") || !check.contains(".")) {
            eTemail.setError("Email must contain @ and .");
            return false;
        }
        return true;
    }

    private boolean updateCustomer() {
        SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/newinventory.db", null, 0);
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName", firstname);

        if (lasname.equals("") && email.equals("")&& phone.equals("")){
            contentValues.put("lastName", "NA");
        }
        else {
            contentValues.put("lastName", lasname);
        }

        contentValues.put("email", email);
        contentValues.put("mobile", phone);
        contentValues.put("gender", item);
        long i = mydatabase.update("tbl_custuser", contentValues, "custUser_ID"+"="+id, null);
        Log.d("editThegun", String.valueOf(i));

        if (i == -1)
            return false;
        else{
            return true;
        }
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
