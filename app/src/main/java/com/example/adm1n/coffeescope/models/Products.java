package com.example.adm1n.coffeescope.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by adm1n on 22.07.2017.
 */

public class Products extends RealmObject implements Parcelable {
    private Integer id;
    private String name;
    private RealmList<Sizes> sizes;

    public Products() {
    }

    protected Products(Parcel in) {
        name = in.readString();
        id = in.readInt();
        this.sizes = new RealmList<>();
        this.sizes.addAll(in.createTypedArrayList(Sizes.CREATOR));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
        dest.writeTypedList(this.sizes);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public RealmList<Sizes> getSizes() {
        return sizes;
    }

    public void setSizes(RealmList<Sizes> sizes) {
        this.sizes = sizes;
    }

}
