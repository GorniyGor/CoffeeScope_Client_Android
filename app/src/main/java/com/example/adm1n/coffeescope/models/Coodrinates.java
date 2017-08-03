package com.example.adm1n.coffeescope.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by adm1n on 22.07.2017.
 */

public class Coodrinates implements Parcelable {
    private double longitude;
    private double latitude;

    protected Coodrinates(Parcel in) {
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<Coodrinates> CREATOR = new Creator<Coodrinates>() {
        @Override
        public Coodrinates createFromParcel(Parcel in) {
            return new Coodrinates(in);
        }

        @Override
        public Coodrinates[] newArray(int size) {
            return new Coodrinates[size];
        }
    };

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }
}
