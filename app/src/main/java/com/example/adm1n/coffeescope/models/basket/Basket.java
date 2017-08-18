package com.example.adm1n.coffeescope.models.basket;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.adm1n.coffeescope.models.Ingredients;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by adm1n on 09.08.2017.
 */

public class Basket extends RealmObject implements Parcelable {
    @PrimaryKey
    private Integer mBasketId;
    private RealmList<BasketProducts> mBasketProductsList = new RealmList<>();

    public Basket() {
    }

    protected Basket(Parcel in) {
        mBasketId = in.readInt();
        this.mBasketProductsList = new RealmList<>();
        this.mBasketProductsList.addAll(in.createTypedArrayList(BasketProducts.CREATOR));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mBasketId);
        dest.writeTypedList(this.mBasketProductsList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Basket> CREATOR = new Creator<Basket>() {
        @Override
        public Basket createFromParcel(Parcel in) {
            return new Basket(in);
        }

        @Override
        public Basket[] newArray(int size) {
            return new Basket[size];
        }
    };

    public RealmList<BasketProducts> getmBasketProductsList() {
        return mBasketProductsList;
    }

    public void setmBasketProductsList(RealmList<BasketProducts> mBasketProductsList) {
        this.mBasketProductsList = mBasketProductsList;
    }

    public Integer getmBasketId() {
        return mBasketId;
    }

    public void setmBasketId(Integer mBasketId) {
        this.mBasketId = mBasketId;
    }
}
