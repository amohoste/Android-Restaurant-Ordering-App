package com.example.aggoetey.myapplication.note.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.note.fragments.NotesPageFragment;

/**
 * Created by sitt on 07/05/18.
 */

public class NotesFragmentPagerAdapter extends FragmentPagerAdapter {

    private MenuInfo menuInfo;
    private String[] categories;

    public NotesFragmentPagerAdapter(FragmentManager fm, MenuInfo menuInfo) {
        super(fm);
        this.menuInfo = menuInfo;

        categories = new String[]{"Pending"};

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categories[position];
    }

    @Override
    public Fragment getItem(int position) {
        return NotesPageFragment.newInstance(menuInfo, position);
    }

    @Override
    public int getCount() {
        return categories.length;
    }
}
