package com.shipgig.thegun.pos.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.shipgig.thegun.pos.Payment.ProceedtoPayActivity;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.adapter.AddItemCartAdapter;
import com.shipgig.thegun.pos.model.AddItemCartModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import static com.shipgig.thegun.pos.fragment.CartFragment.addItemCartArray;
public class SummaryActivity extends AppCompatActivity {

    RecyclerView summary_recyclerview;
    Button proceed_to_pay;
    TextView totalAmountTxt,dicountTxt,taxAmount,number_of_item,subAmountTxt,customer_name;
    private AddItemCartAdapter addItemCartAdapter;
    Context context;
    private ArrayList<AddItemCartModel> temparraylist = new ArrayList<>();
    String noItem,totalAmt,taxes,discount,subtotal,custName,custID,customer_EMAIL,customer_MOBILE;
    private SQLiteDatabase mydatabase;

    private LinearLayout llScroll,llScroll2;
    private Bitmap screenShotBitmap;
    TextView tv_pdf;
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    Uri path;
    RelativeLayout scroll;
    ScrollView scroll_view_full;

    private ProgressDialog dialog;
    ImageView open_drawer;
    Double includeTaxAmount;
    File file;



    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_summary);

        scroll = findViewById(R.id.scroll);
        llScroll2 = findViewById(R.id.llScroll2);
        tv_pdf = findViewById(R.id.tv_pdf);
        scroll_view_full = findViewById(R.id.scroll_view_full);

        fn_permission();

        open_drawer = findViewById(R.id.open_drawer);

        open_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });



        Intent callingIntent= getIntent();
        noItem= callingIntent.getStringExtra("noofitems");
        totalAmt= callingIntent.getStringExtra("totalamt");
        taxes= callingIntent.getStringExtra("taxes");
        discount= callingIntent.getStringExtra("discountamt");
        subtotal= callingIntent.getStringExtra("subtotalamt");
        custName= callingIntent.getStringExtra("cus_name");
        custID= callingIntent.getStringExtra("cus_id");
        customer_EMAIL= callingIntent.getStringExtra("cus_email");
        customer_MOBILE= callingIntent.getStringExtra("cus_mobile");

        summary_recyclerview = findViewById(R.id.summary_recyclerview);
        proceed_to_pay = findViewById(R.id.proceed_to_pay);
        totalAmountTxt = findViewById(R.id.totalAmountTxt);
        dicountTxt = findViewById(R.id.dicountTxt);
        taxAmount = findViewById(R.id.taxAmount);
        number_of_item = findViewById(R.id.number_of_item);
        subAmountTxt = findViewById(R.id.subAmountTxt);
        customer_name = findViewById(R.id.customer_name);


        dialog = new ProgressDialog(SummaryActivity.this);
        dialog.setCancelable(false);

        addItemToCartMethod();


        totalAmountTxt.setText("Rs. " +totalAmt);
        dicountTxt.setText("Rs. "+discount);
        number_of_item.setText(noItem);
        taxAmount.setText("Rs. "+taxes);
        subAmountTxt.setText("Rs. "+subtotal);
        customer_name.setText(custName);

        includeTaxAmount = Double.parseDouble(totalAmt) + Double.parseDouble(taxes);
        proceed_to_pay.setVisibility(View.VISIBLE);

        proceed_to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double convertAm = Double.valueOf(totalAmt);
                DecimalFormat decimalFormat=new DecimalFormat("#");
                String conAm = decimalFormat.format(convertAm);


       //@@@@@@@@@@@@@@@@@InVoice@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                proceed_to_pay.setVisibility(View.GONE);
                screenShotBitmap = getScreenBitmap();
       //@@@@@@@@@@@@@@@@@InVoice@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                Intent intent = new Intent(SummaryActivity.this, ProceedtoPayActivity.class);
                intent.putExtra("noofitems",noItem);
                intent.putExtra("totalamt",conAm);
                intent.putExtra("taxes",taxes);
                intent.putExtra("customer",custName);
                intent.putExtra("discountamt",discount);
                intent.putExtra("subtotalamt",subtotal);
                intent.putExtra("cust_mobile",customer_MOBILE);
                intent.putExtra("cust_email",customer_EMAIL);
                intent.putExtra("cus_id",custID);
                intent.putExtra("path",file);
                ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                screenShotBitmap.compress(Bitmap.CompressFormat.JPEG, 50, _bs);
                byte[] image = _bs.toByteArray();
                intent.putExtra("invoice",image);
                proceed_to_pay.setVisibility(View.VISIBLE);
                startActivity(intent);
            }
        });


        tv_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                proceed_to_pay.setVisibility(View.GONE);
                tv_pdf.setVisibility(View.GONE);
                screenShotBitmap = getScreenBitmap();
                savetoGallery(screenShotBitmap);
                createPdf(screenShotBitmap);
                proceed_to_pay.setVisibility(View.VISIBLE);
                tv_pdf.setVisibility(View.VISIBLE);

            }
        });

    }

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   public Bitmap getScreenBitmap() {

       ScrollView iv = findViewById(R.id.scroll_view_full);
       iv.setDrawingCacheEnabled(true);
       iv.buildDrawingCache(true);
       Bitmap bitmap = Bitmap.createBitmap(
               iv.getChildAt(0).getWidth(),
               iv.getChildAt(0).getHeight(),
               Bitmap.Config.ARGB_8888);
       Canvas c = new Canvas(bitmap);
       iv.getChildAt(0).draw(c);
       return bitmap;

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
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    private void createPdf(Bitmap bitmap){
        PdfDocument document = new PdfDocument();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;


        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawPaint(paint);
        paint.setColor(Color.BLUE);
        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);


        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Invoice/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        String targetPdf = directory_path+System.currentTimeMillis()+"bill.pdf";
        File filePath = new File(targetPdf);

        try {
            document.writeTo(new FileOutputStream(filePath));
            path = Uri.fromFile(filePath);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(this, "PDF is created!!!", Toast.LENGTH_SHORT).show();

//        openGeneratedPDF();

    }

    private void addItemToCartMethod() {

        dialog.setTitle("Data Loading...");
        dialog.setMessage("Please wait while preparing your invoice....");
        dialog.show();

        for (int i = 0; i < addItemCartArray.size(); i++) {
            for (int j = i + 1; j < addItemCartArray.size(); j++) {
                if (addItemCartArray.get(i).getProductName().equals(addItemCartArray.get(j).getProductName())) {
                    addItemCartArray.get(i).setQuantity(addItemCartArray.get(j).getQuantity());
                    addItemCartArray.get(i).setProductPrice(addItemCartArray.get(j).getProductPrice());
//                    addItemCartArray.get(i).setAmount(ad));
                    addItemCartArray.remove(j);
                    j--;
                    Log.d("remove", String.valueOf(addItemCartArray.size()));

                }
            }
        }


        temparraylist.addAll(addItemCartArray);
        addItemCartAdapter = new AddItemCartAdapter(temparraylist, context,2);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        summary_recyclerview.setLayoutManager(verticalLayoutManager);
        summary_recyclerview.setAdapter(addItemCartAdapter);
        summary_recyclerview.setNestedScrollingEnabled(false);
        addItemCartAdapter.notifyDataSetChanged();

        RecyclerView.OnItemTouchListener disable =  new RecyclerViewDisabler();
        summary_recyclerview.addOnItemTouchListener(disable);
        summary_recyclerview.removeOnItemTouchListener(disable);

        dialog.dismiss();

    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(SummaryActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(SummaryActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(SummaryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(SummaryActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;


            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }

    public class RecyclerViewDisabler implements RecyclerView.OnItemTouchListener {

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return true;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
