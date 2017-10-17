package com.example.adm1n.coffeescope.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by adm1n on 22.07.2017.
 */

public class Place extends RealmObject implements Parcelable {
    @PrimaryKey
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
    private RealmList<Categories> categories;
    private RealmList<Ingredients> ingredients;
    private RealmList<Hours> hours;

    @Ignore
    private Bitmap icon;

    @Ignore
    private Bitmap iconBig;

    public Place() {
    }

    protected Place(Parcel in) {
        name = in.readString();
        address = in.readString();
        phone = in.readString();
        rating = in.readDouble();
        average_time = in.readString();
        id = in.readInt();
        active = in.readParcelable(Active.class.getClassLoader());
        who_deactivated = in.readString();
        coodrinates = in.readParcelable(Coodrinates.class.getClassLoader());
        image = in.readParcelable(Image.class.getClassLoader());

        this.categories = new RealmList<>();
        this.categories.addAll(in.createTypedArrayList(Categories.CREATOR));

        this.ingredients = new RealmList<>();
        this.ingredients.addAll(in.createTypedArrayList(Ingredients.CREATOR));

        this.hours = new RealmList<>();
        this.hours.addAll(in.createTypedArrayList(Hours.CREATOR));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeDouble(rating);
        dest.writeString(average_time);
        dest.writeParcelable(active, flags);
        dest.writeString(who_deactivated);
        dest.writeParcelable(coodrinates, flags);
        dest.writeParcelable(image, flags);
        dest.writeInt(id);

        dest.writeTypedList(this.categories);
        dest.writeTypedList(this.ingredients);
        dest.writeTypedList(this.hours);
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

    public RealmList<Categories> getCategories() {
        return categories;
    }

    public void setCategories(RealmList<Categories> categories) {
        this.categories = categories;
    }

    public RealmList<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(RealmList<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public RealmList<Hours> getHours() {
        return hours;
    }

    public void setHours(RealmList<Hours> hours) {
        this.hours = hours;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public Bitmap getIconBig() {
        return iconBig;
    }

    public void setIconBig(Bitmap iconBig) {
        this.iconBig = iconBig;
    }
}
