package com.example.aggoetey.myapplication.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.aggoetey.myapplication.R;

import java.io.Serializable;

/**
 * Created by sitt on 08/04/18.
 */

public class OrderNoteDialogFragment extends DialogFragment {
    public interface  OnNoteButtonClickListener  extends Serializable{
        void onConfirmClick(String editText);
    }

    public static  final String ARG_NOTE_BUTTON_LISTENER = "ARG_NOTE_BUTTON_LISTENER";

    private OnNoteButtonClickListener listener;

    public OrderNoteDialogFragment() {
    }


    public static OrderNoteDialogFragment newInstance(OnNoteButtonClickListener listener) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_BUTTON_LISTENER, listener);
        OrderNoteDialogFragment fragment =  new OrderNoteDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // listener = (OnNoteButtonClickListener) getArguments().getSerializable(ARG_NOTE_BUTTON_LISTENER);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_order_note_dialog, null);
        builder.setTitle("Add notes to your order.");
        builder.setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = view.findViewById(R.id.note_body_text);
                        listener.onConfirmClick(editText.toString());
                        Log.i("Note is: ", editText.getText().toString());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

}
