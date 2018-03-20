package com.example.aggoetey.myapplication.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aggoetey on 3/20/18.
 *
 * Een order bevat een lijst van OrderItems die op hetzelfde moment zijn besteld en een tafel waarvan de order kwam.
 */

public class Order {
    private List<OrderItem> orderItems = new ArrayList<>();
    private Table table;

    public Order(Table table) {
        this.table = table;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
