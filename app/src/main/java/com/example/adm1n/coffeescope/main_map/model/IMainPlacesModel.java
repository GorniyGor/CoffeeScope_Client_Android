package com.example.adm1n.coffeescope.main_map.model;

import com.example.adm1n.coffeescope.models.basket.Basket;
import com.example.adm1n.coffeescope.network.responses.PlaceResponse;
import com.example.adm1n.coffeescope.network.responses.PlacesResponse;

import io.reactivex.Observable;
import io.realm.RealmList;

/**
 * Created by adm1n on 01.08.2017.
 */

public interface IMainPlacesModel {
    Observable<PlacesResponse> getPlaces();

    Observable<PlaceResponse> getPlace(String id);

    Observable<Basket> getBasket(Integer id);
}
