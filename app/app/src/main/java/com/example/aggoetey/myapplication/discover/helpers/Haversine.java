package com.example.aggoetey.myapplication.discover.helpers;

/**
 * Created by amoryhoste on 02/04/2018.
 * Source: - https://en.wikipedia.org/wiki/Haversine_formula
 */

public class Haversine {

    private static final double radius = 6372.8; // In kilometers

    /**
     * Calculates distance between two points using haversine method
     * @param lat1 latitude coördinate 1
     * @param lon1 longitude coördinate 1
     * @param lat2 coördinate 2
     * @param lon2 coördinate 2
     * @return distance between points (km)
     */
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        return 2 * radius * Math.asin(Math.sqrt(Math.pow(Math.sin(Math.toRadians(lat2 - lat1) / 2),2) + Math.pow(Math.sin(Math.toRadians(lon2 - lon1) / 2),2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))));
    }
}
