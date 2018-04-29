package com.example.aggoetey.myapplication.pay.orderdetail;

import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.pay.TabPageFragment;

import java.util.List;

public class OrderedTabPageFragment extends TabPageFragment{

    @Override
    protected List<Tab.Order> getOrders(){

        return Tab.getInstance().getOrderedOrders();
    }

}
