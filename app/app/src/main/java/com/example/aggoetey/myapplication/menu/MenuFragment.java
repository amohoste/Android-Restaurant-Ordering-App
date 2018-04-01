package com.example.aggoetey.myapplication.menu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Menu;
import com.example.aggoetey.myapplication.model.Restaurant;
import com.example.aggoetey.myapplication.model.Tab;

import org.w3c.dom.Text;

/**
 * Created by Dries on 26/03/2018.
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {
    private static final String ARG_RESTAURANT = "restaurant";

    private OnFragmentInteractionListener mListener;

    private Restaurant restaurant;

    private Tab.Order currentOrder;
    private RecyclerView mMenuRecyclerView;
    private MenuListAdapter mAdapter;

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
    public static MenuFragment newInstance(Restaurant restaurant) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESTAURANT, restaurant);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            restaurant = (Restaurant) getArguments().getSerializable(ARG_RESTAURANT);
        }
        currentOrder = Tab.getInstance().newOrder();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        mMenuRecyclerView = (RecyclerView) v.findViewById(R.id.menu_recycler_view);

        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        mMenuRestaurantNameView = (TextView) v.findViewById(R.id.menu_restaurant_name_view);
        mMenuRestaurantNameView.setText(restaurant.getTitle());

        mMenuOrderButton = (Button) v.findViewById(R.id.menu_view_order_button);

        mAdapter = new MenuListAdapter(currentOrder, restaurant.getMenu().getMenuItemList(), mMenuOrderButton);
        mMenuRecyclerView.setAdapter(mAdapter);

        mMenuOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentOrder.getOrderItems().size() > 0) {
                    Tab tab = Tab.getInstance();
                    tab.commitOrder(currentOrder);
                    currentOrder = tab.newOrder();

                    mMenuOrderButton.setText("order");

                    mAdapter.setCurrentOrder(currentOrder);
                    mAdapter.resetOrderCountMap();
                    int start = ((LinearLayoutManager) mMenuRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    int stop = ((LinearLayoutManager) mMenuRecyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    for (int i = start; i <= stop; i++) {
                        View itemview = mMenuRecyclerView.getLayoutManager().findViewByPosition(i);
                        TextView itemCount = (TextView) itemview.findViewById(R.id.menu_recycler_item_count_view);
                        itemCount.setText("0");
                    }
                }
            }
        });
        return v;
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
