package com.whackyapps.pallavgrover.techona.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Food {

    @SerializedName("title")
    @Expose
    private String title = "";
    @SerializedName("image")
    @Expose
    private String image = "";
    @SerializedName("price")
    @Expose
    private float price;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food compareTo = (Food) o;
        return Float.compare(compareTo.price, price) == 0 &&
                Objects.equals(title, compareTo.title) &&
                Objects.equals(image, compareTo.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, image, price);
    }
}
