package com.example.aggoetey.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.aggoetey.myapplication.Menu.MenuItemListActivity;
import com.example.aggoetey.myapplication.loaders.MenuItemLoader;
import com.example.aggoetey.myapplication.model.Menu;

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
                        TextView t = findViewById(R.id.test_text);
                        switch (item.getItemId()){
                            case R.id.action_discover:
                                t.setText(getResources().getText(R.string.discover_text));
                                break;
                            case R.id.action_menu:
                                startMenuListActivity();
                                break;
                            case R.id.action_pay:
                                t.setText(getResources().getText(R.string.pay_text));
                                break;
                        }

                        return true;
                    }
                }
        );
    }

    private void startMenuListActivity() {
        Intent in = new Intent(this, MenuItemListActivity.class);
        startActivity(in);
    }
}
