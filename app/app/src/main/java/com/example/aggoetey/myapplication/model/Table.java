package com.example.aggoetey.myapplication.model;

import com.example.aggoetey.myapplication.Model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

/**
 * Created by aggoetey on 3/20/18.
 * <p>
 * Een tafel verwijst enkel naar een tafel in een restaurant
 */

public class Table extends Model implements Serializable {

    // bv tafel 13, tafel aan het raam, ronde tafel
    private String nickName;
    private String tableId;

    public Table(String nickName, String tableId) {
        this.nickName = nickName;
        this.tableId = tableId;
        if (nickName == null) {
            loadNickNameFromServer();
        }
    }

    private void loadNickNameFromServer() {
        this.nickName = "Anonieme tafel";
        FirebaseFirestore.getInstance().collection("places").document(Tab.getInstance().getRestaurant().getGooglePlaceId())
                .collection("tables").document(this.tableId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                nickName = (String) documentSnapshot.get("nickname");
            }
        });
        fireInvalidationEvent();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
}
