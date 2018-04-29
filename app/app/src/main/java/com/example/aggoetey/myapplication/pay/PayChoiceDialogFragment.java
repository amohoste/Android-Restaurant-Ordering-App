package com.example.aggoetey.myapplication.pay;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.aggoetey.myapplication.R;

public class PayChoiceDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Betalingsmethode")
                .setMessage("Op welke manier wenst u te betalen?")
                .setItems(R.array.pay_options);
        return builder.create();
    }

    public static PayChoiceDialogFragment newInstance() {
        Bundle args = new Bundle();

        PayChoiceDialogFragment fragment = new PayChoiceDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
