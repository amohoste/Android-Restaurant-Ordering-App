package com.example.aggoetey.myapplication.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sitt on 19/04/18.
 *
 * Enum type for the current viewing format of the menu
 */

public enum ViewType implements Serializable {
    GRID_VIEW("Grid_View"), LIST_VIEW("List_View");

    private String viewTypeString;

    ViewType(String viewTypeString) {
        this.viewTypeString = viewTypeString;
    }

    public String getViewTypeString() {
        return viewTypeString;
    }

    //****** Reverse Lookup Implementation************//

    //Lookup table
    private static final Map<String, ViewType> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static
    {
        for(ViewType viewType : ViewType.values())
        {
            lookup.put(viewType.getViewTypeString(),viewType );
        }
    }

    //This method can be used for reverse lookup purpose
    public static ViewType get(String viewTypeString)
    {
        return lookup.get(viewTypeString);
    }
}
