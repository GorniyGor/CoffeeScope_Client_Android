package com.example.adm1n.coffeescope.models.basket;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.adm1n.coffeescope.models.Ingredients;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by adm1n on 18.08.2017.
 */

public class BasketProducts extends RealmObject implements Parcelable {
    @PrimaryKey
    private Integer id;
    private Integer productId;
    private String sizeId;
    private String name;
    private Integer cost;
    private Integer count;
    private Integer costSumm;
    private RealmList<Ingredients> mIngredientsList = new RealmList<>();

    public Integer getSumma() {
        Integer summ = 0;
        for (int j = 0; j < this.mIngredientsList.size(); j++) {
            Ingredients ingredients = this.mIngredientsList.get(j);
            summ += ingredients.getPrice();
        }
        summ += cost;
        summ *= count;
        this.costSumm = summ;
        return summ;
    }

    public BasketProducts() {
        UUID uuid = UUID.randomUUID();
        this.id = uuid.hashCode();
    }

    protected BasketProducts(Parcel in) {
        sizeId = in.readString();
        cost = in.readInt();
        count = in.readInt();
        productId = in.readInt();
        name = in.readString();
        costSumm = in.readInt();
        id = in.readInt();
        this.mIngredientsList = new RealmList<>();
        this.mIngredientsList.addAll(in.createTypedArrayList(Ingredients.CREATOR));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sizeId);
        dest.writeInt(cost);
        dest.writeInt(count);
        dest.writeTypedList(this.mIngredientsList = new RealmList<>());
        dest.writeInt(productId);
        dest.writeString(name);
        dest.writeInt(costSumm);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BasketProducts> CREATOR = new Creator<BasketProducts>() {
        @Override
        public BasketProducts createFromParcel(Parcel in) {
            return new BasketProducts(in);
        }

        @Override
        public BasketProducts[] newArray(int size) {
            return new BasketProducts[size];
        }
    };

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public RealmList<Ingredients> getmIngredientsList() {
        return mIngredientsList;
    }

    public void setmIngredientsList(RealmList<Ingredients> mIngredientsList) {
        this.mIngredientsList = mIngredientsList;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCostSumm() {
        return getSumma();
    }

    public void setCostSumm(Integer costSumm) {
        this.costSumm = costSumm;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}