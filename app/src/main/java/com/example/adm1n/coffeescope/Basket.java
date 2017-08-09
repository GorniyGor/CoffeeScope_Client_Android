package com.example.adm1n.coffeescope;

import com.example.adm1n.coffeescope.models.Ingredients;
import com.example.adm1n.coffeescope.models.Products;

import java.util.ArrayList;

/**
 * Created by adm1n on 09.08.2017.
 */

public class Basket {
    private ArrayList<Products> basketProductsList;
    private ArrayList<Ingredients> basketIngredientsList;
    private Integer mBasketId;
    private static final Basket ourInstance = new Basket();

    public static Basket getInstance() {
        return ourInstance;
    }

    private Basket() {
    }

    void add(Products products) {
        basketProductsList.add(products);
    }
}
