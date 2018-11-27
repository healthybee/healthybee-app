package com.app.healthybee.models;

/*
 * Created by Amod on 20/9/18.
 */

public class MyOrder {
    public String ItemName;
    public String ItemPrice;
    public String ItemDescription;
    public String address;
    public String PauseForNextDay;

    public MyOrder(String itemName, String itemPrice, String itemDescription, String address, String pauseForNextDay) {
        ItemName = itemName;
        ItemPrice = itemPrice;
        ItemDescription = itemDescription;
        this.address = address;
        PauseForNextDay = pauseForNextDay;
    }

    public MyOrder(String itemName, String itemPrice, String itemDescription) {
        ItemName = itemName;
        ItemPrice = itemPrice;
        ItemDescription = itemDescription;
    }

    public MyOrder() {
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPauseForNextDay() {
        return PauseForNextDay;
    }

    public void setPauseForNextDay(String pauseForNextDay) {
        PauseForNextDay = pauseForNextDay;
    }
}
