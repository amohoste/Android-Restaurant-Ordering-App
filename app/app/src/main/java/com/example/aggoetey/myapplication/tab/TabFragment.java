package com.example.aggoetey.myapplication.tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aggoetey.myapplication.orderdetail.OrderDetailActivity;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuItem;
import com.example.aggoetey.myapplication.model.Tab;

import java.util.ArrayList;
import java.util.List;

public class TabFragment extends Fragment implements TabAdapter.OnOrderClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Tab tab = Tab.getInstance();
        tab.beginOrder()
                .addOrderItem("notitie", new MenuItem("spaghetti", 13, "tettne", "ca"))
                .addOrderItem("notitie", new MenuItem("lasagna", 15, "tettne", "ca"))
                .commitOrder();

        tab.beginOrder()
                .addOrderItem("notitie", new MenuItem("spaghetti", 18, "tettne", "ca"))
                .addOrderItem("notitie", new MenuItem("lasagna", 15, "tettne", "ca"))
                .commitOrder();

        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.tabRecyclerView);

        List<Tab.Order> orders = new ArrayList<>(tab.getOrderedOrders());
        TabAdapter tabAdapter = new TabAdapter(orders);
        tabAdapter.setOrderClickListener(this);
        recyclerView.setAdapter(tabAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        int prijs = 0;
        for (Tab.Order order : orders) {
            prijs += order.getPrice();
        }

        TextView total = view.findViewById(R.id.total);
        total.setText(String.valueOf(prijs));

        registerPayButtonListener((Button) view.findViewById(R.id.pay_button), tabAdapter);

        return view;
    }

    private void registerPayButtonListener(Button button, final TabAdapter tabAdapter) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payOrders();
            }
        });
    }

    private void payOrders() {
        List<Tab.Order> orderedOrders = new ArrayList<>(Tab.getInstance().getOrderedOrders());
        for (Tab.Order orderedOrder : orderedOrders) {
            Tab.getInstance().payOrder(orderedOrder);
        }
    }

    @Override
    public void onOrderClick(Tab.Order order) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra(OrderDetailActivity.ORDER_KEY, order);
        startActivity(intent);
    }
}
