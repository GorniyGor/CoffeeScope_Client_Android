package com.example.adm1n.coffeescope.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by adm1n on 22.07.2017.
 */

public class Products implements Parcelable {
    private Integer id;
    private String name;
    private ArrayList<Sizes> sizes;

    protected Products(Parcel in) {
        id = in.readInt();
        name = in.readString();
        sizes = in.createTypedArrayList(Sizes.CREATOR);
    }

    public static final Creator<Products> CREATOR = new Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel in) {
            return new Products(in);
        }

        @Override
        public Products[] newArray(int size) {
            return new Products[size];
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

    public ArrayList<Sizes> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<Sizes> sizes) {
        this.sizes = sizes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(sizes);
    }
}
