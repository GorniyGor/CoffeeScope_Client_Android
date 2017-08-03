package com.example.adm1n.coffeescope.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by adm1n on 24.07.2017.
 */

public class Image implements Parcelable{
    private String lable;
    private String menu;

    protected Image(Parcel in) {
        lable = in.readString();
        menu = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lable);
        dest.writeString(menu);
    }
}
