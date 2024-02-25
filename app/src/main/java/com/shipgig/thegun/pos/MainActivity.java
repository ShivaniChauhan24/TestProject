package com.shipgig.thegun.pos;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.shipgig.thegun.pos.activities.LoginActivity;
import com.shipgig.thegun.pos.fragment.AddExpenditureFragment;
import com.shipgig.thegun.pos.fragment.AddInventoryFragment;
import com.shipgig.thegun.pos.fragment.AllReportsFragment;
import com.shipgig.thegun.pos.fragment.ChangePasswordFragment;
import com.shipgig.thegun.pos.fragment.DailySalesReportsFragment;
import com.shipgig.thegun.pos.fragment.DashBoardFragment;
import com.shipgig.thegun.pos.fragment.ManageCustomerFragment;
import com.shipgig.thegun.pos.fragment.ManageInventoryFragment;
import com.shipgig.thegun.pos.fragment.NewHomeFragment;
import com.shipgig.thegun.pos.model.Products;
import com.shipgig.thegun.pos.utilities.Sharepreference;
import com.shipgig.thegun.pos.utilities.SQLiteSync;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemSelectedListener {

    private final int MY_PERMISSIONS_REQUEST_CODE = 1;
    Context context;
    int check = 0;
    TextView VersionCode;
    public static int navItemIndex = 0;
    private static final String TAG_HOME = "home";
    public static String CURRENT_TAG = TAG_HOME;
    Fragment fragment = null;
    MenuItem mSearchItem;
    SearchView.SearchAutoComplete mSearchAutoComplete;
    public JSONArray jsonArray = new JSONArray();
    boolean doubleBackToExitPressedOnce = false;
    ProgressDialog dialog;
    NavigationView navigationView;
    public static DrawerLayout drawer;

    String headerName,headerEmail;
    int storeID,systemID;

    String openIn = "";

    public FragmentRefreshListener getFragmentRefreshListener() {

        return fragmentRefreshListener;
    }
    private FragmentRefreshListener fragmentRefreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if (checkPermissions()) {
            getSubscriberId();
        } else {
            setPermissions();
        }



        headerName = Sharepreference.getSharedPreferenceString(MainActivity.this,"name","Not Available");
        headerEmail = Sharepreference.getSharedPreferenceString(MainActivity.this,"email","Not Available");
        storeID = Sharepreference.getSharedPreferenceInt(MainActivity.this,"storeID",0);
        systemID = Sharepreference.getSharedPreferenceInt(MainActivity.this,"user_system",0);


        context = this;
        init();
        getAppVersion();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        openIn = getIntent().getStringExtra("openInventory");

        if (savedInstanceState == null) {
            try{
                if (openIn != null){
                    if (openIn.equalsIgnoreCase("inventory")){
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_HOME;
                        displaySelectedScreen(R.id.nav_manage_inventory);
                    }else if (openIn.equalsIgnoreCase("allreport")){
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_HOME;
                        displaySelectedScreen(R.id.nav_allreports);
                    }


                }
                else {
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_HOME;
                    displaySelectedScreen(R.id.nav_home);
                }

            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }

        View headerView = navigationView.getHeaderView(0);
        TextView header_name  = headerView.findViewById(R.id.header_name);
        TextView header_email  = headerView.findViewById(R.id.header_email);

        if (headerName != null && !headerName.equalsIgnoreCase("")
                || headerEmail != null && !headerEmail.equalsIgnoreCase(""))
        {
            header_name.setText(headerName);
            header_email.setText(headerEmail);
        }
        else {
            header_name.setText("Not Available");
            header_email.setText("Not Available");
        }


    }

    private void init() {
        VersionCode = findViewById(R.id.VersionCode);
    }

    @SuppressLint("SetTextI18n")
    public void getAppVersion() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            VersionCode.setText("v " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (++check > 1) {
            String str = (String) adapterView.getItemAtPosition(i);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
    }

    public interface FragmentRefreshListener {
        void onRefresh();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mSearchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        mSearchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        mSearchAutoComplete.setTextColor(R.color.login_edittext_color);
        mSearchAutoComplete.setHintTextColor(R.color.login_edittext_color);
        mSearchAutoComplete.setDropDownBackgroundResource(R.drawable.background_white);

        searchView.setBackgroundColor(Color.WHITE);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        List<Products> data = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                Products products = new Products();
                products.productName = jsonobject.getString("productName");
                products.price = jsonobject.getString("unitPrice");
                products.discount = jsonobject.getString("discount");
                products.amount = jsonobject.getString("unitPrice");
                products.quantity = jsonobject.getString("totalstock");
                products.imagePath = jsonobject.getString("image_path");
                data.add(products);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        mSearchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    filter(editable.toString());
                }
            }
        });
        ImageView searchClose = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_black_close_icons);

        return true;
    }

    private void displaySelectedScreen(int itemId) {

        switch (itemId) {
            case R.id.nav_home:
                fragment=new NewHomeFragment();
                break;
            case R.id.nav_sync:
                syncData();
                break;
            case R.id.nav_dashboard:
                fragment = new DashBoardFragment();
                break;
            case R.id.nav_allreports:
                fragment = new AllReportsFragment();
                break;
            case R.id.nav_manage_customers:
                fragment = new ManageCustomerFragment();
                break;
            case R.id.nav_add_inventory:
                fragment = new AddInventoryFragment();
                break;
            case R.id.nav_manage_inventory:
                fragment = new ManageInventoryFragment();
                break;
            case R.id.nav_daily_sales_reports:
                fragment = new DailySalesReportsFragment();
                break;
            case R.id.nav_add_expenditure:
                fragment = new AddExpenditureFragment();
                break;
            case R.id.nav_change_password:
                fragment = new ChangePasswordFragment();
                break;
            case R.id.nav_initialize:
                initializePOS();
                break;
            case R.id.nav_logout:
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom);
                dialog.setCancelable(false);
                Button yes_logout = dialog.findViewById(R.id.yes_logout);
                Button no_logout = dialog.findViewById(R.id.no_logout);
                yes_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"You logout successfully",Toast.LENGTH_SHORT).show();
                        Sharepreference.logoutFromGoogle(MainActivity.this);
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                no_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    public void initializePOS() {
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Data Loading");
        dialog.setMessage("Please wait while data loading...");
        dialog.setCancelable(false);
        dialog.show();

        String sqlite_sync_url = "http://192.168.1.112:9999/SqliteSync_315/API3";
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sqlite_sync_url", sqlite_sync_url);

        SQLiteSync sqLite_sync = new SQLiteSync("/data/data/" + getPackageName() + "/newinventory.db",
                sqlite_sync_url);

        sqLite_sync.initializeSubscriber(getSubscriberId(), new SQLiteSync.SQLiteSyncCallback() {
            @Override
            public void onSuccess() {
//                        hideProgressBar();
//                button.setEnabled(true);
                Log.e("Initialization", "Initialization finished successfully");
                showMessage("Initialization finished successfully");
                dialog.dismiss();
//                syncData();
            }

            @Override
            public void onError(Exception error) {
//                        hideProgressBar();
//                button.setEnabled(true);
                error.printStackTrace();
                showMessage("Initialization finished with error: \n" + error.getMessage());
                dialog.dismiss();
                Log.e("error sql", error.getMessage());
            }
        });
    }

    public void syncData() {
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Data Uploading");
        dialog.setMessage("Please wait while data uploading...");
        dialog.setCancelable(false);
        dialog.show();

        String sqlite_sync_url = "http://192.168.1.112:9999/SqliteSync_315/API3";
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sqlite_sync_url", sqlite_sync_url);

        SQLiteSync sqLite_sync = new SQLiteSync("/data/data/" + getPackageName() + "/newinventory.db",
                sqlite_sync_url);

        sqLite_sync.synchronizeSubscriber(getSubscriberId(), new SQLiteSync.SQLiteSyncCallback() {
            @Override
            public void onSuccess() {

                showMessage("Data synchronization finished successfully");
                getData("All  Categories");
                displaySelectedScreen(R.id.nav_home);
                dialog.dismiss();

            }

            @Override
            public void onError(Exception error) {
                dialog.dismiss();
                Log.d("datasyncerrorthegun", error.getMessage());
                showMessage("Data synchronization finished with error: \n" + error.getMessage());
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private String getSubscriberId() {
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        Log.d("simstatethegun",tmDevice+" "+tmSerial+" "+androidId);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    public void getData(String category) {

        try {
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/newinventory.db", null, 0);
            Cursor resultSet;
            Log.d("categorythegun",category);
            if (category.equals("All  Categories")) {
                 resultSet = mydatabase.rawQuery("SELECT G.gst_Cat_ID,P.productName,P.HSN_ID,P.product_ID,P.unitPrice,P.discount,P.totalstock,P.totalstock,P.category,P.image_path,P.category " +
                         "from tbl_product as P LEFT JOIN tbl_gst as G ON P.HSN_ID = G.HSN_NO where P.store_ID='"+storeID+"'", null);// before change here table name was tbl_product
            } else {
                 resultSet = mydatabase.rawQuery("SELECT G.gst_Cat_ID,P.productName,P.HSN_ID,P.discount,P.product_ID,P.unitPrice,P.totalstock,P.totalstock,P.category,P.image_path,P.category \" +\n" +
                         "\"from tbl_product as P LEFT JOIN tbl_gst as G ON P.HSN_ID = G.HSN_NO where P.category='" + category + "'" +"and P.store_ID="+storeID, null);// before change here table name was tbl_product
            }
            Log.d("parveen", resultSet.toString());

            jsonArray = cur2Json(resultSet);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            Toast.makeText(context, "Couldn't create database", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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
                        Log.e("parveen111", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;

    }

    public void setDataAdptr(JSONArray jsonArray) {
        List<Products> data = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                Products products = new Products();
                products.productName = jsonobject.getString("productName");
                products.price = jsonobject.getString("unitPrice");
                products.discount = jsonobject.getString("discount");
                products.amount = jsonobject.getString("unitPrice");
                products.totalQty = jsonobject.getString("totalstock");
                products.hsn_No = jsonobject.getString("HSN_ID");
                products.gst_Percent = jsonobject.getInt("gst_Cat_ID");
                products.imagePath = jsonobject.getString("image_path");
                data.add(products);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    private void filter(String text) {

        JSONArray jsonArrayNw = new JSONArray();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                if (jsonobject.getString("productName").toLowerCase().contains(text.toLowerCase())) {
                    jsonArrayNw.put(jsonobject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        jsonArray = jsonArrayNw;
        Log.d("fdsfsd", jsonArray.toString());
//        fragment = new HomeFragment();
        if (getFragmentRefreshListener() != null) {
            getFragmentRefreshListener().onRefresh();

        }
//        displaySelectedScreen(R.id.nav_home);

//        fragment=new HomeFragment();
//        if (fragment != null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, fragment);
//            ft.commit();
//        }
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSIONS_REQUEST_CODE) {
            return;
        }
        boolean isGranted = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }

        if (isGranted) {
            getSubscriberId();
        } else {
            Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();

        }
    }

    private void setPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_CODE);
    }

}
