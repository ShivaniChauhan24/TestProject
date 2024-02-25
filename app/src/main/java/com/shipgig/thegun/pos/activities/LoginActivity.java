package com.shipgig.thegun.pos.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.shipgig.thegun.pos.MainActivity;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.Data;
import com.shipgig.thegun.pos.model.LoginResponseModel;
import com.shipgig.thegun.pos.model.User;
import com.shipgig.thegun.pos.utilities.Sharepreference;
import com.shipgig.thegun.pos.utilities.RetrofitClient;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends Activity implements View.OnClickListener {

    boolean doubleBackToExitPressedOnce = false;
    TextView forgetpassowrd;
    Button login;
    EditText editTextUserId,editTextPassword;
    ImageView imageView;
    String password;
    String username;
    String encoded_user;
    String TAG = "TESTING_login";
    ConstraintLayout mainLayout;
    private ProgressDialog dialog;

    SQLiteDatabase mydatabase;

    public JSONArray jsonArray = new JSONArray();
    private static final int PERMISSION_REQUEST_CODE = 200;


    private int systemuserId,access_id;
    String categoryName,firstName,lastName,email,userName,mobile,name,codes,description,
            store,manager,sales,purchase,inventory,out_ofStock,inventoryReports,transactionReport,expiredReport,
    cashSummary,salesReport,customerwiseReport,userBilledReport,voidBillReport,returnItemReport,outOfStockReport
            ,aboutOutOfStockReport,profitReport,returnedCashReport,storewiseReport,allStoreReport,expenseReport
            ,managerWiseReport,sellUsingCardReport,gstReport,specialDiscountReport,billReport;
   int store_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_screen);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"roboto_regular.ttf",true);

        editTextUserId=findViewById(R.id.username);
        editTextPassword=findViewById(R.id.userpassword);
        mainLayout=findViewById(R.id.mainLayout);
        imageView= findViewById(R.id.image);
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setCancelable(false);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ||checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ||checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    ||checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                    ||checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                    ||checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                    ||checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                    ||checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                    ||checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                    ||checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.INTERNET}, PERMISSION_REQUEST_CODE);

            }
        }

        init();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = editTextUserId.getText().toString();
                password = editTextPassword.getText().toString();

                if (!isInternetOn()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Alert !")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage("Network Error, check your network connection.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
                else {
                    if (TextUtils.isEmpty(username) || username.equalsIgnoreCase("")){
                        Snackbar snackbar = Snackbar.make(mainLayout, "Please enter Email or Username", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                    else if (TextUtils.isEmpty(password)|| password.equalsIgnoreCase("")){
                        Snackbar snackbar = Snackbar.make(mainLayout, "Please enter password", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                    else {

                        JSONArray ja = new JSONArray();
                        JSONObject jo = new JSONObject();
                        try {
                            jo.put("p_username", username);
                            jo.put("p_password", password);
                            ja.put(jo);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String encode = String.valueOf(jo);
                        byte[] data;
                        try {
                            data = encode.getBytes("UTF-8");
                            String encoded_user = Base64.encodeToString(data, Base64.DEFAULT);
                            getUserData(encoded_user);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void getUserData(String encoded_user) {

        dialog.setTitle("Online Authenticating");
        dialog.setMessage("Please wait while authenticating....");
        dialog.show();

        Call<LoginResponseModel> call=RetrofitClient.getInstance().getApi().userLogin(encoded_user.trim());
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {

                if (response!= null){
                    dialog.dismiss();
                    String status = response.body().getStatus();
                    Log.e(TAG, "onResponse: "+status );
                    if (status.equals("Success")){
                        Data getdata = response.body().getData();
                        String result = getdata.getResult();
                        int code = getdata.getCode();
                        String token = getdata.getToken();
                        if (result.equals("login successful")){
                            User user = getdata.getUser();
                            categoryName = user.getCategoryName();
                            systemuserId = user.getSysUserID();
                            store_ID = user.getStoreID();
                            firstName = user.getFirstName();
                            lastName = user.getLastName();
                            email = user.getEmail();
                            userName = user.getUserName();
                            mobile = user.getMobile();
                            name = user.getName();
                            codes = user.getCode();
                            description = user.getDescription();
                            access_id = user.getAccessId();
                            store = user.getStore();
                            manager = user.getManager();
                            sales = user.getSales();
                            purchase = user.getPurchase();
                            inventory = user.getInventory();
                            out_ofStock = user.getOutOfStock();
                            transactionReport = user.getTransactionReport();
                            expiredReport = user.getExpiredReport();
                            cashSummary = user.getCashSummary();
                            salesReport = user.getSalesReport();
                            customerwiseReport = user.getCustomerwiseReport();
                            userBilledReport = user.getUserBilledReport();
                            billReport = user.getBillReport();
                            voidBillReport = user.getVoidBillReport();
                            returnItemReport = user.getReturnedItemReport();
                            outOfStockReport = user.getOutOfStockReport();
                            aboutOutOfStockReport = user.getAboutOutOfStockReport();
                            profitReport = user.getProfitReport();
                            returnedCashReport = user.getReturnedCashReport();
                            storewiseReport = user.getManagerWiseReport();
                            allStoreReport = user.getAllStoreReport();
                            expiredReport = user.getExpiredReport();
                            managerWiseReport = user.getManagerWiseReport();
                            sellUsingCardReport = user.getSellUsingCardReport();
                            gstReport = user.getGstReport();
                            specialDiscountReport = user.getSpecialDiscountReport();

                            //insert in user table
                            insertUserData();
                            String userName = firstName +" "+ lastName;

                            Sharepreference.setSharedPreferenceString(LoginActivity.this,"usertoken",token);
                            Sharepreference.setSharedPreferenceInt(LoginActivity.this,"user_system",systemuserId);
                            Sharepreference.setSharedPreferenceInt(LoginActivity.this,"storeID",store_ID);
                            Sharepreference.setSharedPreferenceString(LoginActivity.this,"name",userName);
                            Sharepreference.setSharedPreferenceString(LoginActivity.this,"email",email);
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                    }else
                        {
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "onResponse:exception "+t );

                String error = t.getMessage().toString();

                if (error.equalsIgnoreCase("java.net.NoRouteToHostException: No route to host")){
                    Snackbar snackbar = Snackbar.make(mainLayout, "Something went wrong", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else if (error.equalsIgnoreCase("java.net.ConnectException: Failed to connect to /192.168.1.96:3000")){
                    Snackbar snackbar = Snackbar.make(mainLayout, "No internet connection, Please connect", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else if (error.equalsIgnoreCase("failed to connect to /192.168.1.96 (port 3000) from /192.168.2.6 (port 49064) after 100000ms: isConnected failed: ETIMEDOUT (Connection timed out)")){

                    Snackbar snackbar = Snackbar.make(mainLayout, "Connection timeout, Please connect", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

                else {
                    Snackbar snackbar = Snackbar.make(mainLayout, "Invalid username & password", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }


            }
        });

    }

    private void init() {
        login =  findViewById(R.id.reset);
        forgetpassowrd =  findViewById(R.id.alreadyuser);
        forgetpassowrd.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.alreadyuser: {
                Intent intentforget = new Intent(this, ForgetPassword.class);
                startActivity(intentforget);
                finish();

            }
            break;

        }
    }



    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        assert connec != null;
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click Back Again to Exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    public boolean insertUserData(){

        try {
            mydatabase   = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/newinventory.db", null, 0);
            ContentValues contentValues = new ContentValues();
            contentValues.put("sysUser_ID", systemuserId);
            contentValues.put("store_ID", store_ID);
            contentValues.put("firstName", firstName);
            contentValues.put("middleName", "unnamed");
            contentValues.put("lastName", lastName);
            contentValues.put("email", email);
            contentValues.put("userName", userName);
            contentValues.put("password", password);
            contentValues.put("mobile", mobile);

            long result = mydatabase.insertOrThrow("tbl_sysuser", null, contentValues);
            if (result == -1){
                Toast.makeText(this, "Something Error", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "User Data Recorded", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

}

