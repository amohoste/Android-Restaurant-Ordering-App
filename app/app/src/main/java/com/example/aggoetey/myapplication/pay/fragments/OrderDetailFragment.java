package com.example.aggoetey.myapplication.pay.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.pay.adapters.OrderItemAdapter;

public class OrderDetailFragment extends Fragment {
    public static final String ORDER_KEY = "order";

    private TextView mOrderNr;
    private TextView mPrice;
    private RecyclerView mOrderItems;

    public OrderDetailFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_detail_fragment, parent, false);


        mOrderNr = view.findViewById(R.id.title);
        mPrice = view.findViewById(R.id.price);
        mOrderItems = view.findViewById(R.id.orderItems);


        Bundle arguments = getArguments();

        if (arguments != null) {
            Tab.Order order = (Tab.Order) (arguments.getSerializable(ORDER_KEY));

            if (order != null) {
                mOrderNr.setText(mOrderNr.getContext().getString(R.string.tab_order_position, order.getOrderNumber()));
                mPrice.setText(mPrice.getContext().getString(R.string.price_order, order.getPrice()));
                mOrderItems.setAdapter(new OrderItemAdapter(order));
                mOrderItems.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OrderDetailFragment.
     */
    public static OrderDetailFragment newInstance(Tab.Order order) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ORDER_KEY, order);
        fragment.setArguments(args);
        return fragment;
    }
}
