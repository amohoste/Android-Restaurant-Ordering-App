package com.example.aggoetey.myapplication.menu.fragments;

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
import com.example.aggoetey.myapplication.menu.model.MenuInfo;
import com.example.aggoetey.myapplication.model.MenuItem;
import com.example.aggoetey.myapplication.model.Tab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sitt on 08/04/18.
 *
 * A dialog which will be called upon to add notes to an order.
 *
 * TODO: Allow user to choose number of notes.
 * Currently only adds one note to the chosen item in the current batch order.
 */

public class OrderNoteDialogFragment extends DialogFragment {


    private static final String ARG_NOTE_MENU_ITEM = "ARG_NOTE_MENU_ITEM";
    private static final String ARG_NOTE_MENU_INFO = "ARG_NOTE_MENU_INFO";

    private MenuItem menuItem;
    private MenuInfo menuInfo;

    public OrderNoteDialogFragment() {
    }


    public static OrderNoteDialogFragment newInstance(MenuItem menuItem, MenuInfo info) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_MENU_INFO, info);
        args.putSerializable(ARG_NOTE_MENU_ITEM, menuItem);
        OrderNoteDialogFragment fragment = new OrderNoteDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setFields();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_order_note_dialog, null);

        final EditText editText = view.findViewById(R.id.note_body_text);
        builder.setTitle("Add notes to your order.");
        builder.setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNoteToOrder(editText.getText().toString());
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

    private void saveNoteToOrder(String note) {
        List<Tab.Order.OrderItem> list = menuInfo.getCurrentOrder().getOrderItems();
        List<Tab.Order.OrderItem> result = new ArrayList<>();
        for(Tab.Order.OrderItem orderItem :  list){
            if(orderItem.getMenuItem().id.equals(menuItem.id)) {
                result.add(orderItem);
            }
        }

        //TODO: CHANGE THIS TO HANDLE NOTE SAVING ON MULTIPLE ORDER ITEMS WITH DROPDOWN LIST IN DIALOG
        result.get(0).setNote(note);
    }

    private void setFields() {
        menuItem = (MenuItem) getArguments().getSerializable(ARG_NOTE_MENU_ITEM);
        menuInfo = (MenuInfo) getArguments().getSerializable(ARG_NOTE_MENU_INFO);

    }
}
