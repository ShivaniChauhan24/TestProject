package com.shipgig.thegun.pos.Printer;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zj.btsdk.BluetoothService;

/**
 * Created by afroz on 9/15/17.
 */

public class BluetoothHandler extends Handler {

    private static final String TAG = BluetoothHandler.class.getSimpleName();
    private HandlerInterface thegun;

    public BluetoothHandler(HandlerInterface thegun) {
        this.thegun = thegun;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case BluetoothService.MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                    case BluetoothService.STATE_CONNECTED:
                        thegun.onDeviceConnected();
                        break;
                    case BluetoothService.STATE_CONNECTING:
                        thegun.onDeviceConnecting();
                        break;
                    case BluetoothService.STATE_LISTEN:
                    case BluetoothService.STATE_NONE:
                        Log.i(TAG, "Bluetooth state listen or none");
                        break;
                }
                break;
            case BluetoothService.MESSAGE_CONNECTION_LOST:
                thegun.onDeviceConnectionLost();
                break;
            case BluetoothService.MESSAGE_UNABLE_CONNECT:
                thegun.onDeviceUnableToConnect();
                break;
        }
    }

    public interface HandlerInterface {

        void onDeviceConnected();

        void onDeviceConnecting();

        void onDeviceConnectionLost();

        void onDeviceUnableToConnect();
    }
}
