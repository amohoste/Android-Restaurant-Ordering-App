package com.example.aggoetey.myapplication.tab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuItem;
import com.example.aggoetey.myapplication.model.Order;
import com.example.aggoetey.myapplication.model.OrderItem;
import com.example.aggoetey.myapplication.model.Tab;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Tab tab = TabSingleton.getInstance().getTab();
        tab.addOrder(new Order(Arrays.asList(
                new OrderItem("notitie", new MenuItem("spaghetti", 13, "tettne", "ca")),
                new OrderItem("notitie", new MenuItem("lasagna", 15, "tettne", "ca"))
        )));
        tab.addOrder(new Order(Arrays.asList(
                new OrderItem("notitie", new MenuItem("spaghetti", 18, "tettne", "ca")),
                new OrderItem("notitie", new MenuItem("lasagna", 15, "tettne", "ca"))
        )));
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.tabRecyclerView);

        List<Order> orders = new ArrayList<>(tab.getOrderedOrders());
        TabAdapter tabAdapter = new TabAdapter(orders);
        recyclerView.setAdapter(tabAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        int prijs = 0;
        for (Order order : orders) {
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
                Log.d("test", String.valueOf(TabSingleton.getInstance().getTab().getOrderedOrders().size()));
                payOrders();
                tabAdapter.notifyDataSetChanged();
                Log.d("test", String.valueOf(TabSingleton.getInstance().getTab().getOrderedOrders().size()));
            }
        });
    }

    private void payOrders() {
        List<Order> orderedOrders = new ArrayList<>(TabSingleton.getInstance().getTab().getOrderedOrders());
            for (Order orderedOrder : orderedOrders) {
                TabSingleton.getInstance().getTab().payOrder(orderedOrder);
            }
        }
}
