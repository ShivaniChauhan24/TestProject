package com.shipgig.thegun.pos.fragment;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.shipgig.thegun.pos.MainActivity;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.adapter.CustomListAdapter;
import com.shipgig.thegun.pos.model.GSTModel;
import com.shipgig.thegun.pos.utilities.Sharepreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static com.shipgig.thegun.pos.MainActivity.drawer;


public class AddInventoryFragment extends Fragment implements View.OnClickListener {
    AlertDialog alertDialog;
    Bitmap thumbnail;
    View myView;
    TextView chooseImage;
    LinearLayout click_select;
    AutoCompleteTextView actv;
    public static int REQUEST_CAMERA = 1;
    String pictureImagePath = "",TAG = "AddServices", imgBase64="",item;
    private static final int PERMISSION_REQUEST_CODE = 200;
    String [] categoryName = {"All Categories","Electronic","Grocery","Pharmacy","Stationary","Household","Clothes","Furniture's"};
    EditText etproductname,etbrandname,etdiscription,etconstraint,etsuplierid,etunitprice,
            etbarcodeid,etdiscount,etcostprice,ettotalstock;
    public static EditText etexpirydate;
    String search,productName,brandName,discription,constraint,supplierId,unitPrice,barCodeId,
            gstId,discount,expiryDate,costPrice,totalStock;
    TextView add,cancel,clear;
    private SQLiteDatabase mydatabase;
    private ImageView addInventoryImage;
    LinearLayout tool;
    RadioGroup radiogroup_chose;
    String radio_chose;
    private int PICK_IMAGE_REQUEST = 1;
    String inventory_image_upload = "";
    final int DRAWABLE_RIGHT = 2;
    private final int requestCode_CAMERA = 20;
    Context context;

    private JSONArray jsonArray = new JSONArray();
    private ArrayList<GSTModel> gstModelArrayList= new ArrayList<>();
    private GSTModel gstModel;
    ImageView open_drawer;


    EditText select_hsn_no;
    ListView hsn_no_ListView;
    CustomListAdapter customListAdapter;
    String selected_HSN_No,selected_CGST,selected_SGST;
    int SelectedgstID_from_Hsn;
    int storeID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.add_inventory_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        context = getActivity();

        storeID = Sharepreference.getSharedPreferenceInt(getActivity(),"storeID",0);
        init();

        getHSN_Number();

        open_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });


        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraDialog();
            }
        });

        myView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.
                            INPUT_METHOD_SERVICE);
                    if (getActivity().getCurrentFocus() != null) {
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                }
                return true;
            }
        });

        select_hsn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom_listviewdisplay, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Select Operator");
                hsn_no_ListView = (ListView) convertView.findViewById(R.id.common);
                customListAdapter = new CustomListAdapter(getActivity(), R.layout.hsn_no_listitem, gstModelArrayList);
                hsn_no_ListView.setAdapter(customListAdapter);
                hsn_no_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selected_HSN_No = gstModelArrayList.get(position).getHsn_no();
                        SelectedgstID_from_Hsn = gstModelArrayList.get(position).getGst_Cat_ID();
                        select_hsn_no.setText(selected_HSN_No);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        return myView;
    }

    private void init() {
        select_hsn_no= myView.findViewById(R.id.select_hsn_no);
        if (checkPermission()) {

        } else {
            requestPermission();
        }
        open_drawer = myView.findViewById(R.id.open_drawer);
        actv = myView.findViewById(R.id.autoCompleteTextView);
        etproductname = myView.findViewById(R.id.etproductname);
        etbrandname = myView.findViewById(R.id.etbrandname);
        etdiscription = myView.findViewById(R.id.etdescription);
        etconstraint = myView.findViewById(R.id.etconstrains);
        etsuplierid = myView.findViewById(R.id.etsupplyid);
        etunitprice = myView.findViewById(R.id.etunitprice);
        etbarcodeid = myView.findViewById(R.id.etbarcodeid);
        etdiscount = myView.findViewById(R.id.etdiscount);
        etexpirydate = myView.findViewById(R.id.et_expirydate);
        etcostprice = myView.findViewById(R.id.etcostprice);
        ettotalstock = myView.findViewById(R.id.ettotalstock);
        add = myView. findViewById(R.id.tv_add);
        clear = myView. findViewById(R.id.tv_clear);
        cancel = myView. findViewById(R.id.tv_cancel);
        addInventoryImage = myView.findViewById(R.id.add_inventory_image);
        chooseImage =  myView.findViewById(R.id.chooseImage);
        click_select =  myView.findViewById(R.id.click_select);
        click_select.setOnClickListener(this);
        clear.setOnClickListener(this);
        add.setOnClickListener(this);
        cancel.setOnClickListener(this);
        etexpirydate.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add:
                isValidate();
                break;

            case R.id.tv_cancel:

                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_clear:
                clearValue();
                break;

            case R.id.et_expirydate:
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(),"Choose Date");
                break;

        }
    }

    private void clearValue() {
        actv.setText("");
        etproductname.setText("");
        etbrandname.setText("");
        etdiscription.setText("");
        etconstraint.setText("");
        etsuplierid.setText("");
        etunitprice.setText("");
        etbarcodeid.setText("");
        etexpirydate.setText("");
        etcostprice.setText("");
        ettotalstock.setText("");
    }

    private void isValidate() {

        search = actv.getText().toString().trim();
        productName = etproductname.getText().toString().trim();
        brandName = etbrandname.getText().toString().trim();
        discription = etdiscription.getText().toString().trim();
        constraint = etconstraint.getText().toString().trim();
        supplierId = etsuplierid.getText().toString().trim();
        unitPrice = etunitprice.getText().toString().trim();
        barCodeId = etbarcodeid.getText().toString().trim();
        discount = etdiscount.getText().toString().trim();
        expiryDate = etexpirydate.getText().toString().trim();
        costPrice = etcostprice.getText().toString().trim();
        totalStock = ettotalstock.getText().toString().trim();



        if (search.equals("")){
            actv.setError("*");
        }
        else if(select_hsn_no.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(context, "Please select HSN number", Toast.LENGTH_LONG).show();
        }
        else if(productName.equals("")){
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
            if (Integer.parseInt(unitPrice) > Integer.parseInt(costPrice)){
                insertInventorydata();
            }else {
                etcostprice.setError("Always cost price less than unit price");
            }

        }
    }

    public boolean insertInventorydata() {
        mydatabase   = SQLiteDatabase.openDatabase("/data/data/" + getActivity().getPackageName() + "/newinventory.db", null, 0);
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", search.toString().trim());
        contentValues.put("productName", productName.toString().trim());
        contentValues.put("UOM_ID", "Quantity");
        contentValues.put("description", discription.toString().trim());
        contentValues.put("constrains", constraint.toString().trim());
        contentValues.put("supplier_ID", supplierId.toString().trim());
        contentValues.put("unitPrice", unitPrice.toString().trim());
        contentValues.put("brandName", brandName.toString().trim());
        contentValues.put("barcodeReader_ID", barCodeId.toString().trim());
        contentValues.put("gst_ID",SelectedgstID_from_Hsn);
        contentValues.put("discount", "0.0");
        contentValues.put("expiryDate", expiryDate.toString().trim());
        contentValues.put("costPrice", costPrice.toString().trim());
        contentValues.put("totalstock", totalStock.toString().trim());
        if (!inventory_image_upload.equals("")){
            contentValues.put("image_path", inventory_image_upload);
        }
        if (inventory_image_upload.equals("")){
            contentValues.put("image_path", "");
        }
        contentValues.put("store_ID", storeID);
        contentValues.put("HSN_ID", selected_HSN_No);
        contentValues.put("subcategory_ID", "ffhguy32");
        long result = mydatabase.insertOrThrow("tbl_product", null, contentValues);
        Toast.makeText(getContext(),"Inventory added successfully",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
        if (result == -1)
            return false;
        else{
            return true;
        }

    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
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
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void cameraDialog(){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.camera_dialog, null);
        dialogBuilder.setView(dialogView);
        LinearLayout takephoto = (LinearLayout)dialogView.findViewById(R.id.takephoto);
        LinearLayout gallery = (LinearLayout)dialogView.findViewById(R.id.choosegallery);

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode_CAMERA);
//                clickpic();
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

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            Log.d("resultcode", String.valueOf(resultCode)+" "+data+" "+requestCode);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                ImageView imageView = myView.findViewById(R.id.add_inventory_image);
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
            ImageView imageView = myView.findViewById(R.id.add_inventory_image);
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
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getContext().getPackageName() + "/newinventory.db", null, 0);
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
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//            etexpirydate.setText(dayOfMonth+"/"+currentDate+"/"+year);
            etexpirydate.setText(year+"-"+currentDate+"-"+dayOfMonth+ " "+currentTime);

        }

    }

}
