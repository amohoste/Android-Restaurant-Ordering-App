package com.example.aggoetey.myapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aggoetey on 4/1/18.
 */

public abstract class Model {

    private List<Listener> listeners = new ArrayList<>();

    public void addListener(Listener listener){
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener){
        this.listeners.remove(listener);
    }

    public List<Listener> getListeners() {
        return listeners;
    }

    public void setListeners(List<Listener> listeners) {
        this.listeners = listeners;
    }

    public void fireInvalidationEvent(){
        for (Listener listener : listeners) {
            listener.invalidated();
        }
    }

}
