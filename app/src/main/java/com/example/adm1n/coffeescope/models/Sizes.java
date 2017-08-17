package com.example.adm1n.coffeescope.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by adm1n on 22.07.2017.
 */

public class Sizes implements Parcelable {
    private Integer id;
    private String size;
    private Integer price;
    private Integer discount;
    private Integer price_with_discount;

    protected Sizes(Parcel in) {
        size = in.readString();
        id = in.readInt();
        discount = in.readInt();
        price = in.readInt();
        price_with_discount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(size);
        dest.writeInt(id);
        dest.writeInt(discount);
        dest.writeInt(price);
        dest.writeInt(price_with_discount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Sizes> CREATOR = new Creator<Sizes>() {
        @Override
        public Sizes createFromParcel(Parcel in) {
            return new Sizes(in);
        }

        @Override
        public Sizes[] newArray(int size) {
            return new Sizes[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getPrice_with_discount() {
        return price_with_discount;
    }

    public void setPrice_with_discount(Integer price_with_discount) {
        this.price_with_discount = price_with_discount;
    }
}
