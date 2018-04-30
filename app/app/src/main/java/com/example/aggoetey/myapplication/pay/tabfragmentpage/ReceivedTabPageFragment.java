package com.example.aggoetey.myapplication.pay.tabfragmentpage;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Tab;

import java.util.List;

public class ReceivedTabPageFragment extends TabPageFragment {
    @Override
    protected List<Tab.Order> getOrders() {
        return Tab.getInstance().getReceivedOrders();
    }

    @Override
    protected void setTotalText(double price) {
        total.setText(total.getContext().getString(R.string.total_price_received, price));
    }
}
