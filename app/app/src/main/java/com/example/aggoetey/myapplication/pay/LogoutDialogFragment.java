package com.example.aggoetey.myapplication.pay;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.aggoetey.myapplication.R;

public class LogoutDialogFragment extends DialogFragment {
    private LogoutChoiceListener logoutChoiceListener;

    public interface LogoutChoiceListener{
        /**
         * @param choice is er positief geantwoord op de vraag of ze willen uitloggen
         */
        void onLogoutChoiceSelection(boolean choice);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            logoutChoiceListener = (LogoutChoiceListener) getParentFragment();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        logoutChoiceListener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.logout_confirmation)
                .setNegativeButton(R.string.oke, (dialogInterface, i) -> logoutChoiceListener.onLogoutChoiceSelection(true))
                .setPositiveButton(R.string.cancel, (dialogInterface, i) -> logoutChoiceListener.onLogoutChoiceSelection(false));
        return builder.create();
    }

    public static LogoutDialogFragment newInstance(){
        return new LogoutDialogFragment();
    }

}
