package com.example.aggoetey.myapplication.pay.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aggoetey.myapplication.Listener;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.pay.fragments.tabfragmentpage.OrderedTabPageFragment;
import com.example.aggoetey.myapplication.pay.fragments.tabfragmentpage.PayedTabPageFragment;
import com.example.aggoetey.myapplication.pay.fragments.tabfragmentpage.ReceivedTabPageFragment;
import com.example.aggoetey.myapplication.pay.fragments.tabfragmentpage.TabPageFragment;

public class TabFragment extends Fragment implements PayChoiceDialogFragment.PayChoiceListener, LogoutDialogFragment.LogoutChoiceListener, Listener {

    private ViewPager mViewPager;
    private TabPageFragmentAdapter mTabPageFragmentAdapter;
    private MenuItem pay_action;
    private MenuItem logout_action;
    private TabLayout mTabLayout;
    private ActionBar mActionBar;

    private LogoutListener logoutListener;

    private static final String PAY_CHOICE_DIALOG_FRAGMENT_TAG = "PayChoiceDialogFragmentTag";
    private static final String LOGOUT_CHOICE_DIALOG_FRAGMENT_TAG = "LOGOUT";

    public TabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            logoutListener = (LogoutListener) getActivity();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        logoutListener = null;
    }


    @Override
    public void onPayChoiceSelection(int i) {
        payConfirmation(i);
        Tab.getInstance().payAllOrders();
        Tab.getInstance().loadAllCollections();
    }

    private void payConfirmation(int i) {
        String choice = getResources().getStringArray(R.array.pay_options)[i];
        Toast.makeText(getContext(), getResources().getString(R.string.paychoiceconfirmation, choice)
                , Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        mTabPageFragmentAdapter = new TabPageFragmentAdapter(getChildFragmentManager());
        mViewPager = view.findViewById(R.id.tab_page_viewpager);
        mTabLayout = view.findViewById(R.id.tab_page_tabs);
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mTabPageFragmentAdapter);
        mActionBar.setTitle(Tab.getInstance().getTable().getNickName());

        setHasOptionsMenu(true); // anders denkt android dat hij de standaard opties moet gebruiken

        Tab.getInstance().addListener(this);
        Tab.getInstance().loadAllCollections();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.pay, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
        pay_action = menu.findItem(R.id.action_pay_tab);
        logout_action = menu.findItem(R.id.action_pay_logout);
        invalidated();
    }

    private void logout() {
        if (Tab.getInstance().canLogout()) {
            FragmentManager fr = getChildFragmentManager();
            LogoutDialogFragment logoutDialogFragment = LogoutDialogFragment.newInstance();
            logoutDialogFragment.show(fr, LOGOUT_CHOICE_DIALOG_FRAGMENT_TAG);
        } else {
            Toast.makeText(getContext(), R.string.open_bill, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pay_tab:
                payOrders();
                return true;
            case R.id.action_pay_logout:
                this.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void payOrders() {
        if (Tab.getInstance().getOrderedOrders().size() != 0
                || Tab.getInstance().getOrderedOrders().size() != 0) {
            // er is iets om te betalen
            FragmentManager fr = getChildFragmentManager();
            PayChoiceDialogFragment payChoiceDialogFragment = PayChoiceDialogFragment.newInstance();
            payChoiceDialogFragment.show(fr, PAY_CHOICE_DIALOG_FRAGMENT_TAG);
        } else {
            Toast.makeText(getContext(), R.string.nothing_to_pay, Toast.LENGTH_LONG).show();
        }
    }

    public static TabFragment newInstance() {
        return new TabFragment();
    }

    @Override
    public void invalidated() {
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onLogoutChoiceSelection(boolean choice) {
        if (choice) {
            Tab.getInstance().logout();
            logoutListener.loggedOut();
        }
    }

    public interface LogoutListener {
        void loggedOut();
    }

    public static class TabPageFragmentAdapter extends FragmentPagerAdapter {
        public enum Division {
            ORDERED("Ordered"),
            RECEIVED("Received"),
            PAYED("Paid");

            private final String title;

            Division(String title) {
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
            Division d = Division.values()[position];
            int amount = 0;
            switch (d) {
                case PAYED:
                    amount = Tab.getInstance().getPayedOrders().size();
                    break;
                case RECEIVED:
                    amount = Tab.getInstance().getReceivedOrders().size();
                    break;
                case ORDERED:
                    amount = Tab.getInstance().getOrderedOrders().size();
                    break;
            }
            return String.format("%s (%d)", Division.values()[position].getTitle(), amount);
        }
    }


}
