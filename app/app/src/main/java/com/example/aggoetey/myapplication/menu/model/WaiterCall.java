package com.example.aggoetey.myapplication.menu.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class WaiterCall {
    private ServerTimestamp timestamp;
    private String tableID;

    public WaiterCall() {}

    public WaiterCall(String tableID) {
        this.tableID = tableID;
    }

}
