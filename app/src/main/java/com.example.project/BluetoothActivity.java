package com.example.project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BlendMode;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.arduino_controller_v2.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends Activity {
    private static final String MAIN_LOL = "MAIN_LOL";

    private Button bt_search;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayAdapter<String> arrayAdapter2;
    private ArrayList devices;
    private Set<BluetoothDevice> bDevices;
    private View header1;
    private View header2;
    private View footer1;
    private View footer2;
    //for bt
    private BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mReceiver;

    //_________________________________________________________________________________________________________________________________________________________
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_search = (Button) findViewById(R.id.button);
        listView = (ListView) findViewById(R.id.listView);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        devices = new ArrayList();
        header1 = createHeader("Подключенные устройства");
        header2 = createHeader("Доступные устройства");
        footer1 = createFooter("");
        footer2 = createFooter("");
        //=================================================================================================================================================
        if( !mBluetoothAdapter.isEnabled() ){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, RESULT_OK);
        }
        bDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bD: bDevices){
            arrayAdapter.add(bD.getName());
            arrayAdapter2.add(bD.getName());
            Log.d(MAIN_LOL, "conjugate " + bD.getName());
        }
        fillList1();
        //=================================================================================================================================================
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String device_name = (String) listView.getItemAtPosition(position);
                Log.d(MAIN_LOL, "Choice device: " + device_name);
            }
        });
        //=================================================================================================================================================
        View.OnClickListener listener = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                checkPermissionLocation();
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                }
                mBluetoothAdapter.startDiscovery();
            }
        };
        bt_search.setOnClickListener(listener);
        //=================================================================================================================================================
        mReceiver = new BroadcastReceiver(){
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                Log.d(MAIN_LOL, "searches");
                if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                    Log.d(MAIN_LOL, "searching device");
                    Toast toast = Toast.makeText(getApplicationContext(), "Поиск устройств", Toast.LENGTH_SHORT);
                    toast.show();
                    BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d(MAIN_LOL, String.valueOf(device.getName()));
                    if ( !devices.contains(device.getName()) && device.getName() != null) {
                        devices.add(device.getName());
                        arrayAdapter.add(device.getName());
                        listView.addHeaderView(header2, arrayAdapter, false);
                        listView.addFooterView(footer2, arrayAdapter, false);
                        listView.setAdapter(arrayAdapter);
                    }
                }
                if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
                {
                    Log.d(MAIN_LOL, "start searching device");
                    Toast toast = Toast.makeText(getApplicationContext(), "Начал поиск устройств", Toast.LENGTH_SHORT);
                    toast.show();
                }
                if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    Log.d(MAIN_LOL, "finished searching device");
                    Toast toast = Toast.makeText(getApplicationContext(), "Закончил поиск устройств", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };
        //=================================================================================================================================================
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver, filter);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
        //=================================================================================================================================================
    }
    //_________________________________________________________________________________________________________________________________________________________
    public void onDestroy()
    {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
    //_________________________________________________________________________________________________________________________________________________________
    View createHeader(String text) {
        View v = getLayoutInflater().inflate(R.layout.header, null);
        ((TextView)v.findViewById(R.id.Text)).setText(text);
        return v;
    }
    //_________________________________________________________________________________________________________________________________________________________
    View createFooter(String text) {
        View v = getLayoutInflater().inflate(R.layout.footer, null);
        ((TextView)v.findViewById(R.id.Text)).setText(text);
        return v;
    }
    //_________________________________________________________________________________________________________________________________________________________
    void fillList1() {
        listView.addHeaderView(header1, arrayAdapter2, false);
        listView.addFooterView(footer1, arrayAdapter2, false);
        listView.setAdapter(arrayAdapter2);
    }
    //_________________________________________________________________________________________________________________________________________________________
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionLocation() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int check = checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            check += checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");

            if (check != 0) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1002);
            }
        }
    }
//_________________________________________________________________________________________________________________________________________________________
}

