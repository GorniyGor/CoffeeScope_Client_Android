package com.example.adm1n.coffeescope.order.presenter;

import com.example.adm1n.coffeescope.models.Ingredients;
import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.models.basket.Basket;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by adm1n on 28.08.2017.
 */

public class OrderPresenter implements IOrderPresenter {
    private Realm mRealm;
    private Basket mBasket;
    private Integer mBasketId;
    private Integer mPlaceId;
    private Place mCurrentPlace;
    private RealmList<Ingredients> mIngredientsList;

    @Override
    public Basket getBasket(Integer id) {
        mBasketId = id;
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    mBasket = mRealm.copyFromRealm(mRealm.where(Basket.class).equalTo("mBasketId", mBasketId).findFirst());
                }
            });
        } finally {
            if (mRealm != null) {
                mRealm.close();
            }
        }
        return mBasket;
    }

    @Override
    public void saveBasket(Basket basket) {
        this.mBasket = basket;
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    mRealm.copyToRealmOrUpdate(mBasket);
                }
            });
        } finally {
            if (mRealm != null) {
                mRealm.close();
            }
        }
    }

    @Override
    public Place getPlace(Integer id) {
        mPlaceId = id;
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    mCurrentPlace = mRealm.copyFromRealm(mRealm.where(Place.class).equalTo("id", mPlaceId).findFirst());
                }
            });
        } finally {
            if (mRealm != null) {
                mRealm.close();
            }
        }
        return mCurrentPlace;
    }
}
