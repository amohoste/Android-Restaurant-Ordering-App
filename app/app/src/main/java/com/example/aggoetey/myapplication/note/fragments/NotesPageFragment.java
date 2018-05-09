package com.example.aggoetey.myapplication.note.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.note.adapters.NoteListAdapter;

public class NotesPageFragment extends Fragment {
    private static final String ARG_MENU_INFO_PARAM = "ARG_MENU_INFO_PARAM";

    private MenuInfo mMenuInfo;

    public NotesPageFragment() {
        // Required empty public constructor
    }

    public static NotesPageFragment newInstance(MenuInfo menuInfo) {
        NotesPageFragment fragment = new NotesPageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MENU_INFO_PARAM, menuInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMenuInfo = (MenuInfo) getArguments().getSerializable(ARG_MENU_INFO_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_page, container, false);
        RecyclerView  recyclerView = view.findViewById(R.id.menu_recycler_view) ;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerView.setAdapter(new NoteListAdapter(mMenuInfo));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
