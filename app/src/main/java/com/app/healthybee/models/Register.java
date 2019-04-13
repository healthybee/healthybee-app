package com.app.healthybee.models;

import com.google.gson.annotations.SerializedName;

public class Register {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("picture")
    private String picture;
    @SerializedName("email")
    private String email;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("createdAt")
    private String createdAt;


    public Register(String id, String name, String picture, String email, String mobile, String createdAt) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.email = email;
        this.mobile = mobile;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
