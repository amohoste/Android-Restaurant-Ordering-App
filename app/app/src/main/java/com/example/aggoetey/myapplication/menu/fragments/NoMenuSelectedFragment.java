package com.example.aggoetey.myapplication.menu.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggoetey.myapplication.R;

/**
 * Show this fragment instead of the menu if no restaurant is selected.
 */

public class NoMenuSelectedFragment extends Fragment {
    public NoMenuSelectedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * @return A new instance of fragment NoMenuSelectedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoMenuSelectedFragment newInstance() {
        NoMenuSelectedFragment fragment = new NoMenuSelectedFragment();
        return new NoMenuSelectedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_menu_selected, container, false);
    }

}
