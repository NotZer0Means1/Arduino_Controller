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
    private static final String MAIN_LOL = "MAIN_LOL";

    Button bt;
    BluetoothAdapter bluetoothAdapter;
    BroadcastReceiver mReceiver;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bt = findViewById(R.id.button);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothAdapter.startDiscovery();
            }
        };
        bt.setOnClickListener(listener);


        mReceiver = new BroadcastReceiver(){
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                Log.d(MAIN_LOL, "nachalo");
                if(BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    Log.d(MAIN_LOL, "nashel");
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d(MAIN_LOL, device.getAddress());
                }
            }
        };
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
        bluetoothAdapter.startDiscovery();
    }

    public void onDestroy()
    {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
