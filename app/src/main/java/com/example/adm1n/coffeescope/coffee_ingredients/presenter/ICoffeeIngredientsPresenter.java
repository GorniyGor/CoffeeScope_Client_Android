package com.example.adm1n.coffeescope.coffee_ingredients.presenter;

import com.example.adm1n.coffeescope.models.Ingredients;
import com.example.adm1n.coffeescope.models.Product;
import com.example.adm1n.coffeescope.models.basket.Basket;

import io.realm.RealmList;

/**
 * Created by adm1n on 28.08.2017.
 */

public interface ICoffeeIngredientsPresenter {
    Basket getBasket(Integer id);
    void saveBasket(Basket basket);
    RealmList<Ingredients> getIngredients(Integer id);
    Product getProduct(Integer id);
}
