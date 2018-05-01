package com.example.aggoetey.myapplication.model;

import com.google.firebase.firestore.ServerTimestamp;

public class WaiterCall {
    private ServerTimestamp timestamp;
    private String tableID;

    public WaiterCall() {}

    public WaiterCall(String tableID) {
        this.tableID = tableID;
    }

}
