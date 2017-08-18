package com.example.adm1n.coffeescope.main_map.presenter;

import android.content.Context;

import com.example.adm1n.coffeescope.models.basket.Basket;
import com.example.adm1n.coffeescope.main_map.model.MainPlacesModel;
import com.example.adm1n.coffeescope.main_map.view.IMapActivity;
import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.network.responses.PlaceResponse;
import com.example.adm1n.coffeescope.network.responses.PlacesResponse;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.realm.Realm;

/**
 * Created by adm1n on 01.08.2017.
 */

public class MainPresenter implements IMainPresenter {
    private Context mContext;
    private IMapActivity mView;
    private MainPlacesModel model = new MainPlacesModel();
    Realm mRealm = Realm.getDefaultInstance();

    public MainPresenter(Context mContext, IMapActivity mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void getPlaces() {
        model.getPlaces()
                .subscribe(new Consumer<PlacesResponse>() {
                    @Override
                    public void accept(@NonNull PlacesResponse placesResponse) throws Exception {
                        mView.setMarkers(placesResponse.getPlaceList());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mView.showError(throwable.getMessage());
                    }
                });
    }

    @Override
    public void getPlace(String placeId) {
        model.getPlace(placeId)
                .subscribe(new Consumer<PlaceResponse>() {
                    @Override
                    public void accept(@NonNull PlaceResponse placeResponse) throws Exception {
                        Place data = placeResponse.getData();
                        mView.setMenuAdapter(data);
                        savePlace(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mView.showError(throwable.getMessage());
                    }
                });
    }

    @Override
    public Observable<Basket> getBasket(String basketId) {
//        Basket basket = mRealm.where(Basket.class).equalTo("mBasketId", basketId).findFirst();
        return new Observable<Basket>() {
            @Override
            protected void subscribeActual(Observer<? super Basket> observer) {

            }
        };
        //Идем в реалм и возвращаем корзину
    }

    @Override
    public void savePlace(Place place) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(place);
        mRealm.commitTransaction();
    }
}
