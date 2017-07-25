package com.example.adm1n.coffeescope.model;

import java.util.ArrayList;

/**
 * Created by adm1n on 22.07.2017.
 */

public class Categories {
    private Integer id;
    private String name;
    private ArrayList<Products> products;

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

    public ArrayList<Products> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Products> products) {
        this.products = products;
    }
}
