package com.shipgig.thegun.pos.Payment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;
import com.shipgig.thegun.pos.MainActivity;
import com.shipgig.thegun.pos.Printer.PrinterActivity;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.adapter.HomeProductAdapter;
import com.shipgig.thegun.pos.model.SendMailModel;
import com.shipgig.thegun.pos.utilities.GetHashModel;
import com.shipgig.thegun.pos.utilities.RetrofitClient;
import com.shipgig.thegun.pos.utilities.Sharepreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import static com.shipgig.thegun.pos.fragment.CartFragment.addItemCartArray;
import static com.shipgig.thegun.pos.fragment.NewHomeFragment.itemInCart;

public class ProceedtoPayActivity extends AppCompatActivity {

    File file;
    private Bitmap screenShotBitmap;
    ImageView open_drawer;
    ImageView cashImage,paytmImage,cardImage;
    String select_payMethod;
    String noItem,totalAmt;
    TextView cash,paytm,card;
    Button pay_for_Proceed;
    LinearLayout cashContainer,paytmContainer,cardContainer,paylater;
    CardView cashCard,paytmCard,cardCard;
    String discount,subtotal,taxes,creatOn,customerName,customer_ID,customer_EMAIL,customer_MOBILE;
    byte[] byteArray;
    String itemName,finalItemQty,itemQty;
    ProgressDialog progressDialog;
    Resources resources;
    Context context;
    int storeID,systemID;
    private ProgressDialog dialog;
    Random random;
    String txnId,productName;

    JSONArray jsonArray = new JSONArray();
    int totalstock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_proceedto_pay);

        Intent callingIntent= getIntent();
        noItem= callingIntent.getStringExtra("noofitems");
        totalAmt= callingIntent.getStringExtra("totalamt");
        taxes= callingIntent.getStringExtra("taxes");
        discount= callingIntent.getStringExtra("discountamt");
        subtotal= callingIntent.getStringExtra("subtotalamt");
        customerName= callingIntent.getStringExtra("customer");
        customer_ID= callingIntent.getStringExtra("cus_id");
        byteArray= callingIntent.getByteArrayExtra("invoice");
        customer_EMAIL= callingIntent.getStringExtra("cust_email");
        customer_MOBILE= callingIntent.getStringExtra("cust_mobile");
        screenShotBitmap  = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        register();
        storeID = Sharepreference.getSharedPreferenceInt(ProceedtoPayActivity.this,"storeID",0);
        systemID = Sharepreference.getSharedPreferenceInt(ProceedtoPayActivity.this,"user_system",0);

        open_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = formatter.format(todayDate);
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        creatOn = currentDate + " " + currentTime;
        pay_for_Proceed.setText("Payable Amount Rs  " + totalAmt);

        cashContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.equals(cashContainer)){
//                    cash.setCompoundDrawablesWithIntrinsicBounds( R.drawable.checked, 0, 0, 0);
//                    Toast.makeText(ProceedtoPayActivity.this, ""+select_payMethod, Toast.LENGTH_SHORT).show();
//                    paytm.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);
//                    card.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    select_payMethod = "Cash";
                    paytm.setText("Paytm");
                    card.setText("Card");
                    cashCard.setCardBackgroundColor(resources.getColor(R.color.black));
                    paytmCard.setCardBackgroundColor(resources.getColor(R.color.white));
                    cardCard.setCardBackgroundColor(resources.getColor(R.color.white));

                }
            }
        });

        paytmContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.equals(paytmContainer)){
//                    paytm.setCompoundDrawablesWithIntrinsicBounds( R.drawable.checked, 0, 0, 0);
//                    Toast.makeText(ProceedtoPayActivity.this, ""+select_payMethod, Toast.LENGTH_SHORT).show();
//                    cash.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);
//                    card.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);select_payMethod = "Paytm";
                    select_payMethod = "Paytm";
                    cash.setText("Cash");
                    card.setText("Card");
                    paytmCard.setCardBackgroundColor(resources.getColor(R.color.black));
                    cashCard.setCardBackgroundColor(resources.getColor(R.color.white));
                    cardCard.setCardBackgroundColor(resources.getColor(R.color.white));
                }
            }
        });

        cardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.equals(cardContainer)){
//                    card.setCompoundDrawablesWithIntrinsicBounds( R.drawable.checked, 0, 0, 0);
//                    Toast.makeText(ProceedtoPayActivity.this, ""+select_payMethod, Toast.LENGTH_SHORT).show();
//                    cash.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);
//                    paytm.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);
                    select_payMethod = "Card";
                    cash.setText("Cash");
                    paytm.setText("Paytm");
                    cardCard.setCardBackgroundColor(resources.getColor(R.color.black));
                    paytmCard.setCardBackgroundColor(resources.getColor(R.color.white));
                    cashCard.setCardBackgroundColor(resources.getColor(R.color.white));

                }
            }
        });

        paylater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProceedtoPayActivity.this);
                alertDialog.setTitle(Html.fromHtml("<font color='#FF7F27'>Confirmation Alert..!!!</font>"));
                alertDialog.setMessage(Html.fromHtml("<font color='#000000'>Are you sure want to pay on later!</font>"));
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        select_payMethod = "Outstanding Balance";
//                        for (int i = 0; i < addItemCartArray.size(); i++){
//                            itemName = addItemCartArray.get(i).getProductName();
//                            itemQty = addItemCartArray.get(i).getQuantity();
//                            int max = Integer.parseInt(HomeProductAdapter.productsArray.get(i).getTotalQty());
//                            finalItemQty = String.valueOf(((max) - Integer.parseInt(itemQty)));
//                            insertInventorydata(itemName,finalItemQty);
//                            insertInToTransactionTable();
//                        }
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                    }
                });
                alertDialog.create();
                alertDialog.show();

            }
        });

        pay_for_Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date todayDate = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                String currentDate = formatter.format(todayDate);
                String currentTime = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
                String invoice_id = currentDate + currentTime;
                random = new Random();
                int randomN = Math.abs(random.nextInt());
                txnId = String.valueOf(storeID)+invoice_id+String.valueOf(randomN);
                txnId = txnId.replace("-","");

                try {

                    if (select_payMethod.equals("Cash")) {
                        dialog.setTitle(Html.fromHtml("<font color='#6AC4D1'>Please wait</font>"));
                        dialog.setMessage(Html.fromHtml("<font color='#000000'> While getting the detail</font>"));
                        dialog.show();

                        for (int i = 0; i < addItemCartArray.size(); i++){
                            itemName = addItemCartArray.get(i).getProductName();
                            itemQty = addItemCartArray.get(i).getQuantity();
                            String productID = addItemCartArray.get(i).getProduct_Id();
                            getProductQuantityFromTble(productID);
                            finalItemQty = String.valueOf(((totalstock) - Integer.parseInt(itemQty)));
                            insertInventorydata(productID,finalItemQty);
                            //for show the void bill use this method
                            String singlePrice = addItemCartArray.get(i).getProductPrice();
                            String discount = addItemCartArray.get(i).getDiscount();
                            String product_ID = addItemCartArray.get(i).getProduct_Id();
                            String productName = addItemCartArray.get(i).getProductName();
                            cancelVoidBill(product_ID,productName,itemQty,singlePrice,discount);
                        }
                        insertInToTransactionTable();
                        dialog.dismiss();
                        Intent intent = new Intent(ProceedtoPayActivity.this, PrinterActivity.class);
                        intent.putExtra("noofitems",noItem);
                        intent.putExtra("totalamt",totalAmt);
                        startActivity(intent);
                        finish();
//                        if (customer_EMAIL != null && !customer_EMAIL.equalsIgnoreCase("NA")){
//                            savetoGallery(screenShotBitmap);
//                            sendInvoiceInMail(customer_EMAIL,totalAmt,customerName);
//                        }
//                        else {
//                            insertInToTransactionTable();
//                            //for void bill use this method
//                            for (int i = 0; i < addItemCartArray.size(); i++){
//                                itemQty = addItemCartArray.get(i).getQuantity();
//                                String singlePrice = addItemCartArray.get(i).getProductPrice();
//                                String discount = addItemCartArray.get(i).getDiscount();
//                                String productID = addItemCartArray.get(i).getProduct_Id();
//                                String productName = addItemCartArray.get(i).getProductName();
//                                cancelVoidBill(productID,productName,itemQty,singlePrice,discount);
//                            }
//                            dialog.dismiss();
//                            Intent intent = new Intent(ProceedtoPayActivity.this, PrinterActivity.class);
//                            intent.putExtra("noofitems",noItem);
//                            intent.putExtra("totalamt",totalAmt);
//                            startActivity(intent);
//                            finish();
//
//                        }
                    }

                    else if (select_payMethod.equals("Card")) {

                        dialog.setTitle(Html.fromHtml("<font color='#6AC4D1'>Please wait</font>"));
                        dialog.setMessage(Html.fromHtml("<font color='#000000'> While you are redirected to make payment gateway</font>"));
                        dialog.show();
                        launchPaymentFlow();
                    }

                    else if (select_payMethod.equals("Paytm")){

                        if (ContextCompat.checkSelfPermission(ProceedtoPayActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ProceedtoPayActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                        }

                        dialog.setTitle(Html.fromHtml("<font color='#6AC4D1'>Please wait</font>"));
                        dialog.setMessage(Html.fromHtml("<font color='#000000'> While you are redirected to make payment gateway</font>"));
                        dialog.show();

                        paytmPay();

                    }

                } catch (NullPointerException e) {
                    showAlert("Please select payment method");
                }
            }
        });

    }

    private void paytmPay() {

        String mid = "BPpjsO89037701595730";
        String url = "https://afrozmypaytm.000webhostapp.com/paytmchecksum/generateChecksum.php";//checksum path
        String callBack = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";//common URL for all

        RequestQueue requestQueue = Volley.newRequestQueue(ProceedtoPayActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("CHECKSUMHASH")){
                        String CHECKSUMHAS = jsonObject.getString("CHECKSUMHASH");
                        PaytmPGService paytmPGService = PaytmPGService.getStagingService();
                        HashMap<String, String> paramMap = new HashMap<String,String>();
                        paramMap.put( "MID" , mid);
                        paramMap.put( "ORDER_ID" , "order1212");
                        paramMap.put( "CUST_ID" , "cust12233");
                        paramMap.put( "MOBILE_NO" , "7777777777");
                        paramMap.put( "EMAIL" , "afroz@shipgigventures.com");
                        paramMap.put( "CHANNEL_ID" , "WAP");
                        paramMap.put( "TXN_AMOUNT" , totalAmt);
                        paramMap.put( "WEBSITE" , "WEBSTAGING");
                        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
                        paramMap.put( "CALLBACK_URL", callBack);
                        paramMap.put("CHECKSUMHASH",CHECKSUMHAS);
                        PaytmOrder paytmOrder = new PaytmOrder(paramMap);
                        paytmPGService.initialize(paytmOrder,null);
                        paytmPGService.startPaymentTransaction(ProceedtoPayActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                            @Override
                            public void onTransactionResponse(Bundle inResponse) {

                                if (inResponse.getString("STATUS").equals("TXN_SUCCESS")){
                                    dialog.dismiss();
                                    Date todayDate = Calendar.getInstance().getTime();
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                                    String currentDate = formatter.format(todayDate);
                                    String currentTime = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
                                    String invoice_id = currentDate + currentTime;
                                    random = new Random();
                                    int randomN = Math.abs(random.nextInt());
                                    txnId = String.valueOf(storeID)+invoice_id+String.valueOf(randomN);
                                    txnId = txnId.replace("-","");

                                    for (int i = 0; i < addItemCartArray.size(); i++){
                                        itemName = addItemCartArray.get(i).getProductName();
                                        itemQty = addItemCartArray.get(i).getQuantity();
                                        String productID = addItemCartArray.get(i).getProduct_Id();
                                        getProductQuantityFromTble(productID);
                                        finalItemQty = String.valueOf(((totalstock) - Integer.parseInt(itemQty)));
                                        insertInventorydata(productID,finalItemQty);


                                        //for show the void bill use this method

                                        String singlePrice = addItemCartArray.get(i).getProductPrice();
                                        String discount = addItemCartArray.get(i).getDiscount();
                                        String product_ID = addItemCartArray.get(i).getProduct_Id();
                                        String productName = addItemCartArray.get(i).getProductName();
                                        cancelVoidBill(product_ID,productName,itemQty,singlePrice,discount);
                                    }
                                    insertInToTransactionTable();
                                    dialog.dismiss();
                                    Intent intent = new Intent(ProceedtoPayActivity.this, PrinterActivity.class);
                                    intent.putExtra("noofitems",noItem);
                                    intent.putExtra("totalamt",totalAmt);
                                    startActivity(intent);
                                    finish();
                                }
                                else if (inResponse.getString("STATUS").equals("TXN_FAILURE")){
                                    dialog.dismiss();
                                    String mesg = inResponse.getString("RESPMSG");
                                    android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(ProceedtoPayActivity.this);
                                    dialog.setTitle(Html.fromHtml("<font color='#FF7F27'> Alert..!!! </font>"));
                                    dialog.setCancelable(false);
                                    dialog.setMessage(mesg);
                                    dialog.setPositiveButton(
                                            "Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    android.app.AlertDialog alertDialog = dialog.create();
                                    alertDialog.show();
                                }

                            }

                            @Override
                            public void networkNotAvailable() {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void clientAuthenticationFailed(String inErrorMessage) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void someUIErrorOccurred(String inErrorMessage) {
                                dialog.dismiss();
                            }

                            @Override
                            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onBackPressedCancelTransaction() {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                dialog.dismiss();
                            }
                        });

                    }
                }catch (JSONException e){
                    dialog.dismiss();
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(ProceedtoPayActivity.this, "Something went wrong"+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramMap = new HashMap<String,String>();
                paramMap.put( "MID" , mid);
                paramMap.put( "ORDER_ID" , "order1");
                paramMap.put( "CUST_ID" , "cust123");
                paramMap.put( "MOBILE_NO" , "7777777777");
                paramMap.put( "EMAIL" , "username@emailprovider.com");
                paramMap.put( "CHANNEL_ID" , "WAP");
                paramMap.put( "TXN_AMOUNT" , totalAmt);
                paramMap.put( "WEBSITE" , "WEBSTAGING");
                paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
                paramMap.put( "CALLBACK_URL", callBack);
//                               paramMap.put( "CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1");
//                               paramMap.put( "CHECKSUMHASH" , "w2QDRMgp1234567JEAPCIOmNgQvsi+BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4=");
                return paramMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void sendInvoiceInMail( String email, String totalAmt, String customerName) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Being Process");
        progressDialog.setMessage("Please wait, while processing....");
        progressDialog.show();

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("myFile", file.getName(), reqFile);

        RequestBody custName = RequestBody.create(MediaType.parse("text/plain"), customerName);
        RequestBody custEmail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody totAmount = RequestBody.create(MediaType.parse("text/plain"), totalAmt);


        Call<SendMailModel> call = RetrofitClient.getInstance().getApi().sendMail(body,custEmail,totAmount,custName);
        call.enqueue(new Callback<SendMailModel>() {
            @Override
            public void onResponse(Call<SendMailModel> call, retrofit2.Response<SendMailModel> response) {

                try{
                    if (response != null){
                        progressDialog.dismiss();
                        dialog.dismiss();
                        Date todayDate = Calendar.getInstance().getTime();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                        String currentDate = formatter.format(todayDate);
                        String currentTime = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
                        String invoice_id = currentDate + currentTime;
                        random = new Random();
                        int randomN = Math.abs(random.nextInt());
                        txnId = String.valueOf(storeID)+invoice_id+String.valueOf(randomN);
                        txnId = txnId.replace("-","");
                        insertInToTransactionTable();
                        Intent intent = new Intent(ProceedtoPayActivity.this, PrinterActivity.class);
                        intent.putExtra("noofitems",noItem);
                        intent.putExtra("totalamt",totalAmt);
                        startActivity(intent);
                        finish();
                    }
                }catch (NullPointerException e){
                    dialog.dismiss();
                    Toast.makeText(ProceedtoPayActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<SendMailModel> call, Throwable t) {
                progressDialog.dismiss();
                dialog.dismiss();
                String error = t.getMessage().toString();
                Toast.makeText(ProceedtoPayActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.e("ErrorView", t.getMessage().toString());
            }
        });
    }

    private void register() {

        context = this;
        open_drawer = findViewById(R.id.open_drawer);
        cashImage = findViewById(R.id.cash_image);
        paytmImage = findViewById(R.id.paytm_image);
        cardImage = findViewById(R.id.card_image);
        cash = findViewById(R.id.cash);
        paytm = findViewById(R.id.paytm);
        card = findViewById(R.id.card);
        paylater = findViewById(R.id.paylater);
        pay_for_Proceed = findViewById(R.id.pay_for_Proceed);
        cashContainer = findViewById(R.id.cash_container);
        paytmContainer = findViewById(R.id.paytm_container);
        cardContainer = findViewById(R.id.card_container);
        cashCard = findViewById(R.id.cashCard);
        paytmCard = findViewById(R.id.paytmCard);
        cardCard = findViewById(R.id.cardCard);
        resources = context.getResources();
        dialog = new ProgressDialog(ProceedtoPayActivity.this);
        dialog.setCancelable(false);


    }

    private boolean insertInventorydata(String productID, String updateQty) {

        try {
            String pdID = "\""+productID+"\"";
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + context.getPackageName() + "/newinventory.db", null, 0);
            ContentValues contentValues = new ContentValues();
            contentValues.put("totalstock", updateQty);
            long i = mydatabase.update("tbl_product", contentValues, "product_ID"+"="+pdID, null);

            if (i == -1)
                return false;
            else{
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public boolean insertInToTransactionTable() {

        SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/newinventory.db", null, 0);
        ContentValues contentValues = new ContentValues();
        contentValues.put("itemCount", itemInCart.getText().toString().trim());
        contentValues.put("totalAmount",totalAmt);
        contentValues.put("CGST", taxes);
        contentValues.put("SGST", "0.0");
        contentValues.put("discount", discount);
        contentValues.put("specialDiscount", "0.0");
        contentValues.put("paymentMode", select_payMethod);
        contentValues.put("transaction_ID", txnId);
        contentValues.put("custUser_ID", customer_ID);
        contentValues.put("createdOn", creatOn);
        contentValues.put("isDeleted", 0);
        contentValues.put("store_ID",storeID);
        contentValues.put("customer_name", customerName);
        long result = mydatabase.insertOrThrow("tbl_transaction", null, contentValues);
        if (result == -1)
            return false;
        else{
            return true;
        }

    }

    private boolean cancelVoidBill(String productID,String productName,String item_Qty,String singleP,String discount) {

        random = new Random();
        int randomN = random.nextInt();
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String currentDate = formatter.format(todayDate);
        String currentTime = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
        String invoice_id = String.valueOf(storeID) +currentDate + currentTime+String.valueOf(randomN);

        try{

            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getPackageName() + "/newinventory.db", null, 0);
            ContentValues contentValues = new ContentValues();
            contentValues.put("invoice_ID",invoice_id);
            contentValues.put("transaction_ID",txnId);
            contentValues.put("product_ID", productID);
            contentValues.put("quantity", item_Qty);
            contentValues.put("unitPrice", singleP);
            contentValues.put("discount", discount);
            contentValues.put("createdOn", creatOn);
            contentValues.put("store_ID",storeID);
            contentValues.put("product_name", productName);
            long result = mydatabase.insertOrThrow("tbl_invoice", null, contentValues);
            Log.e("MYERR",String.valueOf(result));
            if (result == -1)
                return false;
            else{
                return true;
            }
        }catch (Exception e){
            Toast.makeText(this, "Error: "+e, Toast.LENGTH_SHORT).show();
        }
        return true;

    }

    private void showAlert(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        alertDialog.show();
    }

    private void launchPaymentFlow() {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();
        payUmoneyConfig.setPayUmoneyActivityTitle("Buy" + "now");
        payUmoneyConfig.setAccentColor(getResources().getResourceName(R.color.peripherals_button_color_blue));
        payUmoneyConfig.setColorPrimary(getResources().getResourceName(R.color.peripherals_button_color_blue));
        payUmoneyConfig.setDoneButtonText("Pay " + (totalAmt) + (totalAmt));

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String currentDate = formatter.format(todayDate);
        String currentTime = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
        String invoice_id = currentDate + currentTime;
        random = new Random();
        int randomN = Math.abs(random.nextInt());
        txnId = String.valueOf(storeID)+invoice_id+String.valueOf(randomN);
        txnId = txnId.replace("-","");
        productName = "Core Jave";

        builder.setAmount(totalAmt)
                .setTxnId(txnId)
                .setPhone(Constants.MOBILE)
                .setProductName(productName)
                .setFirstName(Constants.FIRST_NAME)
                .setEmail(Constants.EMAIL)
                .setsUrl(Constants.SURL)
                .setfUrl(Constants.FURL)
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(Constants.DEBUG)
                .setKey(Constants.MERCHANT_KEY)
                .setMerchantId(Constants.MERCHANT_ID);

        try {
            PayUmoneySdkInitializer.PaymentParam mPaymentParams = builder.build();
            calculateHashInServer(mPaymentParams);
        } catch (Exception e) {
            dialog.dismiss();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void calculateHashInServer(final PayUmoneySdkInitializer.PaymentParam mPaymentParams) {

        GetHashModel hashModel = new GetHashModel();
        hashModel.setKey(Constants.MERCHANT_KEY);
        hashModel.setTxnid(txnId);
        hashModel.setAmount(totalAmt);
        hashModel.setProductinfo(productName);
        hashModel.setFirstname(Constants.FIRST_NAME);
        hashModel.setEmail(Constants.EMAIL);

        Call<String> call = RetrofitClient.getInstance().getApi().getHashToken(hashModel);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                try {
                    String merchantHash= response.body();
                    if (merchantHash.isEmpty() || merchantHash.equals("")) {
                        Toast.makeText(ProceedtoPayActivity.this, "Could not generate hash", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        mPaymentParams.setMerchantHash(merchantHash);
                        PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, ProceedtoPayActivity.this, R.style.PayUMoney, true);
                    }
                    dialog.dismiss();
                }catch (NullPointerException e){
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                String error = t.getMessage().toString();
                Toast.makeText(ProceedtoPayActivity.this, "Error"+error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void savetoGallery(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().getPath() + "/Invoice/";
        File myDir = new File(root);

        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String fname = "Invoice-"+System.currentTimeMillis()+ ".jpg";
        file = new File(myDir, fname);

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        path = Uri.fromFile(file);

        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        dialog.dismiss();
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {

            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {

                    Date todayDate = Calendar.getInstance().getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                    String currentDate = formatter.format(todayDate);
                    String currentTime = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
                    String invoice_id = currentDate + currentTime;
                    random = new Random();
                    int randomN = Math.abs(random.nextInt());
                    txnId = String.valueOf(storeID)+invoice_id+String.valueOf(randomN);
                    txnId = txnId.replace("-","");

                    for (int i = 0; i < addItemCartArray.size(); i++){
                        itemName = addItemCartArray.get(i).getProductName();
                        itemQty = addItemCartArray.get(i).getQuantity();
                        String productID = addItemCartArray.get(i).getProduct_Id();
                        getProductQuantityFromTble(productID);
                        finalItemQty = String.valueOf(((totalstock) - Integer.parseInt(itemQty)));
                        insertInventorydata(productID,finalItemQty);

                        //for show the void bill use this method

                        String singlePrice = addItemCartArray.get(i).getProductPrice();
                        String discount = addItemCartArray.get(i).getDiscount();
                        String product_ID = addItemCartArray.get(i).getProduct_Id();
                        String productName = addItemCartArray.get(i).getProductName();
                        cancelVoidBill(product_ID,productName,itemQty,singlePrice,discount);
                    }
                    insertInToTransactionTable();
                    dialog.dismiss();
                    Intent intent = new Intent(ProceedtoPayActivity.this, PrinterActivity.class);
                    intent.putExtra("noofitems",noItem);
                    intent.putExtra("totalamt",totalAmt);
                    startActivity(intent);
                    finish();

                } else if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.CANCELLED)) {
                    showAlert("Payment Cancelled");

                } else if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.FAILED)) {
                    showAlert("Payment Failed");
                }

            } else if (resultModel != null && resultModel.getError() != null) {
                Toast.makeText(this, "Error check log", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Both objects are null", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_CANCELED) {
            showAlert("Payment Cancelled");

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public interface CallBackUs{
        void insertinTable();
    }

    private void getProductQuantityFromTble(String productID) {

        try {
            String tranaction = "\""+productID+"\"";

            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + context.getPackageName() +
                    "/newinventory.db", null, 0);
            mydatabase.beginTransaction();
            Cursor cursor = mydatabase.rawQuery("SELECT totalstock from tbl_product where product_ID="+tranaction, null);
            mydatabase.endTransaction();
            jsonArray = cur2Json(cursor);
            setData(jsonArray);

        } catch (Exception e) {
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

    public void setData(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                totalstock = jsonobject.getInt("totalstock");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
