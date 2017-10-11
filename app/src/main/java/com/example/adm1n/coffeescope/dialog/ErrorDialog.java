package com.example.adm1n.coffeescope.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.adm1n.coffeescope.R;

/**
 * Created by adm1n on 05.09.2017.
 */

public class ErrorDialog extends DialogFragment {
    private String message;
    private Button positiveButton;
    private Button negativeButton;
    private TextView tvErrorMessage;

    public ErrorDialog() {
    }

    public ErrorDialog(String message) {
        this.message = message;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.error_dialog, null);
        positiveButton = (Button) view.findViewById(R.id.btnDialogYes);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement code
            }
        });
        negativeButton = (Button) view.findViewById(R.id.btnDialogNo);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        tvErrorMessage = (TextView) view.findViewById(R.id.tvDialogBody);
        tvErrorMessage.setText(message);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
