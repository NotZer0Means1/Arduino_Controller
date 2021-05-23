package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.arduino_controller_v2.R;

public class MainActivity extends AppCompatActivity implements CustomDialogFragment.DialogListener{
    private static final String LOL = "LOL";

    Button create;
    Spinner spinner;
    private ArrayAdapter<String> remoteControllers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create = (Button) findViewById(R.id.create);
        spinner = (Spinner) findViewById(R.id.spinner);
        remoteControllers = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        remoteControllers.setDropDownViewResource(android.R.layout.simple_spinner_item);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(v);
                Log.d(LOL, "Переход на активность создания пульта");
                Intent intent = new Intent(getApplicationContext(), ConstructorActivity.class);
                startActivity(intent);
            }
        };
        //spinner.setAdapter(remoteControllers);
        create.setOnClickListener(listener);
        spinner.setAdapter(remoteControllers);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) spinner.getItemAtPosition(position);
                Log.d(LOL, "Выбран элемент: " + item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Отображение диалогового окна
    public void showDialog(View v) {
        CustomDialogFragment dialog = new CustomDialogFragment();
        dialog.show(getSupportFragmentManager(), "custom");

    }

    @Override
    public void applyTexts(String name) {
        Log.d(LOL, name);
        remoteControllers.add(name);
    }
}