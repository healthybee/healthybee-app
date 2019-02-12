package com.app.healthybee.models;

import com.google.gson.annotations.SerializedName;

public class CartLocal {
    @SerializedName("id")
    private String id;
    @SerializedName("productId")
    private String productId;
    @SerializedName("count")
    private int count;
    @SerializedName("cartId")
    private String cartId;

    public CartLocal() {
    }

    public CartLocal(String productId, int count, String cartId) {
        this.productId = productId;
        this.count = count;
        this.cartId = cartId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
