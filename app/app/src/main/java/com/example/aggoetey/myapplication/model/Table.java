package com.example.aggoetey.myapplication.model;

import java.io.Serializable;

/**
 * Created by aggoetey on 3/20/18.
 *
 * Een tafel verwijst enkel naar een tafel in een restaurant
 */

public class Table implements Serializable{

    // bv tafel 13, tafel aan het raam, ronde tafel
    private String nickName;
    private Tab tab;

    public Table(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Tab getTab() {
        return tab;
    }
}
