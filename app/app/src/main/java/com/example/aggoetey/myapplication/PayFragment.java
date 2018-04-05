package com.example.aggoetey.myapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.orderdetail.NoOrderSelectedFragment;
import com.example.aggoetey.myapplication.orderdetail.OrderDetailActivity;
import com.example.aggoetey.myapplication.orderdetail.OrderDetailFragment;
import com.example.aggoetey.myapplication.tab.TabFragment;

public class PayFragment extends Fragment  {

    public PayFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.pay_fragment, parent, false);

        getChildFragmentManager().beginTransaction().replace(R.id.tab_fragment_container, TabFragment.newInstance()).commit();
        if (v.findViewById(R.id.order_detail_fragment_container) != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.order_detail_fragment_container, NoOrderSelectedFragment.newInstance())
                    .commit();
        }

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static PayFragment newInstance() {
        return new PayFragment();
    }



}
