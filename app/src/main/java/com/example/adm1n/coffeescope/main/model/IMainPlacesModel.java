package com.example.adm1n.coffeescope.main.model;

import com.example.adm1n.coffeescope.network.response.PlaceResponse;
import com.example.adm1n.coffeescope.network.response.PlacesResponse;

import io.reactivex.Observable;

/**
 * Created by adm1n on 01.08.2017.
 */

public interface IMainPlacesModel {
    Observable<PlacesResponse> getPlaces();

    Observable<PlaceResponse> getPlace(String id);
}
