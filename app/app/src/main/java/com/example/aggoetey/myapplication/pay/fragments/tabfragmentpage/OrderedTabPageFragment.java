package com.example.aggoetey.myapplication.pay.fragments.tabfragmentpage;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Tab;

import java.util.List;

public class OrderedTabPageFragment extends TabPageFragment{

    @Override
    protected List<Tab.Order> getOrders(){
        return Tab.getInstance().getOrderedOrders();
    }

    @Override
    protected void setTotalText(double price) {
        total.setText(total.getContext().getString(R.string.total_price_ordered, price));
    }

}
