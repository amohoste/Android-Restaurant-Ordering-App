package com.example.aggoetey.myapplication.menu.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.aggoetey.myapplication.model.Menu;
import com.example.aggoetey.myapplication.model.MenuItem;
import com.example.aggoetey.myapplication.model.Restaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantMenuLoader {
    private static final String MENU_LOAD = "MENU_LOAD";
    private static final String MENU_LOAD_SUCCESS = "MENU_LOAD_SUCCESS";
    private Restaurant restaurant;
    private CollectionReference mColRef;


    public RestaurantMenuLoader(Restaurant restaurant) {
        this.restaurant = restaurant;
        mColRef = FirebaseFirestore.getInstance().collection("places/"
                                        .concat(restaurant.getGooglePlaceId()).concat("/menus"));
        loadMenu();
    }

    public void loadMenu() {
        mColRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Menu menu = new Menu();
                    for (QueryDocumentSnapshot item: task.getResult()) {
                        menu.addMenuItem(new MenuItem(
                                (String) item.get("name"),
                                Double.parseDouble((String) item.get("price")),
                                (String) item.get("description"),
                                (String) item.get("category")
                        ));
                    }
                    restaurant.setMenu(menu);
                    Log.d(MENU_LOAD, MENU_LOAD_SUCCESS);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(MENU_LOAD, e.toString());
                e.printStackTrace();
            }
        });
    }
}
