package com.example.aggoetey.myapplication.adt;

/**
 * Created by aggoetey on 3/20/18.
 *
 * Een MenuItem is iets dat je op je menu ziet verschijnen.
 * Hier vind je info over de prijs, de naam, korte inhoud.
 */

public class MenuItem {
    // Titel van het gerecht
    public final String title;
    // prijs van het gerecht
    public final int price;
    // een korte uitleg over het gerecht
    public final String description;

    public MenuItem(String title, int price, String description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }
}
