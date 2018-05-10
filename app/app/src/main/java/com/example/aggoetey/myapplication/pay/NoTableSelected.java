package com.example.aggoetey.myapplication.pay;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggoetey.myapplication.R;

public class NoTableSelected extends Fragment {

    public NoTableSelected() {
        // Required empty public constructor
    }

    public static NoTableSelected newInstance() {
        return new NoTableSelected();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_table_selected, container, false);
    }

}
