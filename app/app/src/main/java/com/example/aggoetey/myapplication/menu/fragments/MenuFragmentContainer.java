package com.example.aggoetey.myapplication.menu.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.error_screen.ErrorScreenFragment;
import com.example.aggoetey.myapplication.model.Tab;

/**
 * Created by Dries on 26/03/2018.
 * Fragment waarin een menu wordt getoond.
 */
public class MenuFragmentContainer extends Fragment {

    private static final String MENU_INFO_KEY = "MENU_INFO_KEY";
    private static final String MENU_FRAGMENT_TAG = "MENU_FRAGMENT_TAG";
    private static final String NO_MENU_SELECTED_FRAGMENT_TAG = "NO_MENU_SELECTED_FRAGMENT_TAG";
    private static final String MENU_NOT_LOADED_FRAGMENT_TAG = "MENU_NOT_LOADED_FRAGMENT_TAG";

    public MenuFragmentContainer() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu_container, parent, false);


        if (Tab.getInstance().getRestaurant() == null) {
            // er is nog geen restaurant geselecteerd
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container, ErrorScreenFragment.newInstance(getString(R.string.no_restaurant_selected)))
                    .addToBackStack(NO_MENU_SELECTED_FRAGMENT_TAG)
                    .commit();
        } else {
            // er is wel een restaurant geselecteerd
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container, MenuFragment.newInstance())
                    .addToBackStack(MENU_FRAGMENT_TAG)
                    .commit();
        }

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static MenuFragmentContainer newInstance() {
        Bundle bundle = new Bundle();
        MenuFragmentContainer menuFragmentContainer = new MenuFragmentContainer();
        menuFragmentContainer.setArguments(bundle);
        return menuFragmentContainer;
    }


}
