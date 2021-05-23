package com.example.project;

import android.app.Activity;
import android.os.Bundle;

import com.example.arduino_controller_v2.R;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DataBase extends Activity {
    private Button bt_new;
    private Button bt_old;
    private EditText txt_login;
    private EditText txt_password;
    private static final String MAIN_LOL = "MAIN_LOL";
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";
    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_LOGIN = 1;
    private static final int NUM_COLUMN_PASSWORD = 2;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        openHelper helper = new openHelper(this);
        sqLiteDatabase = helper.getWritableDatabase();
        bt_old = (Button) findViewById(R.id.oldbt);
        bt_new = (Button) findViewById(R.id.newbt);
        txt_login = (EditText) findViewById(R.id.loginText);
        txt_password = (EditText) findViewById(R.id.passwordText);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            switch (v.getId()){
                case R.id.oldbt:
                    String LoginEnt = "";
                    String PasswordEnt = "";
                    String CursorLoginEnt = "";
                    String CursorPasswordEnt = "";
                    LoginEnt = txt_login.getText().toString();
                    PasswordEnt = txt_password.getText().toString();
                    Cursor CursorEnt = sqLiteDatabase.query(TABLE_NAME, null, COLUMN_LOGIN + " = '" + LoginEnt + "'", null, null, null, null);
                    CursorEnt.moveToFirst();
                    if( CursorEnt != null && CursorEnt.moveToFirst() ){
                        CursorLoginEnt = CursorEnt.getString(NUM_COLUMN_LOGIN);
                        CursorPasswordEnt = CursorEnt.getString(NUM_COLUMN_PASSWORD);
                        CursorEnt.close();
                    }
                    if(LoginEnt.equals(CursorLoginEnt) && PasswordEnt.equals(CursorPasswordEnt)) {
                        Intent intent = new Intent(DataBase.this, BluetoothActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Неправильный логин или пароль", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    break;
                    case R.id.newbt:
                        Reg:
                        {
                            String LoginReg = "";
                            String PasswordReg = "";
                            String CursorLoginReg = "";
                            String CursorPasswordReg = "";
                            if (txt_login.getText().toString().trim().length() == 0) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Пустой логин", Toast.LENGTH_LONG);
                                toast.show();
                                break Reg;
                            } else {
                                LoginReg = txt_login.getText().toString();
                            }
                            if (txt_password.getText().toString().trim().length() == 0) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Пустой пароль", Toast.LENGTH_LONG);
                                toast.show();
                                break Reg;
                            } else {
                                PasswordReg = txt_password.getText().toString();
                            }
                            Cursor CursorReg = sqLiteDatabase.query(TABLE_NAME, null, COLUMN_LOGIN + " = '" + LoginReg + "'", null, null, null, null);
                            CursorReg.moveToFirst();
                            if (CursorReg != null && CursorReg.moveToFirst()) {
                                CursorLoginReg = CursorReg.getString(NUM_COLUMN_LOGIN);
                                CursorPasswordReg = CursorReg.getString(NUM_COLUMN_PASSWORD);
                                CursorReg.close();
                            }
                            if (!LoginReg.equals(CursorLoginReg)) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(COLUMN_LOGIN, LoginReg);
                                contentValues.put(COLUMN_PASSWORD, PasswordReg);
                                sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Пользователь зарегистрирован", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                        break;
            }
            }
        };
        bt_old.setOnClickListener(listener);
        bt_new.setOnClickListener(listener);
    }


    private class openHelper extends SQLiteOpenHelper {

        openHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_LOGIN + " TEXT, " + COLUMN_PASSWORD + " TEXT);";
            sqLiteDatabase.execSQL(query);
        }
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }

    }
}
