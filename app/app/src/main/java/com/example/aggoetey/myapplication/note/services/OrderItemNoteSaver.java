package com.example.aggoetey.myapplication.note.services;

import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;

import com.example.aggoetey.myapplication.model.Tab;

/**
 * Created by sitt on 10/05/18.
 */

public class OrderItemNoteSaver implements NoteSaver {
    private Tab.Order.OrderItem orderItem;
    private  RecyclerView.Adapter adapter;

    public OrderItemNoteSaver(Tab.Order.OrderItem orderItem, RecyclerView.Adapter adapter) {
        this.orderItem = orderItem;
        this.adapter = adapter;
    }

    @Override
    public void saveNote(String note) {
        orderItem.setNote(note);
    }

    @Override
    public void invalidate() {
        adapter.notifyDataSetChanged();
    }
}
