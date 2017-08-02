package com.example.adm1n.coffeescope.network.response;

import com.example.adm1n.coffeescope.model.Place;

import java.util.ArrayList;

/**
 * Created by adm1n on 01.08.2017.
 */

public class PlacesResponse {
    private ArrayList<Place> data;

    public ArrayList<Place> getPlaceList() {
        return data;
    }

    public void setPlaceList(ArrayList<Place> placeList) {
        this.data = placeList;
    }
}
