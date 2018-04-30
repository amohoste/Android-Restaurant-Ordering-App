package com.example.aggoetey.myapplication.pay.tabfragmentpage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aggoetey.myapplication.Listener;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.pay.TabAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class TabPageFragment extends Fragment implements TabAdapter.OnOrderClickListener, Listener {

    private TabAdapter tabAdapter;
    private RecyclerView recyclerView;
    protected TextView total;

    private int price;

    private OrderSelectedListener orderSelectedListener;
    private android.view.MenuItem payAction;

    public interface OrderSelectedListener {
        void onOrderSelected(Tab.Order order);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        orderSelectedListener = (OrderSelectedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        orderSelectedListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Tab tab = Tab.getInstance();

        View view = inflater.inflate(R.layout.fragment_tab_page, container, false);
        recyclerView = view.findViewById(R.id.tabRecyclerView);
        total = view.findViewById(R.id.total);

        this.invalidated();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        tab.addListener(this);

        return view;
    }

    @Override
    public void onOrderClick(Tab.Order order) {
        orderSelectedListener.onOrderSelected(order);
    }

    @Override
    public void invalidated() {
        setTabAdapter();
        calculatePrice();
    }

    private void calculatePrice() {
        this.price = 0;
        for (Tab.Order order : getOrders()) {
            this.price += order.getPrice();
        }

        if(getOrders().size() != 0) Tab.Order.groupOrders(getOrders().get(0));

        setTotalText(this.price);
    }

    private void setTabAdapter() {
        List<Tab.Order> orders = new ArrayList<>(getOrders());
        tabAdapter = new TabAdapter(orders);
        tabAdapter.setOrderClickListener(this);
        recyclerView.setAdapter(tabAdapter);
    }

    protected abstract List<Tab.Order> getOrders();
    protected abstract void setTotalText(double price);
}
