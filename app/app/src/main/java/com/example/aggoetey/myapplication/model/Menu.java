package com.example.aggoetey.myapplication.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aggoetey on 3/20/18.
 *
 * Dit is een model voor een Menu.
 */

public class Menu {
    private ArrayList<MenuItem> menuItemList = new ArrayList<>();

    /**
     * Voeg een MenuItem toe aan het menu
     */
    public void addMenuItem(MenuItem menuItem){
        menuItemList.add(menuItem);
    }

    public ArrayList<MenuItem> getMenuItemList() {
        return menuItemList;
    }
}
