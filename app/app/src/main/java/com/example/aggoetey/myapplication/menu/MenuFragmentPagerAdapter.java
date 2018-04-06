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

    private String tabTitles[];
    private MenuInfo menuInfo;

    public MenuFragmentPagerAdapter(FragmentManager fm, MenuInfo menuInfo) {
        super(fm);
        tabTitles = menuInfo.getRestaurant().getMenu().getCategories().toArray(
                new String[menuInfo.getRestaurant().getMenu().getCategories().size()]);
        this.menuInfo = menuInfo;
    }

    @Override
    public Fragment getItem(int position) {
        MenuPageFragment menuPageFragment = MenuPageFragment.newInstance(position + 1, getPageTitle(position).toString(), menuInfo);
        return menuPageFragment;
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
