package com.example.aggoetey.myapplication.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by aggoetey on 3/20/18.
 *
 */

public class Order extends Observable {
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void addOrderItem (OrderItem orderItem){
        this.orderItems.add(orderItem);
        setChanged();
        this.notifyObservers();
    }

    public void removeOrderItem(OrderItem orderItem){
        this.orderItems.remove(orderItem);
        setChanged();
        this.notifyObservers();
    }
}
