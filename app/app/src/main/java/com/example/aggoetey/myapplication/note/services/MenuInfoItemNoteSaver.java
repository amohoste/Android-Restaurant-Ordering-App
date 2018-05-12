package com.example.aggoetey.myapplication.note.services;

import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.model.MenuItem;
import com.example.aggoetey.myapplication.model.Tab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sitt on 10/05/18.
 */

public class MenuInfoItemNoteSaver implements NoteSaver {

    private MenuInfo menuInfo;
    private MenuItem menuItem;
    private int itemPos;

    public MenuInfoItemNoteSaver(MenuInfo menuInfo, MenuItem menuItem, int itemPos) {
        this.menuInfo = menuInfo;
        this.menuItem = menuItem;
        this.itemPos = itemPos;
    }

    @Override
    public void saveNote(String note) {
        List<Tab.Order.OrderItem> list = menuInfo.getCurrentOrder().getOrderItems();
        List<Tab.Order.OrderItem> result = new ArrayList<>();
        for(Tab.Order.OrderItem orderItem :  list){
            if(orderItem.getMenuItem().id.equals(menuItem.id)) {
                result.add(orderItem);
            }
        }

        result.get(this.itemPos).setNote(note);
    }

    @Override
    public void invalidate() {
    }
}
