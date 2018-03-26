package com.example.aggoetey.myapplication.tab;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuItem;
import com.example.aggoetey.myapplication.model.OrderItem;

/**
 * Created by aggoetey on 3/24/18.
 */

public class TabListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        OrderItem[] orders = new OrderItem[]{
                new OrderItem("notitie", new MenuItem("Gekapt", 10, "lekker gehaktje", "vlees")),
                new OrderItem("notitie", new MenuItem("Gekapt2", 12, "lekker gehaktje2", "vlees"))};

        ArrayAdapter<OrderItem> orderItemArrayAdapter = new ArrayAdapter<OrderItem>(getActivity(), android.R.layout.simple_list_item_1, orders);
        setListAdapter(orderItemArrayAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }
}
