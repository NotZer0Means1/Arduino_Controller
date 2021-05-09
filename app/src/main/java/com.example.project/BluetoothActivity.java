package com.example.project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BlendMode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

    private static int lastClickId = -1;


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



        if ( bluetoothAdapter.isDiscovering() ) {
            bluetoothAdapter.cancelDiscovery();
            bluetoothAdapter.startDiscovery();
        }


        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);

        if ( !bluetoothAdapter.isDiscovering() ){
            bluetoothAdapter.startDiscovery();
        }
    }

     public void onDestroy () {
            unregisterReceiver(mReceiver);
            super.onDestroy();
    }


    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if( lastClickId != -1 && lastClickId != position ){
                parent.getChildAt(lastClickId).setBackgroundResource(R.color.white);
                view.setBackgroundResource(R.color.white);
            }
            if ( lastClickId == -1 ){
                view.setBackgroundResource(R.color.white);
            }
            lastClickId = position;
        }

    };


    public static int getCurrentSelectedItemId(){
        return lastClickId;
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if ( BluetoothDevice.ACTION_FOUND.equals(action) ) {
                Log.d(LOL, "ресивер");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if ( !mDevices.contains(device.getName()) && device.getName() != null) {
                    mDevices.add(device.getName());
                    arrayAdapter.add(device.getName());
                    Log.d(LOL, device.getName());
                }
                listView.setAdapter(arrayAdapter);
            }
        }
    };
}
