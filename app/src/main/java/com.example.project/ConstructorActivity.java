package com.example.project;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.arduino_controller_v2.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ConstructorActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOL = "Main_LOL";
    private android.widget.LinearLayout.LayoutParams layoutParams;
    private LinearLayout container;
    private LinearLayout llMain;
    private RadioGroup Creators;
    private EditText Name;
    private Button btnCreate, btnClear, saveBt;
    private int xDelta, yDelta;
    private int flag = 0;
    private View view;
    private int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
    private DocumentBuilderFactory dbf;
    private DocumentBuilder db;
    private Document doc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constructor);

        llMain = findViewById(R.id.llMain);
        Creators = findViewById(R.id.Creators);
        Name = findViewById(R.id.Name);

        saveBt = findViewById(R.id.saveButton);
        saveBt.setOnClickListener(this);
        btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);
        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
    }


        @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId", "RtlHardcoded"})
        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnCreate:
                        // Создание LayoutParams c шириной и высотой по содержимому
                        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(wrapContent, wrapContent);
                        // переменная для хранения значения выравнивания
                        int btnGravity = Gravity.LEFT;

                        switch (Creators.getCheckedRadioButtonId()) {
                            case R.id.crButton:

                                btnGravity = Gravity.LEFT;
                                Button btnNew = new Button(this);
                                btnNew.setText(String.valueOf(Name.getText()));
                                llMain.addView(btnNew, lParams);


                                btnNew.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        final int x = (int) event.getRawX();
                                        final int y = (int) event.getRawY();
                                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                                            case MotionEvent.ACTION_DOWN: {
                                                LinearLayout.LayoutParams laParams = (LinearLayout.LayoutParams) v.getLayoutParams();

                                                xDelta = x - laParams.leftMargin;
                                                yDelta = y - laParams.topMargin;
                                                break;
                                            }
                                            case MotionEvent.ACTION_UP: {
                                                Toast.makeText(getApplicationContext(), "Объект пермещен", Toast.LENGTH_SHORT).show();
                                                break;
                                            }
                                            case MotionEvent.ACTION_MOVE: {
                                                LinearLayout.LayoutParams layoutParams =
                                                        (LinearLayout.LayoutParams) v.getLayoutParams();
                                                layoutParams.leftMargin = x - xDelta;
                                                layoutParams.topMargin = y - yDelta;
                                                layoutParams.rightMargin = 0;
                                                layoutParams.bottomMargin = 0;
                                                v.setLayoutParams(layoutParams);
                                                view = v;
                                                break;
                                            }
                                        }
                                        return true;
                                    }
                                });

                                break;


                            case R.id.crTextView:
                                btnGravity = Gravity.LEFT;
                                TextView txt = new TextView(this);
                                txt.setHint(String.valueOf(Name.getText()));
                                llMain.addView(txt, lParams);

                                txt.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        final int x = (int) event.getRawX();
                                        final int y = (int) event.getRawY();
                                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                                            case MotionEvent.ACTION_DOWN: {
                                                LinearLayout.LayoutParams laParams = (LinearLayout.LayoutParams) v.getLayoutParams();

                                                xDelta = x - laParams.leftMargin;
                                                yDelta = y - laParams.topMargin;
                                                break;
                                            }
                                            case MotionEvent.ACTION_UP: {
                                                Toast.makeText(getApplicationContext(), "Объект пермещен", Toast.LENGTH_SHORT).show();
                                                break;
                                            }
                                            case MotionEvent.ACTION_MOVE: {
                                                LinearLayout.LayoutParams layoutParams =
                                                        (LinearLayout.LayoutParams) v.getLayoutParams();
                                                layoutParams.leftMargin = x - xDelta;
                                                layoutParams.topMargin = y - yDelta;
                                                layoutParams.rightMargin = 0;
                                                layoutParams.bottomMargin = 0;
                                                v.setLayoutParams(layoutParams);
                                                view = v;
                                                break;
                                            }
                                        }
                                        return true;
                                    }
                                });

                                break;
                            case R.id.crEditText:
                                btnGravity = Gravity.LEFT;
                                EditText etxt = new EditText(this);
                                etxt.setHint(String.valueOf(Name.getText()));
                                llMain.addView(etxt, lParams);

                                etxt.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        final int x = (int) event.getRawX();
                                        final int y = (int) event.getRawY();
                                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                                            case MotionEvent.ACTION_DOWN: {
                                                LinearLayout.LayoutParams laParams = (LinearLayout.LayoutParams) v.getLayoutParams();

                                                xDelta = x - laParams.leftMargin;
                                                yDelta = y - laParams.topMargin;
                                                break;
                                            }
                                            case MotionEvent.ACTION_UP: {
                                                Toast.makeText(getApplicationContext(), "Объект пермещен", Toast.LENGTH_SHORT).show();
                                                break;
                                            }
                                            case MotionEvent.ACTION_MOVE: {
                                                LinearLayout.LayoutParams layoutParams =
                                                        (LinearLayout.LayoutParams) v.getLayoutParams();
                                                layoutParams.leftMargin = x - xDelta;
                                                layoutParams.topMargin = y - yDelta;
                                                layoutParams.rightMargin = 0;
                                                layoutParams.bottomMargin = 0;
                                                v.setLayoutParams(layoutParams);
                                                view = v;
                                                break;
                                            }
                                        }
                                        return true;
                                    }
                                });
                                break;
                        }
                        // переносим полученное значение выравнивания в LayoutParams
                        lParams.gravity = btnGravity;
                        break;

                    case R.id.btnClear: {
                        llMain.removeAllViews();
                        Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.saveButton: {
                        MyObject myObj = new MyObject(llMain);
                        Intent intent = new Intent(this, Pult.class);
                        intent.putExtra(MyObject.class.getCanonicalName(), myObj);
                        startActivity(intent);
                    }

                }
            };

}

