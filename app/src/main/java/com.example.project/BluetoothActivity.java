package com.example.project;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.UUID;
import androidx.annotation.RequiresApi;
import com.example.arduino_controller_v2.R;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothActivity extends Activity {
    private static final String MAIN_LOL = "MAIN_LOL";

    Method method;

    private Button bt_search;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapterSavedName;
    private ArrayAdapter<String> arrayAdapterAvailableName;
    private ArrayList<String> arrayOfSavedAdress;
    private ArrayList<String> arrayOfAvailableAdress;
    private ArrayList devices;
    private Set<BluetoothDevice> bDevices;
    private Handler h;
    //for bt
    private BluetoothAdapter mBluetoothAdapter;
    private  BluetoothSocket btSocket;
    private BroadcastReceiver mReceiver;
    // for chat between devices
    private static final int REQUEST_ENABLE_BT = 1;
    final int RECIEVE_MESSAGE = 1;        // Статус для Handler
    private StringBuilder sb = new StringBuilder();
    private static String mac_adress;
    //private static final UUID myUUID = UUID.fromString("5fe57aa0-b1a6-11eb-8529-0242ac130003");
    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String device_adress;

    private BluetoothThread bluetoothThread;

    //_________________________________________________________________________________________________________________________________________________________
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        bt_search = (Button) findViewById(R.id.button);
        listView = (ListView) findViewById(R.id.listView);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        arrayAdapterSavedName = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        arrayAdapterAvailableName = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        arrayOfSavedAdress = new ArrayList<>();
        arrayOfAvailableAdress = new ArrayList<>();
        devices = new ArrayList();
        mac_adress = mBluetoothAdapter.getAddress(); // mac-adress нашего устройства
        //=================================================================================================================================================
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE:                                                   // если приняли сообщение в Handler
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);
                        sb.append(strIncom);                                                // формируем строку
                        int endOfLineIndex = sb.indexOf("\r\n");                            // определяем символы конца строки
                        if (endOfLineIndex > 0) {                                            // если встречаем конец строки,
                            String sbprint = sb.substring(0, endOfLineIndex);               // то извлекаем строку
                            sb.delete(0, sb.length());                                      // и очищаем sb
                        }
                        Log.d(MAIN_LOL, "...Строка:"+ sb.toString() +  "Байт:" + msg.arg1 + "...");
                        break;
                }
            };
        };
        //=================================================================================================================================================
        checkBTState(); // проверка работоспособности bluetooth
        //=================================================================================================================================================
        bDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bD: bDevices){
            arrayAdapterSavedName.add(bD.getName());
            arrayOfSavedAdress.add(bD.getAddress());
            Log.d(MAIN_LOL, "conjugate " + bD.getName() + " adress " + bD.getAddress());
        }
        listView.setAdapter(arrayAdapterSavedName);
        //=================================================================================================================================================
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String device_name = (String) listView.getItemAtPosition(position);
                Log.d(MAIN_LOL, "Choice device: " + device_name);
                if(position >= arrayOfSavedAdress.size()){
                    device_adress = arrayOfAvailableAdress.get(position - arrayOfSavedAdress.size());
                    Log.d(MAIN_LOL, "index: " + device_adress);
                }
                else{
                    device_adress = arrayOfSavedAdress.get(position);
                    Log.d(MAIN_LOL, "index: " + device_adress);
                }
                BluetoothDevice chosenDevice = mBluetoothAdapter.getRemoteDevice(device_adress);

                BluetoothConnectThread bluetoothConnectThread = new BluetoothConnectThread(chosenDevice);
                bluetoothConnectThread.start();
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
                    //Log.d(MAIN_LOL, String.valueOf(device.getName()));
                    if ( !devices.contains(device.getName()) && device.getName() != null) {
                        devices.add(device.getName());
                        arrayAdapterSavedName.add(device.getName());
                        arrayOfAvailableAdress.add(device.getAddress());
                        Log.d(MAIN_LOL, "conjugate " + device.getName() + " adress " + device.getAddress());
                        listView.setAdapter(arrayAdapterSavedName);
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
    public void onDestroy() {
        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onDestroy() and failed to close socket." + e2.getMessage() + ".");
        }
        unregisterReceiver(mReceiver);
        super.onDestroy();
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
    //Здесь был onResume() здоровья погибшим
    //_________________________________________________________________________________________________________________________________________________________
    @Override
    public void onPause() {
        super.onPause();

        Log.d(MAIN_LOL, "...In onPause()...");

    }
    //_________________________________________________________________________________________________________________________________________________________
    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(mBluetoothAdapter==null) {
            errorExit("Fatal Error", "Bluetooth не поддерживается");
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                Log.d(MAIN_LOL, "...Bluetooth включен...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }
    //_________________________________________________________________________________________________________________________________________________________
    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }
    //_________________________________________________________________________________________________________________________________________________________
    private class BluetoothConnectThread extends Thread{
        private BluetoothConnectThread(BluetoothDevice device) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
            try {
                method = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
                btSocket = (BluetoothSocket) method.invoke(device, 1);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() { // Коннект
            boolean success = false;
            try {
                btSocket.connect();
                success = true;
            }

            catch (IOException e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(BluetoothActivity.this, "Нет коннекта, проверьте Bluetooth-устройство с которым хотите соединица!", Toast.LENGTH_LONG).show();
                        Log.d(MAIN_LOL, "НЕТ КОННЕКТА ЧТО ТЫ ХОЧЕШЬ");
                        //listViewPairedDevice.setVisibility(View.VISIBLE);
                    }
                });

                try {
                    btSocket.close();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if(success) {  // Если законнектились, тогда открываем панель с кнопками и запускаем поток приёма и отправки данных
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(MAIN_LOL, "ЕСТЬ КОННЕКТ ЧТО ТЫ ХОЧЕШЬ");
                    }
                });

                //BluetoothThread = new BluetoothThread(bluetoothSocket);
                //BluetoothThread.start(); // запуск потока приёма и отправки данных
            }
        }

        public void cancel() {
            Toast.makeText(getApplicationContext(), "Close - BluetoothSocket", Toast.LENGTH_LONG).show();
            try {
                btSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //_________________________________________________________________________________________________________________________________________________________
    private class BluetoothThread extends Thread{
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        final int RECIEVE_MESSAGE = 1;

        public BluetoothThread(BluetoothSocket socket) {
            btSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);        // Получаем кол-во байт и само собщение в байтовый массив "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Отправляем в очередь сообщений Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            Log.d(MAIN_LOL, "...Данные для отправки: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(MAIN_LOL, "...Ошибка отправки данных: " + e.getMessage() + "...");
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                btSocket.close();
            } catch (IOException e) { }
        }
    }
}


