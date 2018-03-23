package com.example.aggoetey.myapplication.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aggoetey on 3/20/18.
 *
 */

public class Order {
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void addOrderItem (OrderItem orderItem){
        this.orderItems.add(orderItem);
    }
}
