package com.example.aggoetey.myapplication.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by sitt on 05/04/18.
 */

public class UIUtility {
    public static int calculateNoOfColumns(Context context, int width) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }
}
