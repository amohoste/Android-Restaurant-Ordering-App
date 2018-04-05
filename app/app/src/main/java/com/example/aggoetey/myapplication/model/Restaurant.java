package com.example.aggoetey.myapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aggoetey on 3/20/18.
 *
 * Een restaurant model.
 */

public class Restaurant implements Serializable {
    private String title;
    private Menu menu;
    private List<Table> tables = new ArrayList<>();

    public Restaurant(String title, Menu menu) {
        this.title = title;
        this.menu = menu;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public void addTable(Table table){
        this.tables.add(table);
    }
}
