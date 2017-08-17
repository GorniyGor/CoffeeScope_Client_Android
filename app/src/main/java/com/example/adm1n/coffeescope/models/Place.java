package com.example.adm1n.coffeescope.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by adm1n on 22.07.2017.
 */

public class Place implements Parcelable {
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private double rating;
    private String average_time;
    private Active active;
    private String who_deactivated;
    private Coodrinates coodrinates;
    private Image image;
    private ArrayList<Categories> categories;
    private ArrayList<Ingredients> ingredients;
    private ArrayList<Hours> hours;

    public Place() {
    }

    public String getWho_deactivated() {
        return who_deactivated;
    }

    public void setWho_deactivated(String who_deactivated) {
        this.who_deactivated = who_deactivated;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getAverage_time() {
        return average_time;
    }

    public void setAverage_time(String average_time) {
        this.average_time = average_time;
    }

    public Active getActive() {
        return active;
    }

    public void setActive(Active active) {
        this.active = active;
    }

    public Coodrinates getCoodrinates() {
        return coodrinates;
    }

    public void setCoodrinates(Coodrinates coodrinates) {
        this.coodrinates = coodrinates;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public ArrayList<Categories> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Categories> categories) {
        this.categories = categories;
    }

    public ArrayList<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Hours> getHours() {
        return hours;
    }

    public void setHours(ArrayList<Hours> hours) {
        this.hours = hours;
    }

    protected Place(Parcel in) {
        name = in.readString();
        address = in.readString();
        phone = in.readString();
        rating = in.readDouble();
        average_time = in.readString();
        coodrinates = in.readParcelable(Coodrinates.class.getClassLoader());
        image = in.readParcelable(Image.class.getClassLoader());
        categories = in.createTypedArrayList(Categories.CREATOR);
        ingredients = in.createTypedArrayList(Ingredients.CREATOR);
        hours = in.createTypedArrayList(Hours.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeDouble(rating);
        dest.writeString(average_time);
        dest.writeParcelable(coodrinates, flags);
        dest.writeParcelable(image, flags);
        dest.writeTypedList(categories);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(hours);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
