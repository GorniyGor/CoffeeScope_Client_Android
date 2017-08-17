package com.example.adm1n.coffeescope.models;

import com.example.adm1n.coffeescope.models.Ingredients;
import com.example.adm1n.coffeescope.models.Products;

import java.util.ArrayList;

import io.realm.RealmObject;

/**
 * Created by adm1n on 09.08.2017.
 */

public class Basket extends RealmObject {
//    private ArrayList<BasketProducts> basketProductsList = new ArrayList<>();
//    private ArrayList<Ingredients> basketIngredientsList = new ArrayList<>();
    private Integer mBasketId;

    public Basket() {
    }

    public Basket(Integer mBasketId) {
        this.mBasketId = mBasketId;
    }

//    public ArrayList<BasketProducts> getBasketProductsList() {
//        return basketProductsList;
//    }

//    public void setBasketProductsList(ArrayList<BasketProducts> basketProductsList) {
//        this.basketProductsList = basketProductsList;
//    }

//    public ArrayList<Ingredients> getBasketIngredientsList() {
//        return basketIngredientsList;
//    }
//
//    public void setBasketIngredientsList(ArrayList<Ingredients> basketIngredientsList) {
//        this.basketIngredientsList = basketIngredientsList;
//    }

    public Integer getmBasketId() {
        return mBasketId;
    }

    public void setmBasketId(Integer mBasketId) {
        this.mBasketId = mBasketId;
    }

//    private class BasketProducts extends RealmObject {
//        private ArrayList<Ingredients> mIngredientsList;
//    }
}
