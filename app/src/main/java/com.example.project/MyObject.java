package com.example.project;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.LinearLayout;

public class MyObject implements Parcelable {

    final static String LOG_TAG = "myLogs";

    public Object LinearLAYOUT;

    // обычный конструктор
    public MyObject(Object l ) {
        Log.d(LOG_TAG, "MyObject(Object l)");
        LinearLAYOUT = l;
    }

    public int describeContents() {
        return 0;
    }

    // упаковываем объект в Parcel
    public void writeToParcel(Parcel parcel, int flags) {
        Log.d(LOG_TAG, "writeToParcel");
        parcel.writeValue(LinearLAYOUT);
    }

    public static final Parcelable.Creator<MyObject> CREATOR = new Parcelable.Creator<MyObject>() {
        // распаковываем объект из Parcel
        public MyObject createFromParcel(Parcel in) {
            Log.d(LOG_TAG, "createFromParcel");
            return new MyObject(in);
        }

        public MyObject[] newArray(int size) {
            return new MyObject[size];
        }
    };

    // конструктор, считывающий данные из Parcel
    private MyObject(Parcel parcel) {
        Log.d(LOG_TAG, "MyObject(Parcel parcel)");
        LinearLAYOUT = parcel.readValue(getClass().getClassLoader());
    }

}
