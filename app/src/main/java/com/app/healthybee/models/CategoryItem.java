package com.app.healthybee.models;

/*
 * Created by Amod on 15/9/18.
 */

import android.os.Parcel;
import android.os.Parcelable;


public class CategoryItem implements Parcelable {
    private int itemId;
    private String price;
    private String add_on;
    private String description;
    private String image_url;
    private String food_type;
    private String old_price;
    private String nutrition;
    private String name;
    private String category;
    private int count;

    public CategoryItem() {
    }

    protected CategoryItem(Parcel in) {
        itemId = in.readInt();
        price = in.readString();
        add_on = in.readString();
        description = in.readString();
        image_url = in.readString();
        food_type = in.readString();
        old_price = in.readString();
        nutrition = in.readString();
        name = in.readString();
        category = in.readString();
        count = in.readInt();
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

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAdd_on() {
        return add_on;
    }

    public void setAdd_on(String add_on) {
        this.add_on = add_on;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(itemId);
        parcel.writeString(price);
        parcel.writeString(add_on);
        parcel.writeString(description);
        parcel.writeString(image_url);
        parcel.writeString(food_type);
        parcel.writeString(old_price);
        parcel.writeString(nutrition);
        parcel.writeString(name);
        parcel.writeString(category);
        parcel.writeInt(count);
    }
}

