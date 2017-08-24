package com.example.adm1n.coffeescope.main_map.presenter;

import android.content.Context;

import com.example.adm1n.coffeescope.models.basket.Basket;
import com.example.adm1n.coffeescope.main_map.model.MainPlacesModel;
import com.example.adm1n.coffeescope.main_map.view.IMapActivity;
import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.models.basket.BasketProducts;
import com.example.adm1n.coffeescope.network.responses.PlaceResponse;
import com.example.adm1n.coffeescope.network.responses.PlacesResponse;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by adm1n on 01.08.2017.
 */

public class MainPresenter implements IMainPresenter {
    private Context mContext;
    private IMapActivity mView;
    private MainPlacesModel model = new MainPlacesModel();
    private Realm mRealm = Realm.getDefaultInstance();
    private Place myPlace;

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
                        savePlaces(placesResponse.getPlaceList());
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
                        savePlace(data);
                        mView.setMenuAdapter(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mView.showError(throwable.getMessage());
                    }
                });
    }

    @Override
    public Observable<Basket> getBasket(Integer basketId) {
        return model.getBasket(basketId);
    }

    @Override
    public void savePlaces(ArrayList<Place> list) {
        mRealm.beginTransaction();
        for (int i = 0; i < list.size(); i++) {
            Place place = list.get(i);
            mRealm.copyToRealmOrUpdate(place);
        }
        mRealm.commitTransaction();
    }

    @Override
    public void savePlace(Place place) {
        myPlace = place;
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(myPlace);
        mRealm.commitTransaction();

        Basket mBasket = mRealm.where(Basket.class).equalTo("mBasketId", place.getId()).findFirst();
        if (mBasket == null) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Basket basket = realm.createObject(Basket.class, myPlace.getId());
                    basket.setmBasketProductsList(new RealmList<BasketProducts>());
                }
            });
        }
    }

//    @Override
//    public void savePlaces(final ArrayList<Place> list) {
//        try { // I could use try-with-resources here
//            mRealm = Realm.getDefaultInstance();
//            mRealm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    for (int i = 0; i < list.size(); i++) {
//                        Place place = list.get(i);
//                        mRealm.copyToRealmOrUpdate(place);
//                    }
//                }
//            });
//        } finally {
//            if (mRealm != null) {
//                mRealm.close();
//            }
//        }
//    }
//
//    @Override
//    public void savePlace(Place place) {
//        myPlace = place;
//
//        try { // I could use try-with-resources here
//            mRealm = Realm.getDefaultInstance();
//            mRealm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    mRealm.copyToRealmOrUpdate(myPlace);
//                }
//            });
//
//            Basket mBasket = mRealm.where(Basket.class).equalTo("mBasketId", place.getId()).findFirst();
//            if (mBasket == null) {
//                mRealm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        Basket basket = realm.createObject(Basket.class, myPlace.getId());
//                        basket.setmBasketProductsList(new RealmList<BasketProducts>());
//                    }
//                });
//            }
//        } finally {
//            if (mRealm != null) {
//                mRealm.close();
//            }
//        }
//    }
}
