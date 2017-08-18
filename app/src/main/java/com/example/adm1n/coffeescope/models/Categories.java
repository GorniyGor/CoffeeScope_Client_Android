package com.example.adm1n.coffeescope.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by adm1n on 22.07.2017.
 */

public class Categories extends RealmObject implements Parcelable {

    private Integer id;
    private String name;
    private RealmList<Products> products;

    public Categories() {
    }


    protected Categories(Parcel in) {
        name = in.readString();
        id = in.readInt();
        this.products = new RealmList<>();
        this.products.addAll(in.createTypedArrayList(Products.CREATOR));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
        dest.writeTypedList(this.products);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Categories> CREATOR = new Creator<Categories>() {
        @Override
        public Categories createFromParcel(Parcel in) {
            return new Categories(in);
        }

        @Override
        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Products> getProducts() {
        return products;
    }

    public void setProducts(RealmList<Products> products) {
        this.products = products;
    }

}
