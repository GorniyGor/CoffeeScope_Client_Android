package com.example.adm1n.coffeescope.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by adm1n on 22.07.2017.
 */

public class Ingredients implements Parcelable {
    private Integer id;
    private String name;
    private Integer price;

    public Ingredients() {
        this.name = "Доп. Ингредиенты";
    }

    protected Ingredients(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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
