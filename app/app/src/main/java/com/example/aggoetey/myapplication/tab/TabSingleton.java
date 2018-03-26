package com.example.aggoetey.myapplication.tab;

import com.example.aggoetey.myapplication.model.Tab;

/**
 * Created by aggoetey on 3/26/18.
 */

class TabSingleton {
    private static final TabSingleton ourInstance = new TabSingleton();

    private final Tab tab = new Tab();

    static TabSingleton getInstance() {
        return ourInstance;
    }

    private TabSingleton() {
    }

    public Tab getTab() {
        return tab;
    }
}
