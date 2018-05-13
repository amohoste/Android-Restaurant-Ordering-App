package com.example.aggoetey.myapplication.note.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.model.MenuItem;
import com.example.aggoetey.myapplication.note.services.MenuInfoItemNoteSaver;
import com.example.aggoetey.myapplication.note.services.NoteSaver;

/**
 * Created by sitt on 08/04/18.
 * <p>
 * A dialog which will be called upon to add notes to an order.
 */

public class OrderNoteDialogFragment extends DialogFragment {


    private static final String ARG_NOTE_MENU_ITEM_TITLE = "ARG_NOTE_MENU_ITEM_TITLE";
    private static final String ARG_NOTE_SAVER = "ARG_NOTE_SAVER";

    private String itemTitle;
    private NoteSaver noteSaver;

    public OrderNoteDialogFragment() {
    }

    public static OrderNoteDialogFragment newInstance(NoteSaver noteSaver, String itemTitle) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_NOTE_MENU_ITEM_TITLE, itemTitle);
        bundle.putSerializable(ARG_NOTE_SAVER, noteSaver);
        OrderNoteDialogFragment fragment = new OrderNoteDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static OrderNoteDialogFragment newInstance(MenuItem menuItem, MenuInfo info) {
        return newInstance(new MenuInfoItemNoteSaver(info, menuItem, 0), menuItem.title);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setFields();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_order_note_dialog, null);

        final EditText editText = view.findViewById(R.id.note_body_text);

        builder.setTitle("Add notes to " + itemTitle);
        builder.setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noteSaver.saveNote(editText.getText().toString());
                        noteSaver.invalidate();
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

        editText.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;
    }


    private void setFields() {
        if (getArguments() != null) {
            itemTitle = getArguments().getString(ARG_NOTE_MENU_ITEM_TITLE);
            noteSaver = (NoteSaver) getArguments().getSerializable(ARG_NOTE_SAVER);
        }
    }
}
