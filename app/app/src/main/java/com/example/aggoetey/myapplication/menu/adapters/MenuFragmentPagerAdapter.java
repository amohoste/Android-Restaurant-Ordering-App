package com.example.aggoetey.myapplication.menu.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.aggoetey.myapplication.menu.model.MenuInfo;
import com.example.aggoetey.myapplication.menu.fragments.MenuPageFragment;

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
    public int getItemPosition(Object object) {
        MenuPageFragment fragment = (MenuPageFragment) object;
        Log.e("Pager adapter", "Fragment isGridView: " + fragment.isInstanceIsGridView());
        Log.e("Pager adapter",  "Fragment global gridview: " + MenuPageFragment.isIsGridView());
        if(fragment.isInstanceIsGridView() != MenuPageFragment.isIsGridView()) {
            // Refresh fragment if it's view type is different from the global setting.
            fragment.getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
        }

        return super.getItemPosition(object);
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
