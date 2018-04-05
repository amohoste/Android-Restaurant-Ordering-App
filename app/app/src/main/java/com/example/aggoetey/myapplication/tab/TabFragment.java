package com.example.aggoetey.myapplication.tab;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aggoetey.myapplication.Listener;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Tab;

import java.util.ArrayList;
import java.util.List;

public class TabFragment extends Fragment implements TabAdapter.OnOrderClickListener, Listener {

    TabAdapter tabAdapter;
    RecyclerView recyclerView;
    TextView total;

    private Callbacks callbacks;

    public interface Callbacks {
        void onOrderSelected(Tab.Order order);
    }

    @Override
    public void onAttach(Activity act){
        super.onAttach(act);
        callbacks = (Callbacks) act;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        callbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Tab tab = Tab.getInstance();

        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        recyclerView = view.findViewById(R.id.tabRecyclerView);
        total = view.findViewById(R.id.total);

        setTabAdapter();
        calculatePrice();

        registerPayButtonListener((Button) view.findViewById(R.id.pay_button), tabAdapter);
        tab.addListener(this);

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
        callbacks.onOrderSelected(order);
    }

    @Override
    public void invalidated() {
        setTabAdapter();
        calculatePrice();
    }

    private void calculatePrice() {
        int prijs = 0;
        for (Tab.Order order : Tab.getInstance().getOrderedOrders()) {
            prijs += order.getPrice();
        }

        total.setText(total.getContext().getString(R.string.total_price, prijs));
    }

    private void setTabAdapter() {
        Tab tab = Tab.getInstance();
        List<Tab.Order> orders = new ArrayList<>(tab.getOrderedOrders());
        tabAdapter = new TabAdapter(orders);
        tabAdapter.setOrderClickListener(this);
        recyclerView.setAdapter(tabAdapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        tabAdapter.notifyDataSetChanged();

    }

    public static TabFragment newInstance(){
        return new TabFragment();
    }
}
