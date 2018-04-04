package com.example.aggoetey.myapplication.orderdetail;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.aggoetey.myapplication.R;

public class OrderDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        FragmentManager manager = getSupportFragmentManager();
        OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
        manager.beginTransaction().replace(R.id.order_fragment, orderDetailFragment).commit();

        // fragment zijn argumenten meegeven
        orderDetailFragment.setArguments(getIntent().getExtras());

    }
}
