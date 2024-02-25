package com.shipgig.thegun.pos.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.shipgig.thegun.pos.MainActivity;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.adapter.CustomListAdapter;
import com.shipgig.thegun.pos.adapter.CustomSpinnerAdapter;
import com.shipgig.thegun.pos.model.GSTModel;
import com.shipgig.thegun.pos.utilities.Sharepreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditInventoryActivity extends Activity implements View.OnClickListener {

    AlertDialog alertDialog;
    TextView chooseImage;
    String pictureImagePath = "",TAG = "AddServices", imgBase64="",item;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ImageView backarrow;
    Context context;
    AutoCompleteTextView etsearch;
    EditText etproductname,etbrandname,etdiscription,etconstraint,etsuplierid,etunitprice,etbarcodeid,
            etdiscount,etcostprice,ettotalstock;
    public static EditText etexpirydate;
    String search,brandName,discription,constraint,supplierId,unitPrice,barCodeId,gstId,discount,
            expiryDate,costPrice,totalStock,category,description,expiredate,hsn;
    TextView add,cancel,clear;
    private SQLiteDatabase mydatabase;
    String product_Name,check_to_edit,product_ID;
    private ImageView addInventoryImage;
    LinearLayout click_select;
    private int PICK_IMAGE_REQUEST = 1;
    String inventory_image_upload;
    private final int requestCode_CAMERA = 20;
    ImageView open_drawer;
//    TextView show_hsn_No;
//    ImageView edit_hsn_No;
    EditText select_hsn_no;
    ListView hsn_no_ListView;
    CustomListAdapter customListAdapter;
    String selected_HSN_No,dropDownItem;
    int SelectedgstID_from_Hsn;
    private JSONArray jsonArray = new JSONArray();
    private JSONArray jsonArrayProduct = new JSONArray();
    private ArrayList<GSTModel> gstModelArrayList= new ArrayList<>();
    private GSTModel gstModel;
    int storeID;

    private String blockCharacterSet = "~#^|$%&*!:'@,./?+=-_)({}[]\";";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.add_inventory_fragment);
        storeID = Sharepreference.getSharedPreferenceInt(EditInventoryActivity.this,"storeID",0);
        context = this;
        init();
        initCustomSpinner();
        getHSN_Number();
        String receive = getIntent().getStringExtra("fromAdapter");
        click_select = findViewById(R.id.click_select);
        open_drawer.setVisibility(View.GONE);
        ImageView back_arrow = findViewById(R.id.back_arrow);
        TextView headerTxt = findViewById(R.id.headerTxt);
        back_arrow.setVisibility(View.VISIBLE);
        headerTxt.setText("Edit Inventory");

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (receive.equals("adapter")){
            add.setText("Update");
        }


        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraDialog();
            }
        });

//        edit_hsn_No.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                show_hsn_No.setVisibility(View.GONE);
//                edit_hsn_No.setVisibility(View.GONE);
//                select_hsn_no.setVisibility(View.VISIBLE);
//            }
//        });

        select_hsn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog alertDialog = new AlertDialog.Builder(EditInventoryActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom_listviewdisplay, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Select HSN No.");
                hsn_no_ListView = (ListView) convertView.findViewById(R.id.common);
                customListAdapter = new CustomListAdapter(EditInventoryActivity.this, R.layout.hsn_no_listitem, gstModelArrayList);
                hsn_no_ListView.setAdapter(customListAdapter);
                hsn_no_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dropDownItem = gstModelArrayList.get(position).getHsn_no();
                        SelectedgstID_from_Hsn = gstModelArrayList.get(position).getGst_Cat_ID();
                        select_hsn_no.setText(dropDownItem);
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

            }
        });

    }


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);

    }

    private void init() {
        if (checkPermission()) {

        } else {
            requestPermission();
        }
        select_hsn_no= findViewById(R.id.select_hsn_no);
        open_drawer = findViewById(R.id.open_drawer);
        etsearch = findViewById(R.id.autoCompleteTextView);
//        show_hsn_No = findViewById(R.id.show_hsn_No);
//        edit_hsn_No = findViewById(R.id.edit_hsn_No);

        etproductname = findViewById(R.id.etproductname);
        etproductname.setFilters(new InputFilter[] { filter });

        etbrandname = findViewById(R.id.etbrandname);
        etbrandname.setFilters(new InputFilter[] { filter });

        etdiscription = findViewById(R.id.etdescription);
        etdiscription.setFilters(new InputFilter[] { filter });

        etconstraint = findViewById(R.id.etconstrains);
        etconstraint.setFilters(new InputFilter[] { filter });

        etsuplierid = findViewById(R.id.etsupplyid);
        etsuplierid.setFilters(new InputFilter[]{filter});

        etunitprice = findViewById(R.id.etunitprice);
        etunitprice.setFilters(new InputFilter[]{filter});

        etbarcodeid = findViewById(R.id.etbarcodeid);
        etbarcodeid.setFilters(new InputFilter[]{filter});

        etdiscount = findViewById(R.id.etdiscount);
        etdiscount.setFilters(new InputFilter[]{filter});

        etexpirydate = findViewById(R.id.et_expirydate);
        etexpirydate.setFilters(new InputFilter[]{filter});

        etcostprice = findViewById(R.id.etcostprice);
        etcostprice.setFilters(new InputFilter[]{filter});

        ettotalstock = findViewById(R.id.ettotalstock);
        ettotalstock.setFilters(new InputFilter[]{filter});

        addInventoryImage = findViewById(R.id.add_inventory_image);
        add =  findViewById(R.id.tv_add);
        clear =  findViewById(R.id.tv_clear);
        cancel =  findViewById(R.id.tv_cancel);
        chooseImage =  findViewById(R.id.chooseImage);

        backarrow = findViewById(R.id.backarrow);
        clear.setOnClickListener(this);
        add.setOnClickListener(this);
        cancel.setOnClickListener(this);
        etexpirydate.setOnClickListener(this);

        Intent intent= getIntent();
        check_to_edit = intent.getStringExtra("forEdit");
        product_Name = intent.getStringExtra("intentproductname");
        product_ID = intent.getStringExtra("intentproductId");

        showDataToFill(product_ID);

        etsearch.setText(category);
        etproductname.setText(product_Name);
        etbrandname.setText(brandName);
        etdiscription.setText(description);
        etconstraint.setText(constraint);
        etsuplierid.setText(supplierId);
        etunitprice.setText(unitPrice);
        etbarcodeid.setText(barCodeId);
        etexpirydate.setText(expiredate);
        etcostprice.setText(costPrice);
        ettotalstock.setText(totalStock);
        select_hsn_no.setText(hsn);
//        show_hsn_No.setText(hsn);

        if (inventory_image_upload != null && !inventory_image_upload.equalsIgnoreCase("")){
            try{

                byte[] encodeByte = Base64.decode(inventory_image_upload, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                        encodeByte.length);
                addInventoryImage.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backarrow: {
                hideKeyboard(this);
                finish();
            } break;


            case R.id.tv_add:

                isValidate();
                break;

            case R.id.tv_cancel:
                finish();
                break;

            case R.id.tv_clear:
                clearValue();
                break;

            case R.id.et_expirydate:
                DialogFragment dialogFragment = new SelectDateFragment();
                dialogFragment.show(getFragmentManager(),"Date Picker");
                break;
        }
    }

    String [] categoryName = {"All Categories","Electronic","Grocery","Household","Pharmacy","Stationary","Clothes","Furniture's"};

    private void initCustomSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, categoryName);
        //Getting the instance of AutoCompleteTextView

        etsearch.setThreshold(1);//will start working from first character
        etsearch.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        etsearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                item = parent.getItemAtPosition(position).toString();

                if(position > 0){
                    Toast.makeText
                            (EditInventoryActivity.this, "Selected : " + item, Toast.LENGTH_SHORT)
                            .show();
                    if(item == "North Indian") {
                        etsearch.setText(item);
                    } else if (item == "South Indian") {
                        etsearch.setText(item);
                    }

                    else if (item == "Chinese") {
                        etsearch.setText(item);
                    }  else if (item == "Punjabi") {
                        etsearch.setText(item);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinnerError(Spinner spinner){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error"); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            //  selectedTextView.setText("*"); // actual error message
            // spinner.performClick(); // to open the spinner list if error is found.

        }
    }

    private void clearValue() {
        etsearch.setText("");
        etproductname.setText("");
        etbrandname.setText("");
        etdiscription.setText("");
        etconstraint.setText("");
        etsuplierid.setText("");
        etunitprice.setText("");
        etbarcodeid.setText("");
        etdiscount.setText("");
        etexpirydate.setText("");
        etcostprice.setText("");
        ettotalstock.setText("");
    }

    private void isValidate() {

        search = etsearch.getText().toString().trim();
        product_Name = etproductname.getText().toString().trim();
        brandName = etbrandname.getText().toString().trim();
        discription = etdiscription.getText().toString().trim();
        constraint = etconstraint.getText().toString().trim();
        supplierId = etsuplierid.getText().toString().trim();
        unitPrice = etunitprice.getText().toString().trim();
        barCodeId = etbarcodeid.getText().toString().trim();
        expiryDate = etexpirydate.getText().toString().trim();
        costPrice = etcostprice.getText().toString().trim();
        totalStock = ettotalstock.getText().toString().trim();
        selected_HSN_No = select_hsn_no.getText().toString().trim();



        if(search.equals("")){
            etsearch.setError("*");
        }
        else if(select_hsn_no.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(context, "Please select HSN number", Toast.LENGTH_LONG).show();
        }
        else if(product_Name.equals("")){
            etproductname.setError("*");
        }
        else if(brandName.equals("")){
            etbrandname.setError("*");
        }

        else if(discription.equals("")){
            etdiscription.setError("*");
        }
        else if(constraint.equals("")){
            etconstraint.setError("*");
        }
        else if(supplierId.equals("")){
            etsuplierid.setError("*");
        }
        else if(unitPrice.equals("")){
            etunitprice.setError("*");
        }
        else if(barCodeId.equals("")){
            etbarcodeid.setError("*");
        }
        else  if(expiryDate.equals("")){
            etexpirydate.setError("*");
        }
        else  if(costPrice.equals("")){
            etcostprice.setError("*");
        }
        else  if(totalStock.equals("")){
            ettotalstock.setError("*");
        }
        else{

            insertInventorydata(product_Name);

            Toast.makeText(this,"Edit Successfully!",Toast.LENGTH_SHORT).show();

            finish();
        }
    }


    public boolean insertInventorydata(String productId) {
        Log.d("customerInsertthegun", "value" + "\n"+
                productId);
        mydatabase   = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/newinventory.db", null, 0);
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", search.toString().trim());
        contentValues.put("productName", product_Name.toString().trim());
        contentValues.put("UOM_ID", "Quantity");
        contentValues.put("description", discription.toString().trim());
        contentValues.put("constrains", constraint.toString().trim());
        contentValues.put("supplier_ID", supplierId.toString().trim());
        contentValues.put("unitPrice", unitPrice.toString().trim());
        contentValues.put("barcodeReader_ID", barCodeId.toString().trim());
        contentValues.put("gst_ID", gstId.toString().trim());
        contentValues.put("brandName", brandName.toString().trim());
        contentValues.put("discount", discount.toString());
        contentValues.put("expiryDate", expiryDate.toString().trim());
        contentValues.put("costPrice", costPrice.toString().trim());
        contentValues.put("totalstock", totalStock.toString().trim());
        contentValues.put("image_path", inventory_image_upload);
        contentValues.put("store_ID", storeID);
        contentValues.put("subcategory_ID", "ffhguy32");
        contentValues.put("HSN_ID", selected_HSN_No);
        long result= mydatabase.update("tbl_product", contentValues, "productName='"+productId+"'", null);
        Log.d("customerInsertthegun", "value" + result+"\n"+
                productId);
        Toast.makeText(this,"Inventory update successfully",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditInventoryActivity.this, MainActivity.class);
        intent.putExtra("openInventory","inventory");
        startActivity(intent);
        if (result == -1){
            return false;
        }
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

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void cameraDialog(){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditInventoryActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.camera_dialog, null);
        dialogBuilder.setView(dialogView);
        LinearLayout takephoto = dialogView.findViewById(R.id.takephoto);
        LinearLayout gallery = dialogView.findViewById(R.id.choosegallery);

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode_CAMERA);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chooseImage();

            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }

                }
                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("resultcode", String.valueOf(resultCode)+" "+data+" "+requestCode);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(EditInventoryActivity.this.getContentResolver(), uri);
                ImageView imageView = findViewById(R.id.add_inventory_image);
                ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,15, baos);

                byte [] b=baos.toByteArray();
                inventory_image_upload =Base64.encodeToString(b, Base64.DEFAULT);
                imageView.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(requestCode_CAMERA == requestCode && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            ImageView imageView = findViewById(R.id.add_inventory_image);
            ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,15, baos);
            byte [] b=baos.toByteArray();
            inventory_image_upload =Base64.encodeToString(b, Base64.DEFAULT);
            imageView.setImageBitmap(bitmap);
        }

        alertDialog.dismiss();


    }

    private void getHSN_Number() {
        try {
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/newinventory.db", null, 0);
            mydatabase.beginTransaction();
            Cursor cursor = mydatabase.rawQuery("SELECT * from tbl_gst", null);
            // Log.d("customer table", mydatabase.getAttachedDbs().toString());
            Log.d("table customer", cursor.toString());
            // if Cursor is contains results

            mydatabase.endTransaction();

            jsonArray = cur2Json(cursor);
            Log.e("customer data::", "customer data " + jsonArray);
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
                gstModel = new GSTModel();
                gstModel.setGst_Id(jsonobject.getInt("gst_ID"));
                gstModel.setGst_Cat_ID(jsonobject.getInt("gst_Cat_ID"));
                gstModel.setHsn_no(jsonobject.getString("HSN_No"));
                gstModel.setItem(jsonobject.getString("Item"));
                gstModel.setItem_Desc(jsonobject.getString("Item_Desc"));
                gstModel.setIgst(jsonobject.getString("igst"));
                gstModel.setSgst(jsonobject.getString("sgst"));
                gstModel.setCgst(jsonobject.getString("cgst"));
                gstModelArrayList.add(gstModel);

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
                        e.printStackTrace();
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;

    }

    private void showDataToFill(String productname) {
        try {
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/newinventory.db", null, 0);
            mydatabase.beginTransaction();
            Cursor cursor = mydatabase.rawQuery("SELECT * from tbl_product where product_ID='"+productname+"'", null);
            // Log.d("customer table", mydatabase.getAttachedDbs().toString());
            Log.d("table customer", cursor.toString());
            // if Cursor is contains results

            mydatabase.endTransaction();

            jsonArrayProduct = cur2Json(cursor);
            inventoryData(jsonArrayProduct);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void inventoryData(JSONArray jsonArrayProduct) {

        for (int i = 0; i < jsonArrayProduct.length(); i++) {
            JSONObject jsonobject = null;
            try{
                jsonobject = jsonArrayProduct.getJSONObject(i);
               product_Name = jsonobject.optString("productName");
               totalStock = jsonobject.optString("totalstock");
               discount =  jsonobject.optString("discount");
               totalStock=  jsonobject.optString("totalstock");
               unitPrice =     jsonobject.optString("unitPrice");
               brandName =  jsonobject.optString("brandName");
               description = jsonobject.optString("description");
               constraint = jsonobject.optString("constrains");
               supplierId = jsonobject.optString("supplier_ID");
               barCodeId = jsonobject.optString("barcodeReader_ID");
               gstId = jsonobject.optString("gst_ID");
               category = jsonobject.getString("category");
               expiredate = jsonobject.optString("expiryDate");
               costPrice = jsonobject.optString("costPrice");
               hsn = jsonobject.optString("HSN_ID");
               inventory_image_upload = jsonobject.optString("image_path");
            }catch (JSONException e){
                  e.printStackTrace();
            }
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


    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    this, year, month, day);
            calendar.add(Calendar.DATE,0);
            Date newDate = calendar.getTime();
            datePickerDialog.getDatePicker().setMinDate(newDate.getTime() - (newDate.getTime() % (24*60*60*1000)));
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            int currentDate = month + 1;
            etexpirydate.setText(dayOfMonth+"/"+currentDate+"/"+year);

        }

    }
}
