package com.shipgig.thegun.pos.Printer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Printer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shipgig.thegun.pos.MainActivity;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.fragment.CartFragment;
import com.shipgig.thegun.pos.fragment.NewHomeFragment;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

@SuppressLint("SetTextI18n")
public class PrinterActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, BluetoothHandler.HandlerInterface {

    @BindView(R.id.font_awesome_print_icon)
    TextView print;
//    @BindView(R.id.tv_status_print)
//    TextView tvStatus;
    Button go_to_home;

    private final String TAG = PrinterActivity.class.getSimpleName();
    public static final int RC_BLUETOOTH = 0;
    public static final int RC_CONNECT_DEVICE = 1;
    public static final int RC_ENABLE_BLUETOOTH = 2;

    private BluetoothService mService = null;
    private boolean isPrinterReady = false;
    String noItem, totalAmt,stateGST,centralGST,discount,subtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.print_reciept_dilog_box);
        ButterKnife.bind(this);
//        tvStatus.setVisibility(View.VISIBLE);
        this.setFinishOnTouchOutside(false);
        setupBluetooth();

        go_to_home = findViewById(R.id.go_to_home);


        Intent callingIntent= getIntent();
        noItem= callingIntent.getStringExtra("noofitems");
        totalAmt= callingIntent.getStringExtra("totalamt");
        stateGST= callingIntent.getStringExtra("stategst");
        centralGST= callingIntent.getStringExtra("cgstamt");
        discount= callingIntent.getStringExtra("discountamt");
        subtotal= callingIntent.getStringExtra("subtotalamt");

        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        ImageView backButton = (ImageView)findViewById(R.id.imageView);
        TextView textSelectPayMode = (TextView)findViewById(R.id.text_select_pay_mode);
        TextView totalAmountDialog = (TextView)findViewById(R.id.total_amount_dialog);
        final RadioGroup radioGroupPaymentMode= findViewById(R.id.radio_group_payment_mode);
        totalAmountDialog.setVisibility(View.GONE);
        radioGroupPaymentMode.setVisibility(View.GONE);
        textSelectPayMode.setVisibility(View.GONE);
        print.setTypeface(fontAwesomeFont);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        go_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrinterActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


//        Log.d("reciept",noItem);
//        Log.d("reciept1", totalAmt);

        Glide.with(this).load(R.drawable.check1)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.check1)
                        .fitCenter())
                .into((ImageView)findViewById(R.id.gifimage));

    }

    @AfterPermissionGranted(RC_BLUETOOTH)
    private void setupBluetooth() {
        String[] params = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};
        if (!EasyPermissions.hasPermissions(this, params)) {
            EasyPermissions.requestPermissions(this, "You need bluetooth permission",
                    RC_BLUETOOTH, params);
            return;
        }
        mService = new BluetoothService(this, new BluetoothHandler(this));
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // TODO: 10/11/17 do something
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // TODO: 10/11/17 do something
    }

    @Override
    public void onDeviceConnected() {
        isPrinterReady = true;
//        tvStatus.setText("Device connected! "+"\n"+"Click on print button to print");
    }

    @Override
    public void onDeviceConnecting() {
//        tvStatus.setText("Connecting...");
    }

    @Override
    public void onDeviceConnectionLost() {
//        isPrinterReady = false;
//        tvStatus.setText("The device connection has been lost");
    }

    @Override
    public void onDeviceUnableToConnect() {
//        tvStatus.setText("\n" +
//                "Cannot connect to device");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_ENABLE_BLUETOOTH:
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "onActivityResultthegun: \n" +
                            "active bluetooth ");
                } else
                    Log.i(TAG, "onActivityResultthegun: Bluetooth must be active to use this feature\n");
                break;
            case RC_CONNECT_DEVICE:
                if (resultCode == RESULT_OK) {
                    String address = data.getExtras().getString(DeviceActivity.EXTRA_DEVICE_ADDRESS);
                    BluetoothDevice mDevice = mService.getDevByMac(address);
                    mService.connect(mDevice);
                }
                break;
        }
    }

    @OnClick(R.id.font_awesome_print_icon)
    public void printText(@Nullable View view) {


        if (!mService.isAvailable()) {
            Log.i(TAG, "printText: the device does not support bluetooth\n");
            return;
        }
        if (isPrinterReady) {
            if (noItem.isEmpty()) {
                Toast.makeText(this, "Cant print null text", Toast.LENGTH_SHORT).show();
                return;
            }
            byte[] blinefeed= {0xa};
            byte [] bc={0x1D  , 066 , (byte) 255};
            mService.write(blinefeed);
            mService.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
            mService.write(PrinterCommands.SELECT_FONT_BOLD_LARGE);
            mService.write(PrinterCommands.PRINT_FEED);
            mService.sendMessage(("Shipgig Ventures"+"\n"+"H55 Noida"+"\n"+"India"), String.valueOf(Charset.forName("UTF-8")));
            mService.write(PrinterCommands.ESC_ENTER);
            mService.write(PrinterCommands.CLEAR_BUFFER);


            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.write(String.valueOf(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date())).getBytes());
            mService.write(PrinterCommands.NEW_LINE);

            mService.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
            mService.sendMessage("- - - - - - - - - -", "");
            mService.write(PrinterCommands.NEW_LINE);
            //mService.write(PrinterCommands.FEED_LINE);

            mService.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
            mService.sendMessage("No of Items"+"           "+noItem,"");
            mService.write(PrinterCommands.NEW_LINE);

//            mService.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
//            mService.sendMessage("Total Amount"+"          "+totalAmt, "");
//            mService.write(PrinterCommands.ESC_ENTER);

            mService.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
            mService.sendMessage("GST Amount"+"            "+stateGST, "");
            mService.write(PrinterCommands.ESC_ENTER);

            mService.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
            mService.sendMessage("SGST Amount"+"           "+centralGST, "");
            mService.write(PrinterCommands.ESC_ENTER);

            if (discount != null){
                mService.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
                mService.sendMessage("Discount"+"           "+discount, "");
                mService.write(PrinterCommands.ESC_ENTER);
            }

            if (subtotal != null){
                mService.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
                mService.sendMessage("Total Amount"+"           "+subtotal, "");
                mService.write(PrinterCommands.ESC_ENTER);
            }

            mService.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
            mService.write(PrinterCommands.SELECT_FONT_BOLD_LARGE);
            mService.sendMessage("Paid Amount"+"           "+totalAmt, "");
            mService.write(PrinterCommands.ESC_ENTER);
            mService.write(PrinterCommands.CLEAR_BUFFER);



           /* mService.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
            mService.sendMessage("- - - - - - - - - -", "");
            mService.write(PrinterCommands.ESC_ENTER);
*/
        } else {
            if (mService.isBTopen())
                startActivityForResult(new Intent(this, DeviceActivity.class), RC_CONNECT_DEVICE);
            else
                requestBluetooth();
        }
    }




    private void requestBluetooth() {
        if (mService != null) {
            if (!mService.isBTopen()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, RC_ENABLE_BLUETOOTH);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
