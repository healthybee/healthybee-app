package com.app.healthybee.models;

/*
 * Created by Amod on 15/9/18.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class CategoryItem implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("add_on")
    private String add_on;
    @SerializedName("add_on_price")
    private String add_on_price;
    @SerializedName("category")
    private String category;
    @SerializedName("description")
    private String description;
    @SerializedName("food_type")
    private String food_type;
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("name")
    private String name;
    @SerializedName("nutrition")
    private String nutrition;
    @SerializedName("old_price")
    private String old_price;
    @SerializedName("price")
    private String price;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("count")
    private int count;

    private ArrayList<String> mSpinnerData = new ArrayList<>();

    public CategoryItem() {
    }


    protected CategoryItem(Parcel in) {
        id = in.readString();
        add_on = in.readString();
        add_on_price = in.readString();
        category = in.readString();
        description = in.readString();
        food_type = in.readString();
        image_url = in.readString();
        name = in.readString();
        nutrition = in.readString();
        old_price = in.readString();
        price = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        count = in.readInt();
        mSpinnerData = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(add_on);
        dest.writeString(add_on_price);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeString(food_type);
        dest.writeString(image_url);
        dest.writeString(name);
        dest.writeString(nutrition);
        dest.writeString(old_price);
        dest.writeString(price);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(count);
        dest.writeStringList(mSpinnerData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryItem> CREATOR = new Creator<CategoryItem>() {
        @Override
        public CategoryItem createFromParcel(Parcel in) {
            return new CategoryItem(in);
        }

        @Override
        public CategoryItem[] newArray(int size) {
            return new CategoryItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdd_on() {
        return add_on;
    }

    public void setAdd_on(String add_on) {
        this.add_on = add_on;
    }

    public String getAdd_on_price() {
        return add_on_price;
    }

    public void setAdd_on_price(String add_on_price) {
        this.add_on_price = add_on_price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<String> getmSpinnerData() {
        return mSpinnerData;
    }

    public void setmSpinnerData(ArrayList<String> mSpinnerData) {
        this.mSpinnerData = mSpinnerData;
    }
}

