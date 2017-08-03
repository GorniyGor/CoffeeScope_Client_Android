package com.example.adm1n.coffeescope.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by adm1n on 22.07.2017.
 */

public class Categories implements Parcelable {

    private Integer id;
    private String name;
    private ArrayList<Products> products;

    protected Categories(Parcel in) {
        name = in.readString();
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

    public ArrayList<Products> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Products> products) {
        this.products = products;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
