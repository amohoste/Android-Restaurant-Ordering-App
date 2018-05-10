package com.example.aggoetey.myapplication.note.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.note.adapters.NoteExpandableRecyclerAdapter;
import com.example.aggoetey.myapplication.note.models.NoteItemParent;
import com.example.aggoetey.myapplication.note.services.OrderItemNoteSaver;
import com.example.aggoetey.myapplication.note.viewholders.NoteChildViewHolder;

import java.util.ArrayList;
import java.util.List;

public class NotesPageFragment extends Fragment {
    public static final String ARG_MENU_INFO_PARAM = "ARG_MENU_INFO_PARAM";
    public static final String ARG_NOTE_POS = "ARG_NOTE_POS";
    public static final int PENDING_POS = 0;
    public static final int ORDERED_POS = 1;

    private MenuInfo mMenuInfo;

    private int position;

    public NotesPageFragment() {
        // Required empty public constructor
    }

    public static NotesPageFragment newInstance(MenuInfo menuInfo, int pos) {
        NotesPageFragment fragment = new NotesPageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MENU_INFO_PARAM, menuInfo);
        args.putInt(ARG_NOTE_POS, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMenuInfo = (MenuInfo) getArguments().getSerializable(ARG_MENU_INFO_PARAM);
            position = getArguments().getInt(ARG_NOTE_POS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_page, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.menu_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        List<ParentObject> parents = getNoteParents();
        NoteExpandableRecyclerAdapter adapter = createNoteExpandableRecyclerAdapter(parents);

        recyclerView.setAdapter(adapter);
        return view;
    }

    @NonNull
    private NoteExpandableRecyclerAdapter createNoteExpandableRecyclerAdapter(List<ParentObject> parents) {


        NoteExpandableRecyclerAdapter adapter = new NoteExpandableRecyclerAdapter(this.getContext(), parents, position == PENDING_POS);
        NoteChildViewHolder.OnNoteItemEditBtnClickListener listener
                = orderItem ->
                showDialog(OrderNoteDialogFragment.newInstance(new OrderItemNoteSaver(orderItem, adapter), orderItem.getMenuItem().title));
        adapter.setCustomParentAnimationViewId(R.id.note_expand_btn);
        adapter.setParentAndIconExpandOnClick(true);
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setmListener(listener);
        return adapter;
    }

    private void showDialog(DialogFragment fragment){
        if(getFragmentManager() != null) {
            fragment.show(getFragmentManager(), this.toString());
        }else{
            fragment.show(getChildFragmentManager(), this.toString());
        }


    }

    @NonNull
    private List<ParentObject> getNoteParents() {
        switch (position) {
            case PENDING_POS:
                return  getPendingOrders();
            case ORDERED_POS:
                return  getOrderedOrders();
            default:
                break;
        }
        return new ArrayList<>();
    }


    private List<ParentObject> getOrderedOrders() {
        List<ParentObject> result = new ArrayList<>();
        List<Tab.Order> orders =  Tab.getInstance().getOrderedOrders();

        for(int i = 0 ; i<orders.size(); i++){
            Tab.Order orderGroup = orders.get(i);
            List<ParentObject> parentObjects = getParentObjects(orderGroup, i+1);
            result.addAll(parentObjects);
        }
        return result;
    }

    private List<ParentObject> getPendingOrders() {
        Tab.Order pending = mMenuInfo.getCurrentOrder();
        return getParentObjects(pending, 0);

    }

    @NonNull
    private List<ParentObject> getParentObjects(Tab.Order pending, int groupNo) {
        List<List<Tab.Order.OrderItem>> orderGroups = Tab.Order.groupOrders(pending);
        List<ParentObject> parentObjects = new ArrayList<>();

        for (List<Tab.Order.OrderItem> group : orderGroups) {
            if (group.size() != 0) {
                String itemName = group.get(0).getMenuItem().title;

                List<Object> children = new ArrayList<>();
                children.addAll(group);
                NoteItemParent parent = new NoteItemParent(itemName, groupNo);
                parent.setChildObjectList(children);
                parentObjects.add(parent);
            }
        }
        return parentObjects;
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
