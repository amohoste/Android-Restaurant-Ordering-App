package com.example.aggoetey.myapplication.menu;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.TreeSet;

/**
 * Created by Dries on 6/04/2018.
 */

public class MenuFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private String tabTitles[];
    private MenuFragment menuFragment;

    public MenuFragmentPagerAdapter(FragmentManager fm, Context context, TreeSet<String> categories, MenuFragment menuFragment) {
        super(fm);
        this.context = context;
        tabTitles = categories.toArray(new String[categories.size()]);
        this.menuFragment = menuFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return MenuPageFragment.newInstance(position + 1, getPageTitle(position).toString(), menuFragment);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
