package com.example.adm1n.coffeescope.model;

/**
 * Created by adm1n on 22.07.2017.
 */

public class Kofeinia {
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private Integer rating;
    private Integer active;
    private Coodrinates coodrinates;
    private Image image;
    private Categories categories;
    private Ingredients ingredients;
    private Hours hours;

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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
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

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public Ingredients getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredients ingredients) {
        this.ingredients = ingredients;
    }

    public Hours getHours() {
        return hours;
    }

    public void setHours(Hours hours) {
        this.hours = hours;
    }

}
