package com.example.aggoetey.myapplication;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;

/**
 * Created by Dries on 1/05/2018.
 */

public class ServerConnectionFailure implements OnFailureListener {

    Fragment fragment;
    Toast toast;

    public ServerConnectionFailure(Fragment fragment) {
        this.fragment = fragment;
    }

    public ServerConnectionFailure(Fragment fragment, Toast toast) {
        this.fragment = fragment;
        this.toast = toast;
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (toast != null) {
            toast.cancel();
        }

        Toast.makeText(fragment.getContext(), fragment.getResources()
                .getString(R.string.order_send_failure), Toast.LENGTH_SHORT)
                .show();
    }
}
