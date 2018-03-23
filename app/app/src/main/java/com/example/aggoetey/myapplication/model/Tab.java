package com.example.aggoetey.myapplication.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aggoetey on 3/20/18.
 *
 * Een tab is een lijst met orders die nog betaald moeten worden door een bepaalde tafel.
 */

public class Tab {
    private List<Order> orderList = new ArrayList<>();

    public List<Order> getOrderList() {
        return orderList;
    }
}
