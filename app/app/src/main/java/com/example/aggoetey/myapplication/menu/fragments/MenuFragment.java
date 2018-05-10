package com.example.aggoetey.myapplication.menu.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aggoetey.myapplication.Listener;
import com.example.aggoetey.myapplication.MainActivity;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.ServerConnectionFailure;
import com.example.aggoetey.myapplication.menu.adapters.MenuFragmentPagerAdapter;
import com.example.aggoetey.myapplication.menu.services.RestaurantMenuLoader;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.model.ViewType;
import com.example.aggoetey.myapplication.note.activity.NotesActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Dries on 26/03/2018.
 * Fragment waarin een menu wordt getoond.
 */
public class MenuFragment extends Fragment implements Listener {
    private static final String ARG_MENUINFO = "menuinfo";
    private static final int CANCEL_WINDOW = 5000;
    private static final String VIEW_TYPE_PREFERENCE = "VIEW_TYPE_PREFERENCE";
    private static final String VIEW_TYPE_PREFERENCE_FILE = "VIEW_TYPE_PREFERENCE_FILE";
    private static final int REQUEST_MENU_INFO = 200;
    private MenuInfo menuInfo;

    private ViewPager viewPager;
    private MenuFragmentPagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private TextView mMenuRestaurantNameView;
    private Menu optionsMenu;
    private Button mMenuOrderButton;
    private Button mCheckOrderButton;
    private View v;

    private static ViewType viewType = ViewType.LIST_VIEW;

    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance(MenuInfo menuInfo) {
        Log.d("MENUFRAGMENT", "new");
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

        //Get view type from shared preferences
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(VIEW_TYPE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            String viewTypeString = sharedPreferences.getString(VIEW_TYPE_PREFERENCE, viewType.getViewTypeString());
            viewType = ViewType.get(viewTypeString);
        }

        // Set the title in the actionbar.
        setTitle();

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_menu, container, false);


        mMenuOrderButton = (Button) v.findViewById(R.id.menu_view_login_order_button);

        mCheckOrderButton = (Button) v.findViewById(R.id.menu_view_check_button);

        setCheckButtonProperties();
        loggedInCheck();

        // Load the restaurant's menu from the FireStore backend if not loaded already
        if (menuInfo.getRestaurant().getMenu() == null) {
            new RestaurantMenuLoader(menuInfo, this);
        } else {
            setupViewPager();
        }

        return v;
    }


    public void setupViewPager() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        pagerAdapter = new MenuFragmentPagerAdapter(getChildFragmentManager(), menuInfo, viewType);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    // TODO: Call this after logging in
    public void loggedInCheck() {
        if (menuInfo.getTableID() == null) {    //user not logged in
            setLogInButton();
        } else {
            setOrderButton();
        }
    }

    public void setLogInButton() {
        mMenuOrderButton.setEnabled(true);
        mMenuOrderButton.setText(R.string.menu_view_login_button);

        // Login Button action - Open the QR scanner fragment
        mMenuOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).startQRScannerActivity();
            }
        });
    }

    public void setOrderButton() {
        // Order button action
        setOrderButtonProperties();

        // MenuFragment order button action

        mMenuOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast gecancelled = Toast.makeText(getContext(), "Order is gecancelled", Toast.LENGTH_SHORT);
                Toast confirm = Toast.makeText(getContext(), "Je order wordt over 5 seconden verstuurd, ondertussen kan je het cancellen", Toast.LENGTH_SHORT);
                if (mMenuOrderButton.getText().equals("Cancel")) {
                    mMenuOrderButton.setText("Order");
                    confirm.cancel();
                    gecancelled.show();
                } else {
                    confirm.show();
                    mMenuOrderButton.setText("Cancel");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendOrder();
                        }
                    }, CANCEL_WINDOW);
                }
            }
        });
    }

    private void setTitle() {
        if (menuInfo != null) {
            String title = menuInfo.getRestaurant().getTitle();
            Log.e("MenuFragment", getActivity().getActionBar() + "");

            if (getActivity() instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                if (activity.getSupportActionBar() != null) {
                    activity.getSupportActionBar().setTitle(title);
                }
            }else{
                if(getActivity().getActionBar() != null) {
                    getActivity().getActionBar().setTitle(title);
                }
            }
        }
    }

    private void sendOrder() {
        mMenuOrderButton.setText("Order");
        Tab.getInstance().commitOrder(menuInfo.getCurrentOrder(), MenuFragment.this);
    }


    public void setCheckButtonProperties() {

        mCheckOrderButton.setEnabled(menuInfo.getCurrentOrder().getOrderItems().size() > 0 );

        mCheckOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getContext(), NotesActivity.class);
                intent.putExtra(NotesActivity.ARG_MENU_INFO, menuInfo);
                startActivityForResult(intent, REQUEST_MENU_INFO);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_MENU_INFO){
            if(resultCode == AppCompatActivity.RESULT_OK){
                this.menuInfo = (MenuInfo) data.getSerializableExtra(NotesActivity.ARG_MENU_INFO);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setOrderButtonProperties() {
        if (menuInfo.getTableID() != null) {
            if (menuInfo.getCurrentOrder().getOrderItems().size() > 0) {
                mMenuOrderButton.setText(getResources().getString(R.string.menu_view_order_button) + " (€" + menuInfo.getCurrentOrder().getPrice() + ")");
                mMenuOrderButton.setEnabled(true);
            } else {
                mMenuOrderButton.setText(getResources().getString(R.string.menu_view_order_button));
                mMenuOrderButton.setEnabled(false);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() != null) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences(VIEW_TYPE_PREFERENCE_FILE, Context.MODE_PRIVATE);
            if (sharedPref != null) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(VIEW_TYPE_PREFERENCE, viewType.getViewTypeString());
                editor.apply();
            }
        }


        toggleViewTypeMenu(this.optionsMenu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("MenuFragment:", "Destroyed");
        if(menuInfo != null) {
            menuInfo.clearAdapters();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        this.optionsMenu = menu;
        toggleViewTypeMenu(menu);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        this.optionsMenu = menu;
        toggleViewTypeMenu(menu);
    }

    private void toggleViewTypeMenu(Menu menu) {
        if (menu != null) {
            int grid = R.id.to_grid_view;
            int list = R.id.to_list_view;
            menu.findItem(grid).setVisible(viewType == ViewType.LIST_VIEW);
            menu.findItem(list).setVisible(viewType == ViewType.GRID_VIEW);
        } else {
            Log.w("MenuFragmentContainer ", "MENU NOT FOUND");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.e("MenuFragmentContainer ", item.getItemId() + "");

        switch (item.getItemId()) {
            case R.id.call_waiter_button:
                callWaiter();
                return true;
            case R.id.to_grid_view:
                if (viewType == ViewType.LIST_VIEW) {
                    viewType = ViewType.GRID_VIEW;
                    menuInfo.clearAdapters();
                    pagerAdapter.updateViewType(viewType);
                    pagerAdapter.notifyDataSetChanged();
                    toggleViewTypeMenu(this.optionsMenu);
                }
                return true;
            case R.id.to_list_view:
                if (viewType == ViewType.GRID_VIEW) {
                    viewType = ViewType.LIST_VIEW;
                    menuInfo.clearAdapters();
                    pagerAdapter.updateViewType(viewType);
                    pagerAdapter.notifyDataSetChanged();
                    toggleViewTypeMenu(this.optionsMenu);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * First need to get WaiterCall-array before we can add a new WaiterCall
     * TODO: change tableID when Sitt updates the model for the "online" version
     * TODO: check whether the user is logged in to a table/has "permission" to call a waiter
     * TODO: check whether the restaurant supports this function
     */
    public void callWaiter() {
        final Toast try_toast = Toast.makeText(getContext(), getResources()
                .getString(R.string.waiter_call_try), Toast.LENGTH_SHORT);
        try_toast.show();

        final View waiter_button = getActivity().findViewById(R.id.call_waiter_button);
        waiter_button.setEnabled(false);
        final DocumentReference mDocRef = FirebaseFirestore.getInstance().collection("places")
                .document(menuInfo.getRestaurant().getGooglePlaceId()).collection("tables")
                .document(menuInfo.getTableID());

        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<Object> currentCalls;
                if (documentSnapshot.exists() && documentSnapshot.get("waiterCalls") != null) {
                    currentCalls = (ArrayList<Object>) documentSnapshot.get("waiterCalls");
                } else {
                    currentCalls = new ArrayList<>();
                }

                HashMap<String, Object> newEntry = new HashMap<>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // TODO: use server time when FireStore supports timestamps in arrays
                newEntry.put("timestamp", dateFormat.format(new Date()));
                currentCalls.add(newEntry);
                mDocRef.update("waiterCalls", currentCalls).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                try_toast.cancel();
                                Toast.makeText(getContext(), getResources()
                                        .getString(R.string.waiter_call_success), Toast.LENGTH_LONG)
                                        .show();
                                waiter_button.setEnabled(true);
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        try_toast.cancel();
                        Toast.makeText(getContext(), getResources()
                                .getString(R.string.waiter_call_failure), Toast.LENGTH_SHORT).show();
                        waiter_button.setEnabled(true);
                    }
                });
            }
        }).addOnFailureListener(new ServerConnectionFailure(this, try_toast));
        ;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        pagerAdapter = null;
        menuInfo.getCurrentOrder().removeListener(this);
    }

    @Override
    public void invalidated() {
        setCheckButtonProperties();
    }

    public MenuInfo getMenuInfo() {
        return menuInfo;
    }

    public void setMenuInfo(MenuInfo menuInfo) {
        this.menuInfo = menuInfo;
    }

    // TODO: use this later for waiter call control
    public void button_sleeper(final View btn, final int time) {
        btn.setEnabled(false);

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        btn.setEnabled(true);

                    }
                });
            }
        }).start();
    }
}
