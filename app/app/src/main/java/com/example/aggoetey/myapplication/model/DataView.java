package com.example.aggoetey.myapplication.model;

/**
 * Created by sitt on 20/04/18.
 */

public interface DataView<T> {

    /**
     * Initialize the fields which the custom view will later use for binding/interactions.
     */
    void init();

    /**
     * Use this method instead of giving data parameter in the constructor/newInstance
     * Storing custom models in fields might cause serializable/parcelable exceptions.
     * Moreover, this view should be able to change by just binding to
     * different models.
     *
     * @param data model to which this view will be bound to
     */

    void bind(T data);
}
