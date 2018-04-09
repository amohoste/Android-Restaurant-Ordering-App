package com.example.aggoetey.myapplication.menu;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.utils.UIUtility;

/**
 * Created by Dries on 6/04/2018.
 */

public class MenuPageFragment extends Fragment implements MenuCardsAdapter.OnAddNoteButtonClickListener {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_MENU_INFO = "ARG_MENU_INFO";
    public static final String ARG_MENU_CATEGORY = "ARG_MENU_CATEGORY";

    private MenuInfo menuInfo;
    private int mPage;
    private String category;
    private static boolean isGridView = false;
    private boolean instanceIsGridView = isGridView;
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
        setHasOptionsMenu(true);
        mPage = getArguments().getInt(ARG_PAGE);
        category = getArguments().getString(ARG_MENU_CATEGORY);
        menuInfo = (MenuInfo) getArguments().getSerializable(ARG_MENU_INFO);
    }

    @Override
    public void onStop() {
        super.onStop();
        menuInfo.clearAdapters();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_page, container, false);
        mMenuPageRecyclerView = (RecyclerView) view.findViewById(R.id.menu_recycler_view);

        updateViewType(mMenuPageRecyclerView);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_view_change, menu);
        toggleViewTypeMenu(menu);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        toggleViewTypeMenu(menu);
    }

    private void toggleViewTypeMenu(Menu menu) {
        int viewTypeId = R.id.to_list_view;
        if (isGridView) {
            viewTypeId = R.id.to_grid_view;
        }
        menu.findItem(viewTypeId).setVisible(true);
    }

    private void updateViewType(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = new MenuListAdapter(menuInfo, category);
        int columnSpan = 1;

        if (isGridView) {
            adapter = new MenuCardsAdapter(menuInfo, category, this);
            float dpWidth = getResources().getDimension(R.dimen.card_width);
            columnSpan = UIUtility.calculateNoOfColumns(getContext(), dpWidth);
            columnSpan = columnSpan <= 0 ? 1 : columnSpan;
        }
        instanceIsGridView = isGridView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnSpan));
        menuInfo.addAdapter(adapter);
        recyclerView.setAdapter(adapter);
    }

    public static boolean isIsGridView() {
        return isGridView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.to_grid_view:
                if (!isGridView) {
                    isGridView = true;
                    menuInfo.clearAdapters();
                    updateViewType(mMenuPageRecyclerView);
                }
                return true;
            case R.id.to_list_view:
                if (isGridView) {
                    isGridView = false;
                    menuInfo.clearAdapters();
                    updateViewType(mMenuPageRecyclerView);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public boolean isInstanceIsGridView() {
        return instanceIsGridView;
    }

    @Override
    public void onAddNoteButtonClick(com.example.aggoetey.myapplication.model.MenuItem menuItem) {

        DialogFragment fragment = OrderNoteDialogFragment.newInstance(menuItem, menuInfo);
        fragment.show(this.getFragmentManager(), this.toString());
    }

}
