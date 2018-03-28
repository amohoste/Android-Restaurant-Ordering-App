package com.example.aggoetey.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.orderdetail.OrderDetailActivity;
import com.example.aggoetey.myapplication.tab.TabAdapter;
import com.example.aggoetey.myapplication.tab.TabFragment;

import com.example.aggoetey.myapplication.loaders.MenuItemLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableBottomNavigation();

    }

    /**
     * Listeners enablen van de bottomnavigationview
     */
    private void enableBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentManager manager = getSupportFragmentManager();
                        TabFragment tabFragment = new TabFragment();
                        switch (item.getItemId()) {
                            case R.id.action_discover:
                                break;
                            case R.id.action_menu:
                                manager.beginTransaction().remove(manager.findFragmentById(R.id.fragment_place)).commit();
                                break;
                            case R.id.action_pay:
                                manager.beginTransaction().replace(R.id.fragment_place, tabFragment).commit();
                                break;
                        }

                        return true;
                    }
                }
        );
    }

}
