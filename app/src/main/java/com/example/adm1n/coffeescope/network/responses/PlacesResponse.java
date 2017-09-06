package com.example.adm1n.coffeescope.network.responses;

import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.network.BaseResponse;

import java.util.ArrayList;

/**
 * Created by adm1n on 01.08.2017.
 */

public class PlacesResponse extends BaseResponse {
    private ArrayList<Place> data;

    public ArrayList<Place> getPlaceList() {
        return data;
    }

    public void setPlaceList(ArrayList<Place> placeList) {
        this.data = placeList;
    }
}
