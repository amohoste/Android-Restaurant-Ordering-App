package com.example.aggoetey.myapplication.menu.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.menu.adapters.MenuCardsAdapter;
import com.example.aggoetey.myapplication.menu.adapters.MenuListAdapter;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.model.ViewType;
import com.example.aggoetey.myapplication.note.fragments.OrderNoteDialogFragment;
import com.example.aggoetey.myapplication.utils.UIUtility;

import java.io.Serializable;

/**
 * Created by Dries on 6/04/2018.
 */

public class MenuPageFragment extends Fragment implements Serializable, MenuCardsAdapter.OnAddNoteButtonClickListener, MenuListAdapter.MenuListClickListener {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_MENU_INFO = "ARG_MENU_INFO";
    public static final String ARG_MENU_CATEGORY = "ARG_MENU_CATEGORY";
    public static final String ARG_MENU_VIEW_LISTENER = "ARG_MENU_VIEW_LISTENER";

    private MenuInfo menuInfo;
    private int mPage;
    private String category;

    private RecyclerView.Adapter mMenuRecyclerAdapter;
    private MenuViewStateListener menuViewStateListener;

    public static MenuPageFragment newInstance(int page, String category, MenuInfo menuInfo, MenuViewStateListener listener) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(ARG_MENU_VIEW_LISTENER, listener);
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
        menuViewStateListener = (MenuViewStateListener) getArguments().getSerializable(ARG_MENU_VIEW_LISTENER);
        category = getArguments().getString(ARG_MENU_CATEGORY);
        menuInfo = (MenuInfo) getArguments().getSerializable(ARG_MENU_INFO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_page, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.menu_recycler_view);
        initViewType(recyclerView);

        return view;
    }


    private void initViewType(RecyclerView recyclerView) {
        int columnSpan = 1;
        RecyclerView.Adapter adapter;


        if (menuViewStateListener.currentViewIsGrid().equals(ViewType.GRID_VIEW)) {
            adapter = new MenuCardsAdapter(menuInfo, category, this);
            float width = getResources().getDimension(R.dimen.card_width);
            int calculatedNoOfColumns = UIUtility.calculateNoOfColumns(getContext(), width);
            columnSpan = (calculatedNoOfColumns > columnSpan) ? calculatedNoOfColumns : columnSpan;
        } else {
            adapter = new MenuListAdapter(menuInfo, category, this);
        }

        menuInfo.addAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), columnSpan));
        recyclerView.setAdapter(adapter);
        mMenuRecyclerAdapter = adapter;
    }


    @Override
    public void onAddNoteButtonClick(com.example.aggoetey.myapplication.model.MenuItem menuItem) {
        showDialog(OrderNoteDialogFragment.newInstance(menuItem, menuInfo));
    }

    @Override
    public void onMenuListLongClick(com.example.aggoetey.myapplication.model.MenuItem menuItem, MenuInfo menuInfo, int position) {
        showDialog(MenuCardInfoDialogFragment.newInstance(menuItem, menuInfo, position, new MenuCardInfoDialogFragment.CardDialogClickListener() {
            @Override
            public void onInteraction(int pos) {
                mMenuRecyclerAdapter.notifyItemChanged(pos);
            }
        }));
    }

    private void showDialog(DialogFragment fragment) {
        fragment.show(this.getFragmentManager(), this.toString());
    }


    public interface MenuViewStateListener extends Serializable {
        ViewType currentViewIsGrid();
        void updateViewType(ViewType viewType);
    }
}
