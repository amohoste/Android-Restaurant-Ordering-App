package com.example.aggoetey.myapplication.discover.helpers;

public class DayConverter {

    public static int toGoogleDay(int day) {
        if (day == 1) {
            return 7;
        } else {
            return day - 1;
        }
    }

}
