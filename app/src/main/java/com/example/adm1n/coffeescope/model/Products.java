package com.example.adm1n.coffeescope.model;

/**
 * Created by adm1n on 22.07.2017.
 */

public class Products {
    private Integer id;
    private String name;
    private Integer price;
    private Sizes sizes;

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

    public Sizes getSizes() {
        return sizes;
    }

    public void setSizes(Sizes sizes) {
        this.sizes = sizes;
    }

}
