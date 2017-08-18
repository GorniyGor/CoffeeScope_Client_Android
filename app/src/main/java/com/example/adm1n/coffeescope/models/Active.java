package com.example.adm1n.coffeescope.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by adm1n on 08.08.2017.
 */

public class Active extends RealmObject implements Parcelable {
    private Boolean place;
    private Boolean manager;

    public Active() {
    }

    protected Active(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
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

    public Boolean getPlace() {
        return place;
    }

    public void setPlace(Boolean place) {
        this.place = place;
    }

    public Boolean getManager() {
        return manager;
    }

    public void setManager(Boolean manager) {
        this.manager = manager;
    }
}
