package com.example.aggoetey.myapplication.menu.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.menu.views.MenuCardViewInitializer;
import com.example.aggoetey.myapplication.menu.model.MenuInfo;
import com.example.aggoetey.myapplication.model.MenuItem;

import java.io.Serializable;

/**
 * Created by sitt on 09/04/18.
 */

public class MenuCardInfoDialogFragment extends DialogFragment implements MenuCardViewInitializer.OnAddNoteButtonClickListener {


    public interface CardDialogClickListener extends Serializable {
        void onInteraction(int pos);
    }

    private static final String ARG_CARD_MENU_LISTENER = "ARG_CARD_MENU_LISTENER";
    private static final String ARG_CARD_MENU_INFO = "ARG_CARD_MENU_INFO";
    private static final String ARG_CARD_MENU_ITEM = "ARG_CARD_MENU_ITEM";
    private static final String ARG_CARD_MENU_POS = "ARG_CARD_MENU_POS";


    private MenuItem menuItem;
    private MenuInfo menuInfo;
    private int menuItemPos;
    private CardDialogClickListener listener;

    public MenuCardInfoDialogFragment() {
    }

    public static MenuCardInfoDialogFragment newInstance(MenuItem menuItem, MenuInfo info,int position ,CardDialogClickListener listener) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CARD_MENU_INFO, info);
        args.putSerializable(ARG_CARD_MENU_LISTENER, listener);
        args.putSerializable(ARG_CARD_MENU_ITEM, menuItem);
        args.putInt(ARG_CARD_MENU_POS, position);
        MenuCardInfoDialogFragment fragment = new MenuCardInfoDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        menuInfo = (MenuInfo) getArguments().getSerializable(ARG_CARD_MENU_INFO);
        menuItem = (MenuItem) getArguments().getSerializable(ARG_CARD_MENU_ITEM);
        listener = (CardDialogClickListener) getArguments().getSerializable(ARG_CARD_MENU_LISTENER);
        menuItemPos = getArguments().getInt(ARG_CARD_MENU_POS);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_menu_card_item, null);


        builder.setView(view)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onInteraction(menuItemPos );
                        dialog.dismiss();
                    }
                });


        new MenuCardViewInitializer(menuInfo, this)
                .intializeView(view)
                .updateView(menuItem);

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.onInteraction(menuItemPos);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        listener.onInteraction(menuItemPos);
    }

    @Override
    public void onAddNoteButtonClick(MenuItem item) {
        showDialog(OrderNoteDialogFragment.newInstance(item, menuInfo));
    }

    private void showDialog(DialogFragment fragment) {
        fragment.show(this.getFragmentManager(), this.toString());
    }

}
