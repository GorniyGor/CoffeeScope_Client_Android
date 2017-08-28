package com.example.adm1n.coffeescope.coffee_ingredients.presenter;

import com.example.adm1n.coffeescope.models.Ingredients;
import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.models.Product;
import com.example.adm1n.coffeescope.models.basket.Basket;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by adm1n on 28.08.2017.
 */

public class CoffeeIngredientsPresenter implements ICoffeeIngredientsPresenter {

    private Realm mRealm;
    private Basket mBasket;
    private Integer mBasketId;
    private Place mCurrentPlace;
    private Integer mProductId;
    private Product mProduct;
    private Integer mPlaceId;
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
    public RealmList<Ingredients> getIngredients(Integer placeId) {
        mPlaceId = placeId;
        mIngredientsList = new RealmList<>();
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Place place = mRealm.where(Place.class)
                            .equalTo("id", mPlaceId)
                            .findFirst();
                    mCurrentPlace = mRealm.copyFromRealm(place);
                    if (mCurrentPlace.getIngredients() != null) {
                        mIngredientsList.addAll(mCurrentPlace.getIngredients());
                    }
                }
            });
        } finally {
            if (mRealm != null) {
                mRealm.close();
            }
        }
        return mIngredientsList;
    }

    @Override
    public Product getProduct(Integer id) {
        mProductId = id;
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Product product = mRealm.where(Product.class)
                            .equalTo("id", mProductId)
                            .findFirst();
                    mProduct = mRealm.copyFromRealm(product);
                }
            });
        } finally {
            if (mRealm != null) {
                mRealm.close();
            }
        }
        return mProduct;
    }
}
