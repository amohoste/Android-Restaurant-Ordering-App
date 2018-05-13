package com.example.aggoetey.myapplication.pay.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.example.aggoetey.myapplication.R;

public class PayChoiceDialogFragment extends DialogFragment {

    private PayChoiceListener payChoiceListener;

    public interface PayChoiceListener {
        void onPayChoiceSelection(int i);
    }

    @Override
    public void onCreate(Bundle b) {
        onAttachToParentFragment(getParentFragment());
        super.onCreate(b);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.paychoicedialog_title)
                .setItems(R.array.pay_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        payChoiceListener.onPayChoiceSelection(i);
                    }
                });
        return builder.create();
    }

    public static PayChoiceDialogFragment newInstance() {
        Bundle args = new Bundle();

        PayChoiceDialogFragment fragment = new PayChoiceDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttachToParentFragment(Fragment fragment) {
        try {
            payChoiceListener = (PayChoiceListener) fragment;
        } catch (ClassCastException c) {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }
}
