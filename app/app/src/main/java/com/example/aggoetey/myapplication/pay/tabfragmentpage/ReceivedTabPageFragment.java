package com.example.aggoetey.myapplication.pay.tabfragmentpage;

import com.example.aggoetey.myapplication.model.Tab;

import java.util.List;

public class ReceivedTabPageFragment extends TabPageFragment {
    @Override
    protected List<Tab.Order> getOrders() {
        return Tab.getInstance().getReceivedOrders();
    }
}
