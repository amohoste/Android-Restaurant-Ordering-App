package com.example.aggoetey.myapplication.orderdetail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggoetey.myapplication.R;


public class NoOrderSelectedFragment extends Fragment {


    public NoOrderSelectedFragment() {
        // Required empty public constructor
    }

    public static NoOrderSelectedFragment newInstance() {
        return new NoOrderSelectedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_order_selected, container, false);
    }

}
