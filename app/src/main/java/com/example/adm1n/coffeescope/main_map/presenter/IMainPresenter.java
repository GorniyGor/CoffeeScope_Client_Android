package com.example.adm1n.coffeescope.main_map.presenter;

import com.example.adm1n.coffeescope.models.basket.Basket;
import com.example.adm1n.coffeescope.models.Place;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by adm1n on 01.08.2017.
 */

public interface IMainPresenter {
    void getPlaces();
    void getPlace(String placeId);
    void savePlace(Place place);
    void savePlaces(ArrayList<Place> list);
    Observable<Basket> getBasket(String PlaceId);
}
