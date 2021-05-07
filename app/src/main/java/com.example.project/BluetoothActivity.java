package com.example.project;

import android.app.Activity;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.arduino_controller_v2.R;

import java.util.Set;

public class BluetoothActivity extends Activity {
    private static final String MAIN_LOL = "MAIN_LOL";
    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter bluetoothAdapter;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    BroadcastReceiver mReceiver;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        listView = (ListView) findViewById(R.id.listView);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        Log.d(MAIN_LOL, "HAHAHAHAHHA");

        mReceiver = new BroadcastReceiver(){
            public void onReceive(Context context, Intent intent){
                String action = intent.getAction();
                // Когда найдено новое устройство
                Log.d(MAIN_LOL, "nashel");
                if(BluetoothDevice.ACTION_FOUND.equals(action)){
                    // Получаем объект BluetoothDevice из интента
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    //Добавляем имя и адрес в array adapter, чтобы показвать в ListView
                    arrayAdapter.add(device.getName()+"\n"+ device.getAddress());
                    Log.d(MAIN_LOL, "nashel");
                    listView.setAdapter(arrayAdapter);
                }
            }
        };

/*

        if (bluetoothAdapter.isEnabled()) {
            // bluetooth включен
            Log.d(MAIN_LOL,  String.valueOf(bluetoothAdapter.startDiscovery()));
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if(pairedDevices.size() > 0){
                for(BluetoothDevice device: pairedDevices){
                    arrayAdapter.add(device.getName());
                }
                listView.setAdapter(arrayAdapter);
            }
        }
        else
        {
            // Предложение включить bluetooth
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
*/
        // Регистрируем BroadcastReceiver
        if (bluetoothAdapter.isDiscovering()){
            Log.d(MAIN_LOL, String.valueOf(bluetoothAdapter.isDiscovering()));
            bluetoothAdapter.cancelDiscovery();
        }
        Log.d(MAIN_LOL, String.valueOf(bluetoothAdapter.startDiscovery()));
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);// Не забудьте снять регистрацию в onDestroy
        listView.setAdapter(arrayAdapter);
    }

    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
