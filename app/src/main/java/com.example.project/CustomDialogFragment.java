package com.example.project;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.arduino_controller_v2.R;

import java.util.Objects;

public class CustomDialogFragment extends AppCompatDialogFragment {
    String name;
    private EditText editText;
    private DialogListener listener;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);

        builder.setView(view)
                .setTitle( "Диалоговое окно" )
                .setIcon( android.R.drawable.ic_dialog_alert )
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name = editText.getText().toString();
                        listener.applyTexts(name);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        editText = view.findViewById(R.id.nameOfController);

        return builder.create();

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "должен определять интерфейс DialogListener");
        }
    }

    public interface DialogListener{
        void applyTexts(String name);
    }

}
