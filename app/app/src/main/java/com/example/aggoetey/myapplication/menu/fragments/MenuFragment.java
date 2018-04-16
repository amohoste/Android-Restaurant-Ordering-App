package com.example.aggoetey.myapplication.menu.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aggoetey.myapplication.Listener;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.menu.adapters.MenuFragmentPagerAdapter;
import com.example.aggoetey.myapplication.menu.model.MenuInfo;

/**
 * Created by Dries on 26/03/2018.
 * <p>
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment implements Listener, MenuPageFragment.MenuViewStateListener {
    private static final String ARG_MENUINFO = "menuinfo";

    private OnFragmentInteractionListener mListener;

    private MenuInfo menuInfo;

    private ViewPager viewPager;
    private MenuFragmentPagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private TextView mMenuRestaurantNameView;
    private Menu optionsMenu;
    private Button mMenuOrderButton;


    private static boolean isGridView = false;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MenuFragment.
     */
    public static MenuFragment newInstance(MenuInfo menuInfo) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MENUINFO, menuInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            menuInfo = (MenuInfo) getArguments().getSerializable(ARG_MENUINFO);
            menuInfo.getCurrentOrder().addListener(this);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        mMenuRestaurantNameView = (TextView) v.findViewById(R.id.menu_restaurant_name_view);
        mMenuRestaurantNameView.setText(menuInfo.getRestaurant().getTitle());

        mMenuOrderButton = (Button) v.findViewById(R.id.menu_view_order_button);

        setOrderButtonProperties();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        pagerAdapter = new MenuFragmentPagerAdapter(getChildFragmentManager(), menuInfo, this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        mMenuOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuInfo.commitOrder();
                setOrderButtonProperties();
            }
        });

        return v;
    }


    public void setOrderButtonProperties() {
        if (menuInfo.getCurrentOrder().getOrderItems().size() > 0) {
            mMenuOrderButton.setText(getResources().getString(R.string.menu_view_order_button) + " (€" + menuInfo.getCurrentOrder().getPrice() + ")");
            mMenuOrderButton.setEnabled(true);
        } else {
            mMenuOrderButton.setText(getResources().getString(R.string.menu_view_order_button));
            mMenuOrderButton.setEnabled(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        toggleViewTypeMenu(this.optionsMenu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_view_change, menu);
        this.optionsMenu = menu;
        toggleViewTypeMenu(menu);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        this.optionsMenu = menu;
        Log.e("MenuFragment " ,  menu + "");

        toggleViewTypeMenu(menu);
    }

    private void toggleViewTypeMenu(Menu menu) {
        int grid = R.id.to_grid_view;
        int list = R.id.to_list_view;
        menu.findItem(grid).setVisible(!isGridView);
        menu.findItem(list).setVisible(isGridView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.e("MenuFragment " ,  item.getItemId() + "");

        switch (item.getItemId()) {
            case R.id.to_grid_view:
                if (!isGridView) {
                    isGridView = true;
                    pagerAdapter.notifyDataSetChanged();
                    toggleViewTypeMenu(this.optionsMenu);
                }
                return true;
            case R.id.to_list_view:
                if (isGridView) {
                    isGridView = false;
                    pagerAdapter.notifyDataSetChanged();
                    toggleViewTypeMenu(this.optionsMenu);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        pagerAdapter = null;
        menuInfo.getCurrentOrder().removeListener(this);
    }

    @Override
    public void invalidated() {
        setOrderButtonProperties();
    }

    @Override
    public boolean currentViewIsGrid() {
        return  isGridView;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
