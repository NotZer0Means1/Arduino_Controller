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
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.arduino_controller_v2.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends Activity {
    private static final String LOL = "MAIN_LOL";

    Button bt;
    ListView listView;
    BluetoothAdapter bluetoothAdapter;
    private ArrayList<String> mDevices = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private Set<BluetoothDevice> devices;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        Log.d(LOL, "CJPLFYJ");
        bt = findViewById(R.id.button);
        listView = findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if( !bluetoothAdapter.isEnabled() ){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, RESULT_OK);
        }
        devices = bluetoothAdapter.getBondedDevices();
        for ( BluetoothDevice bd: devices ){
            arrayAdapter.add(bd.getName());
        }
        listView.setAdapter(arrayAdapter);

        if ( !bluetoothAdapter.isDiscovering() ){
            bluetoothAdapter.startDiscovery();
        }

        if ( bluetoothAdapter.isDiscovering() ) {
            bluetoothAdapter.cancelDiscovery();
            bluetoothAdapter.startDiscovery();
        }

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);


    }

     public void onDestroy () {
            unregisterReceiver(mReceiver);
            super.onDestroy();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d(LOL, "ресивер");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if ( !mDevices.contains(device.getName()) ) {
                    mDevices.add(device.getName());
                    arrayAdapter.add(device.getName());
                    Log.d(LOL, device.getName());
                }
                listView.setAdapter(arrayAdapter);
            }

        }
    };
}
