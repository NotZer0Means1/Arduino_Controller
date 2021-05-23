package com.example.project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.arduino_controller_v2.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Pult extends Activity {


    private static final String MAIN_LOL = "LOL";
    private BluetoothSocket btSocket;
    private Handler h;
    final int RECIEVE_MESSAGE = 1;        // Статус для Handler
    private StringBuilder sb = new StringBuilder();
    private Button bt1, bt2, end;
    private TextView txt1;
    private EditText etxt;
    private String message;
    private BluetoothThread thread;
    private String sbprint;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pult);

        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        end = findViewById(R.id.end);

        txt1 = findViewById(R.id.text1);

        etxt = findViewById(R.id.etext1);

        thread = new BluetoothThread(btSocket);
        thread.start();

         h = new Handler() {
            @SuppressLint("HandlerLeak")
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE:                                                   // если приняли сообщение в Handler
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);
                        sb.append(strIncom);                                                // формируем строку
                        int endOfLineIndex = sb.indexOf("\r\n");                            // определяем символы конца строки
                        if (endOfLineIndex > 0) {                                            // если встречаем конец строки,
                            sbprint = sb.substring(0, endOfLineIndex);               // то извлекаем строку
                            sb.delete(0, sb.length());                                      // и очищаем sb
                        }
                        Log.d(MAIN_LOL, "...Строка:" + sbprint + "Байт:" + msg.arg1 + "...");
                        break;
                }
            }
        };

        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt1.setText(sbprint);
            }
        };
        bt1.setOnClickListener(listener1);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = etxt.getText().toString();
                thread.write(message);
            }
        };
        bt2.setOnClickListener(listener);

        View.OnClickListener listenerEnd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.cancel();
            }
        };
        end.setOnClickListener(listenerEnd);
    }


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
