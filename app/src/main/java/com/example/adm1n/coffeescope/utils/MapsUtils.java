package com.example.adm1n.coffeescope.utils;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;

/**
 * Created by adm1n on 21.07.2017.
 */

public class MapsUtils {

    public static int calculationDistance(LatLng StartP, LatLng EndP) {
        double d = calculationDistanceByCoord(StartP.latitude, StartP.longitude, EndP.latitude, EndP.longitude);
        return (int) d;
    }

    private static double calculationDistanceByCoord(double startPointLat, double startPointLon, double endPointLat, double endPointLon) {
        float[] results = new float[1];
        Location.distanceBetween(startPointLat, startPointLon, endPointLat, endPointLon, results);
        return results[0];
    }

    public static LatLng castLatLngToString(String latLng) {
        String[] newLatLng = latLng.split(",");
        double latitude = Double.parseDouble(newLatLng[0]);
        double longitude = Double.parseDouble(newLatLng[1]);
        LatLng coffeeLatLng = new LatLng(latitude, longitude);
        return coffeeLatLng;
    }

    public static String castDistance(float distance) {
        if (distance <= 10 && distance >= 0) {
            return ("10 м");
        }
        if (distance >= 100 && distance < 1000) {
            float newDistance = distance % 10;
            return String.valueOf(distance - newDistance + " м");
        }
        if (distance >= 1000) {
            BigDecimal bd = new BigDecimal(Float.toString(distance / 1000));
            bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
            return String.valueOf(bd.floatValue() + " км");
        }
        if (distance >= 10000) {
            BigDecimal bd = new BigDecimal(Float.toString(distance / 1000));
            bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
            return String.valueOf(bd.floatValue() + " км");
        }
        return "";
    }
}
