package com.example.adm1n.coffeescope.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by adm1n on 08.08.2017.
 */

public class Active implements Parcelable {
    private Integer place;
    private Integer manager;

    protected Active(Parcel in) {
    }

    public static final Creator<Active> CREATOR = new Creator<Active>() {
        @Override
        public Active createFromParcel(Parcel in) {
            return new Active(in);
        }

        @Override
        public Active[] newArray(int size) {
            return new Active[size];
        }
    };

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
