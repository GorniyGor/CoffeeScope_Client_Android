package com.example.adm1n.coffeescope;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by adm1n on 20.07.2017.
 */

public class Coffee {
    private String Name;
    private String Adress;
    private String Number;
    private String JobTime;
    private String coordinate;
    private String rating;
    private String range;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public Coffee() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getJobTime() {
        return JobTime;
    }

    public void setJobTime(String jobTime) {
        JobTime = jobTime;
    }
}
