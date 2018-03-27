package com.example.aggoetey.myapplication.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by aggoetey on 3/20/18.
 * <p>
 * Een tab is een lijst met orders die nog betaald moeten worden door een bepaalde tafel.
 */

public class Tab extends Observable {

    private static final Tab ourInstance = new Tab();

    private List<Order> payedOrders = new ArrayList<>();
    private List<Order> orderedOrders = new ArrayList<>();

    public List<Order> getPayedOrders() {
        return payedOrders;
    }

    public List<Order> getOrderedOrders() {
        return orderedOrders;
    }

    private Tab() {
    }

    static Tab getInstance() {
        return ourInstance;
    }


    public void addOrder(Order order) {
        orderedOrders.add(order);
        this.setChanged();
        this.notifyObservers();
    }

    public void payOrder(Order order) {
        orderedOrders.remove(order);
        payedOrders.add(order);
        this.setChanged();
        this.notifyObservers();
    }
}
