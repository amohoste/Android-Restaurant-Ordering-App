package com.example.aggoetey.myapplication.tab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuItem;
import com.example.aggoetey.myapplication.model.OrderItem;

import java.util.Arrays;

public class TabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        OrderItem[] orders = new OrderItem[]{
                new OrderItem("notitie", new MenuItem("Gekapt", 10, "lekker gehaktje", "vlees")),
                new OrderItem("notitie", new MenuItem("Gekapt2", 12, "lekker gehaktje2", "vlees"))};

        View view = inflater.inflate(R.layout.fragment_tab, container,false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.tabRecyclerView);

        TabAdapter tabAdapter = new TabAdapter(Arrays.asList(orders));
        recyclerView.setAdapter(tabAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        int prijs = 0;
        for (OrderItem order : orders) {
            prijs += order.getMenuItem().price;
        }

        TextView total = view.findViewById(R.id.total);
        total.setText(String.valueOf(prijs));

        return view;
    }

}
