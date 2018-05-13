package com.example.aggoetey.myapplication.pay.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.error_screen.ErrorScreenFragment;
import com.example.aggoetey.myapplication.model.Tab;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayContainerFragment extends Fragment {

    private static final String NO_TABLE = "no_table";
    private static final String NO_RESTAURANT = "no_Restaurant";
    private static final String PAY_FRAGMENT = "pay_fragment";


    public PayContainerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_pay_container, container, false);
        Fragment f;
        String tag;
        Tab t = Tab.getInstance();

        if(Tab.getInstance().getRestaurant() == null){
            // er is nog geen restaurant geselecteerd
            f = ErrorScreenFragment.newInstance(getString(R.string.no_restaurant_selected));
            tag = NO_RESTAURANT;
        } else if (Tab.getInstance().getTable() == null){
            // nog geen tafel
            f = ErrorScreenFragment.newInstance(getString(R.string.no_table_selected));
            tag = NO_TABLE;
        } else {
            // alles in order
            f = PayFragment.newInstance();
            tag = PAY_FRAGMENT;
        }

        getChildFragmentManager().beginTransaction().replace(R.id.container, f)
                .addToBackStack(tag)
                .commit();

        return v;
    }

    public static PayContainerFragment newInstance(){
        return new PayContainerFragment();
    }

}
