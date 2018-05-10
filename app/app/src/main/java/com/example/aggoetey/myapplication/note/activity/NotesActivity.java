package com.example.aggoetey.myapplication.note.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.note.adapters.NotesFragmentPagerAdapter;

public class NotesActivity extends AppCompatActivity {

    public static  final String ARG_MENU_INFO = "ARG_MENU_INFO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        MenuInfo menuInfo =  (MenuInfo) intent.getSerializableExtra(ARG_MENU_INFO);
        Log.d("NotesActivity", "" +  menuInfo.getCurrentOrder().getOrderItems().size());
        setContentView(R.layout.activity_notes);

        setupViewPager(menuInfo);
    }


    private void setupViewPager(MenuInfo menuInfo) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = findViewById(R.id.notes_view_pager);
        NotesFragmentPagerAdapter pagerAdapter = new NotesFragmentPagerAdapter(getSupportFragmentManager(), menuInfo);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.notes_sliding_tab);
        tabLayout.setupWithViewPager(viewPager,true);
    }
}
