package com.example.adm1n.coffeescope.main_map.model;

import com.example.adm1n.coffeescope.App;
import com.example.adm1n.coffeescope.network.ApiInterface;
import com.example.adm1n.coffeescope.network.responses.PlaceResponse;
import com.example.adm1n.coffeescope.network.responses.PlacesResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by adm1n on 01.08.2017.
 */

public class MainPlacesModel implements IMainPlacesModel {

    private ApiInterface apiInterface = App.getApiInterface();

    public Observable<PlacesResponse> getPlaces() {
        return apiInterface.getPlaces()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<PlaceResponse> getPlace(String id) {
        return apiInterface.getPlace(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
