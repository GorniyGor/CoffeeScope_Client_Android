package com.example.adm1n.coffeescope.main_map.model;

import com.example.adm1n.coffeescope.App;
import com.example.adm1n.coffeescope.models.basket.Basket;
import com.example.adm1n.coffeescope.network.PublicApiInterface;
import com.example.adm1n.coffeescope.network.responses.PlaceResponse;
import com.example.adm1n.coffeescope.network.responses.PlacesResponse;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.operators.observable.ObservableFromCallable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by adm1n on 01.08.2017.
 */

public class MainPlacesModel implements IMainPlacesModel {

    private PublicApiInterface publicApiInterface = App.getPublicApi();
    private Realm mRealm = null;
    private Basket mBasket;
    private Integer mBasketId;
    private ArrayList sdf;

    public Observable<PlacesResponse> getPlaces() {
        return publicApiInterface.getPlaces()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<PlaceResponse> getPlace(String id) {
        sdf = new ArrayList();
        return publicApiInterface.getPlace(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Basket> getBasket(final Integer id) {
        mBasketId = id;
        return new ObservableFromCallable<>(new Callable<Basket>() {
            @Override
            public Basket call() throws Exception {
                try (Realm r = Realm.getDefaultInstance()) {
                    r.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Basket realmBasket = realm.where(Basket.class)
                                    .equalTo("mBasketId", mBasketId)
                                    .findFirst();
                            if (realmBasket != null) {
                                mBasket = realm.copyFromRealm(realmBasket);
                            } else {
                                mBasket = new Basket();
                            }
                        }
                    });
                }
                return mBasket;
            }
        });
    }
}
