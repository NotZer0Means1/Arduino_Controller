package com.example.project;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.arduino_controller_v2.R;

public class ConstructorActivity extends AppCompatActivity {

    private static final String LOL = "Main_LOL";
    private int elem = 1;
    Button btCreator, txtCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constructor);
        txtCreator = findViewById(R.id.textView_creator);
        btCreator = findViewById(R.id.button_creator);


        LinearLayout linLay = findViewById(R.id.linearLayout2);
        int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;


        // создаем LayoutParams
        LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(wrapContent, wrapContent);

        LinearLayout.LayoutParams leftMarginParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        leftMarginParams.leftMargin = 500;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = new Button(getApplicationContext());
                btn.setText("Button");
                linLay.addView(btn, lParams);
                setContentView(linLay, linLayoutParam);
            }
        };
        btCreator.setOnClickListener(listener);

        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = new TextView(getApplicationContext());
                txt.setText("текстовое поле");
                txt.setLayoutParams(lpView);
                linLay.addView(txt, lParams);
                setContentView(linLay, linLayoutParam);

            }
        };
        txtCreator.setOnClickListener(listener1);
        /**/

        /*Button btn2 = new Button(this);
                 btn2.setText("Button2");
                 linLayout.addView(btn2, lParams);
                 */
        /*TextView txt = new TextView(this);
                */
        /**/

    }




}
