package com.example.aggoetey.myapplication.pay;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.pay.tabfragmentpage.OrderedTabPageFragment;
import com.example.aggoetey.myapplication.pay.tabfragmentpage.PayedTabPageFragment;
import com.example.aggoetey.myapplication.pay.tabfragmentpage.ReceivedTabPageFragment;
import com.example.aggoetey.myapplication.pay.tabfragmentpage.TabPageFragment;

public class TabFragment extends Fragment {

    private ViewPager mViewPager;
    private TabPageFragmentAdapter mTabPageFragmentAdapter;

    public TabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        mTabPageFragmentAdapter = new TabPageFragmentAdapter(getChildFragmentManager());
        mViewPager = view.findViewById(R.id.tab_page_viewpager);
        mViewPager.setAdapter(mTabPageFragmentAdapter);

        TabLayout tabs = view.findViewById(R.id.tab_page_tabs);
        tabs.setupWithViewPager(mViewPager);

        return view;
    }


    public static TabFragment newInstance() {
        return new TabFragment();
    }


    public static class TabPageFragmentAdapter extends FragmentPagerAdapter {
        public enum Division {
            ORDERED("Ordered"),
            RECEIVED("Received"),
            PAYED("Payed");

            private final String title;

            Division(String title){
                this.title = title;
            }

            public String getTitle() {
                return title;
            }
        }

        public TabPageFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public TabPageFragment getItem(int i) {
            Division d = Division.values()[i];
            TabPageFragment fragment = null;
            switch (d) {
                case PAYED:
                    fragment = new PayedTabPageFragment();
                    break;
                case ORDERED:
                    fragment = new OrderedTabPageFragment();
                    break;
                case RECEIVED:
                    fragment = new ReceivedTabPageFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return Division.values().length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return Division.values()[position].getTitle();
        }
    }


}
