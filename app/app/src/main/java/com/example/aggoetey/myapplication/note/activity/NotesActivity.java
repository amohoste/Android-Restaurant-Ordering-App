package com.example.aggoetey.myapplication.note.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuInfo;

public class NotesActivity extends AppCompatActivity {

    public static  final String ARG_MENU_INFO = "ARG_MENU_INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        MenuInfo menuInfo =  (MenuInfo) intent.getSerializableExtra(ARG_MENU_INFO);
        Log.e("NotesActivity", "" +  menuInfo.getCurrentOrder().getOrderItems().size());
        setContentView(R.layout.activity_notes);
    }
}
