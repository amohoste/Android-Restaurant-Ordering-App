package com.example.aggoetey.myapplication.menu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.aggoetey.myapplication.Listener;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Menu;
import com.example.aggoetey.myapplication.model.MenuItem;
import com.example.aggoetey.myapplication.model.Restaurant;
import com.example.aggoetey.myapplication.model.Tab;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Created by Dries on 26/03/2018.
 *
 * Fragment waarin een menu wordt getoond.
 */
public class MenuFragment extends Fragment implements Listener {
    private static final String ARG_MENUINFO = "menuinfo";

    private OnFragmentInteractionListener mListener;

    private MenuInfo menuInfo;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView mMenuRestaurantNameView;
    private Button mMenuOrderButton;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            menuInfo = (MenuInfo) savedInstanceState.getSerializable(ARG_MENUINFO);
            menuInfo.getCurrentOrder().addListener(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_MENUINFO, menuInfo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
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
        viewPager.setAdapter(new MenuFragmentPagerAdapter(getChildFragmentManager(), menuInfo));

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

    public Button getmMenuOrderButton() {
        return mMenuOrderButton;
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
        mListener = null;
        menuInfo.getCurrentOrder().removeListener(this);
    }

    @Override
    public void invalidated() {
        setOrderButtonProperties();
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
