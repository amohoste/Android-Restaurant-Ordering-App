package com.example.aggoetey.myapplication.menu.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.aggoetey.myapplication.menu.fragments.MenuPageFragment;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.model.ViewType;

/**
 * Created by Dries on 6/04/2018.
 * Adapter voor de menu tabs (drinks, food...).
 */

public class MenuFragmentPagerAdapter extends FragmentPagerAdapter implements MenuPageFragment.MenuViewStateListener {

    private String tabTitles[];
    private MenuInfo menuInfo;
    private static ViewType viewType;
    public MenuFragmentPagerAdapter(FragmentManager fm, MenuInfo menuInfo, ViewType viewType) {
        super(fm);
        tabTitles = Tab.getInstance().getRestaurant().getMenu().getCategories().toArray(
                new String[Tab.getInstance().getRestaurant().getMenu().getCategories().size()]);
        this.menuInfo = menuInfo;
        Log.d("MENU_LOAD", Tab.getInstance().getRestaurant().getMenu().toString() + "lol");
        MenuFragmentPagerAdapter.viewType = viewType;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("PagerAdapter", "getItem : "  + position);
        return MenuPageFragment.newInstance(position + 1, getPageTitle(position).toString(), menuInfo, this);
    }

    /**
     * This method is called when notifydatasetchanged() is called.
     * Default value is POSITION_UNCHANGED which won't call getItem(int position) method to update the views.
     * Overriding it with POSITION_NONE will refresh all fragments of the viewpager.
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
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

    @Override
    public ViewType currentViewIsGrid() {
        return viewType;

    }

    @Override
    public void updateViewType(ViewType viewType) {
        MenuFragmentPagerAdapter.viewType = viewType;
    }
}
