package com.example.aggoetey.myapplication.menu.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import com.example.aggoetey.myapplication.menu.views.MenuCardView;
import com.example.aggoetey.myapplication.menu.model.MenuInfo;
import com.example.aggoetey.myapplication.model.MenuItem;

import java.io.Serializable;

/**
 * Created by sitt on 09/04/18.
 * 
 * A menu card dialog which will open up when there is a hold click on menu items during the list view. 
 * Notes can be added to the menu which can be viewed in the payment tab. 
 * 
 */

public class MenuCardInfoDialogFragment extends DialogFragment {


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

        MenuCardView view =  new MenuCardView(getContext(), menuInfo, new MenuCardView.OnAddNoteButtonClickListener() {
            @Override
            public void onAddNoteButtonClick(MenuItem data) {
                showDialog(OrderNoteDialogFragment.newInstance(data, menuInfo));
            }
        });

        view.bind(menuItem);
    
        builder.setView(view)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onInteraction(menuItemPos );
                        dialog.dismiss();
                    }
                });



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



    private void showDialog(DialogFragment fragment) {
        fragment.show(this.getFragmentManager(), this.toString());
    }

}
