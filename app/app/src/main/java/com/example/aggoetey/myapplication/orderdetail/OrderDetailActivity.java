package com.example.aggoetey.myapplication.orderdetail;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Tab;

import static com.example.aggoetey.myapplication.orderdetail.OrderDetailFragment.ORDER_KEY;

public class OrderDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        FragmentManager manager = getSupportFragmentManager();
        OrderDetailFragment orderDetailFragment = OrderDetailFragment.newInstance((Tab.Order) getIntent().getSerializableExtra(ORDER_KEY));
        manager.beginTransaction().replace(R.id.order_fragment, orderDetailFragment).commit();
    }
}
