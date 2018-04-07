package com.example.aggoetey.myapplication.discover.models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

public class Filter implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(sortMethod);
        parcel.writeDouble(minRating);
        parcel.writeValue(open);
        parcel.writeInt(distance);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

    @SuppressLint("ParcelClassLoader")
    private Filter(Parcel in) {
        this.sortMethod = (SortMethod) in.readSerializable();
        this.minRating = in.readDouble();
        this.open = (boolean) in.readValue(null);
        this.distance = in.readInt();
    }


    public enum SortMethod {
        ALPHABET, DISTANCE
    }

    private SortMethod sortMethod;
    private double minRating;
    private boolean open;
    private int distance;

    public Filter(SortMethod sortMethod, double minRating, boolean open, int distance) {
        this.sortMethod = sortMethod;
        this.minRating = minRating;
        this.open = open;
        this.distance = distance;
    }

    public SortMethod getSortMethod() {
        return sortMethod;
    }

    public double getMinRating() {
        return minRating;
    }

    public boolean isOpen() {
        return open;
    }

    public int getDistance() {
        return distance;
    }

    public void setSortMethod(SortMethod sortMethod) {
        this.sortMethod = sortMethod;
    }

    public void setMinRating(double minRating) {
        this.minRating = minRating;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
