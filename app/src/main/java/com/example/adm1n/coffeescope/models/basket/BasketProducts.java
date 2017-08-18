package com.example.adm1n.coffeescope.models.basket;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.adm1n.coffeescope.models.Ingredients;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by adm1n on 18.08.2017.
 */

public class BasketProducts extends RealmObject implements Parcelable {
    private String sizeId;
    private Integer cost;
    private Integer count;
    private RealmList<Ingredients> mIngredientsList = new RealmList<>();

    public BasketProducts() {
    }

    protected BasketProducts(Parcel in) {
        sizeId = in.readString();
        cost = in.readInt();
        count = in.readInt();
        this.mIngredientsList = new RealmList<>();
        this.mIngredientsList.addAll(in.createTypedArrayList(Ingredients.CREATOR));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sizeId);
        dest.writeInt(cost);
        dest.writeInt(count);
        dest.writeTypedList(this.mIngredientsList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BasketProducts> CREATOR = new Creator<BasketProducts>() {
        @Override
        public BasketProducts createFromParcel(Parcel in) {
            return new BasketProducts(in);
        }

        @Override
        public BasketProducts[] newArray(int size) {
            return new BasketProducts[size];
        }
    };

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public RealmList<Ingredients> getmIngredientsList() {
        return mIngredientsList;
    }

    public void setmIngredientsList(RealmList<Ingredients> mIngredientsList) {
        this.mIngredientsList = mIngredientsList;
    }

    public void incrementCount() {
        this.count++;
    }

    public void decrementCount() {
        this.count--;
    }
}