package com.example.aggoetey.myapplication.menu.views;

import android.view.View;

/**
 * Created by sitt on 09/04/18.
 */

public interface ViewInitializer<T, R extends ViewInitializer>  {

     R intializeView (View view);

     R updateView (T data);
}
