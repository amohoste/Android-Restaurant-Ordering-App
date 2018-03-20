package com.example.aggoetey.myapplication.model;

/**
 * Created by aggoetey on 3/20/18.
 *
 * Een orderItem bevat een notitie naar de ober en menuItem waarnaar het wijst
 */

public class OrderItem {
    private String note;
    private MenuItem menuItem;

    public OrderItem(String note, MenuItem menuItem) {
        this.note = note;
        this.menuItem = menuItem;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }
}


