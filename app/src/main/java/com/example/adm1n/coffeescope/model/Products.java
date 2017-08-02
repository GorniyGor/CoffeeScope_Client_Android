package com.example.adm1n.coffeescope.model;

import java.util.ArrayList;

/**
 * Created by adm1n on 22.07.2017.
 */

public class Products {
    private Integer id;
    private String name;
    private Integer price;
    private ArrayList<Sizes> sizes;

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ArrayList<Sizes> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<Sizes> sizes) {
        this.sizes = sizes;
    }

}
