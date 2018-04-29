package com.example.aggoetey.myapplication.menu.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.aggoetey.myapplication.model.Menu;
import com.example.aggoetey.myapplication.model.MenuItem;
import com.example.aggoetey.myapplication.model.Restaurant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantMenuLoader {
    private static final String MENU_LOAD_FAILURE = "MENU_LOAD_FAILURE";
    private Restaurant restaurant;
    private DocumentReference mDocRef;


    public RestaurantMenuLoader(Restaurant restaurant) {
    }

    public void loadMenu() {
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Menu menu = new Menu();
                    for (HashMap<String, String> item: (ArrayList<HashMap<String, String>>) documentSnapshot.get("menu")) {
                        menu.addMenuItem(new MenuItem(
                                item.get("name"),
                                Double.parseDouble(item.get("price")),
                                item.get("description"),
                                item.get("category")
                        ));
                    }
                    restaurant.setMenu(menu);
                    Log.d("MENULOADER", "SUCCESS");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(MENU_LOAD_FAILURE, e.toString());
                e.printStackTrace();
            }
        });
    }
}
