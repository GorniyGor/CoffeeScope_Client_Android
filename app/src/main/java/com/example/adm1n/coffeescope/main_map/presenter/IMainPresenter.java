package com.example.adm1n.coffeescope.main_map.presenter;

import com.example.adm1n.coffeescope.models.Basket;

import io.reactivex.Observable;

/**
 * Created by adm1n on 01.08.2017.
 */

public interface IMainPresenter {
    void getPlaces();
    void getPlace(String placeId);
    Observable<Basket> getBasket(String PlaceId);
}
