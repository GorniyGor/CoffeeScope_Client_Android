package com.example.adm1n.coffeescope.order.presenter;

import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.models.basket.Basket;

/**
 * Created by adm1n on 28.08.2017.
 */

public interface IOrderPresenter {
    Basket getBasket(Integer id);
    void saveBasket(Basket basket);
    Place getPlace(Integer id);
}
