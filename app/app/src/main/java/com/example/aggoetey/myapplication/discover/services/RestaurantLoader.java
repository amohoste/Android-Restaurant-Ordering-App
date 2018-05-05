package com.example.aggoetey.myapplication.discover.services;


import com.example.aggoetey.myapplication.model.Restaurant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 *
 */
public class RestaurantLoader {

    private CollectionReference mColRef = FirebaseFirestore.getInstance().collection("places");
    private ArrayList<Restaurant> list = new ArrayList<>();

    public RestaurantLoader() {
        mColRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot document: queryDocumentSnapshots.getDocuments()) {
                        list.add(
                                new Restaurant(
                                    document.getString("name"),  // Restaurant name
                                    document.getId()                // Restaurant google place id
                        ));
                    }
                }
            }
        });
    }

    public ArrayList<Restaurant> getRestaurants() {
        return list;
    }

}
