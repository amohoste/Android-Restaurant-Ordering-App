package com.example.aggoetey.myapplication.orderdetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Tab;

import org.w3c.dom.Text;

public class OrderDetailActivity extends AppCompatActivity {

    public static final String ORDER_KEY = "order";

    private TextView mOrderNr;
    private TextView mPrice;
    private RecyclerView mOrderItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        mOrderNr = findViewById(R.id.order_nr);
        mPrice = findViewById(R.id.price);
        mOrderItems = findViewById(R.id.orderItems);

        Tab.Order order = (Tab.Order) (getIntent().getExtras().getSerializable(ORDER_KEY));

        mOrderNr.setText(String.valueOf(order.getOrderNumber()));
        mPrice.setText(String.valueOf(order.getPrice()));

        mOrderItems.setAdapter(new OrderItemAdapter(order));
        mOrderItems.setLayoutManager(new LinearLayoutManager(this));


    }
}
