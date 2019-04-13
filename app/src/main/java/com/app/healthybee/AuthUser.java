package com.app.healthybee;

import com.google.gson.annotations.SerializedName;

public class AuthUser {
    @SerializedName("token")
    private String token;
    @SerializedName("user")
    private User user;

    public AuthUser() {
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
