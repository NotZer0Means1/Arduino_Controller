package com.example.project;

import android.app.Activity;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.arduino_controller_v2.R;

import java.util.Set;

public class BluetoothActivity extends Activity {
    private static final String LOL = "MAIN_LOL";

    Button bt;
    BluetoothAdapter bluetoothAdapter;
    BroadcastReceiver mReceiver;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        Log.d(LOL, "CJPLFYJ");
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bt = findViewById(R.id.button);


        mReceiver = new BroadcastReceiver(){
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                Log.d(LOL, "nachalo");
                if(BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    Log.d(LOL, "nashel");
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d(LOL, device.getName());
                }
            }
        };
        IntentFilter filter = new IntentFilter();


        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        bluetoothAdapter.startDiscovery();

    }

    public void onDestroy()
    {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
