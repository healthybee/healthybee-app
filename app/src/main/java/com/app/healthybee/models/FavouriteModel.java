package com.app.healthybee.models;

public class FavouriteModel {

    String title;
    String old_price;
    String new_price;


    public FavouriteModel(String title, String old_price, String new_price) {
        this.title = title;
        this.old_price = old_price;
        this.new_price = new_price;
    }

    public FavouriteModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getNew_price() {
        return new_price;
    }

    public void setNew_price(String new_price) {
        this.new_price = new_price;
    }
}
