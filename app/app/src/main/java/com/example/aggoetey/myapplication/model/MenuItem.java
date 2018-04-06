package com.example.aggoetey.myapplication.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by aggoetey on 3/20/18.
 *
 * Een MenuItem is iets dat je op je menu ziet verschijnen.
 * Hier vind je info over de prijs, de naam, korte inhoud.
 */

public class MenuItem implements Serializable {
    // Titel van het gerecht
    public final String title;
    // prijs van het gerecht
    public final int price;
    // een korte uitleg over het gerecht
    public final String description;

    public String id;

    /**
     * Een categorie is hier gewoon een string. Eventueel later nog een klasse van maken.
     * Door een string te gebruiken versimpelt er vanalles.
     */
    public final String category;

    public MenuItem(String title, int price, String description, String category) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.id = UUID.randomUUID().toString();
    }
}
