package com.example.aggoetey.myapplication.pay;


import android.os.Bundle;
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

        return view;
    }

    public static TabFragment newInstance() {
        return new TabFragment();
    }


    public class TabPageFragmentAdapter extends FragmentPagerAdapter {
        public TabPageFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public TabPageFragment getItem(int i) {
            TabPageFragment fragment = null;
            if(i == 0){
                fragment = new OrderedTabPageFragment();
            } else if (i == 1){
                fragment = new ReceivedTabPageFragment();
            } else {
                fragment = new PayedTabPageFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }


}
