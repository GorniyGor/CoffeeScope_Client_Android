package com.example.adm1n.coffeescope.main_map.model;

import com.example.adm1n.coffeescope.App;
import com.example.adm1n.coffeescope.models.basket.Basket;
import com.example.adm1n.coffeescope.network.ApiInterface;
import com.example.adm1n.coffeescope.network.responses.PlaceResponse;
import com.example.adm1n.coffeescope.network.responses.PlacesResponse;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.operators.observable.ObservableFromCallable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by adm1n on 01.08.2017.
 */

public class MainPlacesModel implements IMainPlacesModel {

    private ApiInterface apiInterface = App.getApiInterface();
    private Realm mRealm = null;
    private Basket mBasket;
    private Integer mBasketId;

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
