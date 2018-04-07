package com.example.aggoetey.myapplication.menu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggoetey.myapplication.R;

/**
 * Created by Dries on 6/04/2018.
 */

public class MenuPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_MENU_INFO = "ARG_MENU_INFO";
    public static final String ARG_MENU_CATEGORY = "ARG_MENU_CATEGORY";

    private MenuListAdapter mAdapter;
    private MenuInfo menuInfo;
    private int mPage;
    private String category;
    private RecyclerView mMenuPageRecyclerView;

    public static MenuPageFragment newInstance(int page, String category, MenuInfo menuInfo) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_MENU_CATEGORY, category);
        args.putSerializable(ARG_MENU_INFO, menuInfo);
        MenuPageFragment fragment = new MenuPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        category = getArguments().getString(ARG_MENU_CATEGORY);
        menuInfo = (MenuInfo) getArguments().getSerializable(ARG_MENU_INFO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_page, container, false);
        RecyclerView mMenuPageRecyclerView = (RecyclerView) view.findViewById(R.id.menu_recycler_view);

        mMenuPageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new MenuListAdapter(menuInfo, category);
        menuInfo.addAdapter(mAdapter);
        mMenuPageRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
