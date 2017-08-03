package com.example.adm1n.coffeescope.coffee_menu.custom_model;

import com.example.adm1n.coffeescope.models.Products;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by adm1n on 25.07.2017.
 */

public class CoffeeMenu extends ExpandableGroup {
    public CoffeeMenu(String title, List<Products> items) {
        super(title, items);
    }
}