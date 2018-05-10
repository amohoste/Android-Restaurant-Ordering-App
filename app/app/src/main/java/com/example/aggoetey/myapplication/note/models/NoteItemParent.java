package com.example.aggoetey.myapplication.note.models;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.aggoetey.myapplication.model.Tab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sitt on 10/05/18.
 */

public class NoteItemParent implements ParentObject {
    private String name;
    private List<Object> mChildrenList;

    public NoteItemParent(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<Object> getChildObjectList() {

        return mChildrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        mChildrenList =  list;
    }
}
