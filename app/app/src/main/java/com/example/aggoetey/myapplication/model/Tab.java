package com.example.aggoetey.myapplication.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by aggoetey on 3/20/18.
 *
 * Een tab is een lijst met orders die nog betaald moeten worden door een bepaalde tafel.
 */

public class Tab {
    private Set<Order> payedOrders = new HashSet<>();
    private Set<Order> orderedOrders = new HashSet<>();

    public Set<Order> getPayedOrders() {
        return payedOrders;
    }

    public Set<Order> getOrderedOrders() {
        return orderedOrders;
    }

    public void addOrder(Order order){
        orderedOrders.add(order);
    }

    public void payOrder(Order order){
        orderedOrders.remove(order);
        payedOrders.add(order);
    }
}
