package com.example.aggoetey.myapplication.menu.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.aggoetey.myapplication.menu.fragments.MenuFragment;
import com.example.aggoetey.myapplication.model.Menu;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.model.MenuItem;
import com.example.aggoetey.myapplication.model.Tab;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class RestaurantMenuLoader {
    private static final String MENU_LOAD = "MENU_LOAD";
    private static final String MENU_LOAD_SUCCESS = "MENU_LOAD_SUCCESS";
    private static final String MENU_LOAD_FETCHING = "MENU_LOAD_FETCHING";
    private MenuFragment menuFragment;
    private MenuInfo menuInfo;
    private CollectionReference mColRef;


    public RestaurantMenuLoader(MenuInfo menuInfo, MenuFragment menuFragment) {
        this.menuFragment = menuFragment;
        this.menuInfo = menuInfo;
        mColRef = FirebaseFirestore.getInstance().collection("places")
                        .document(Tab.getInstance().getRestaurant().getGooglePlaceId()).collection("menus");
        loadMenu();
    }

    public void loadMenu() {
        Log.d(MENU_LOAD, MENU_LOAD_FETCHING);
        mColRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Menu menu = new Menu();
                    for (QueryDocumentSnapshot item: task.getResult()) {
                        menu.addMenuItem(new MenuItem(
                                (String) item.get("title"),
                                Double.parseDouble((String) item.get("price")),
                                (String) item.get("description"),
                                (String) item.get("category")
                        ));
                    }
                    Tab.getInstance().getRestaurant().setMenu(menu);
                    Log.d(MENU_LOAD, MENU_LOAD_SUCCESS);
                    menuFragment.setupViewPager();
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
