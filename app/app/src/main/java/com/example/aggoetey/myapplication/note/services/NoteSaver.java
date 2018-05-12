package com.example.aggoetey.myapplication.note.services;

import java.io.Serializable;

/**
 * Created by sitt on 10/05/18.
 */

public interface NoteSaver extends Serializable {

    void saveNote(String note);
    void invalidate();
}
