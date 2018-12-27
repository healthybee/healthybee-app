package com.app.healthybee.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtil {

    public static void setToken(Context context, String tokenId) {
        SharedPreferences preferences = context.getSharedPreferences("HealthyBee", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", tokenId);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("HealthyBee", Context.MODE_PRIVATE);
        return prefs.getString("token", "");
    }
    public static void setUserName(Context context, String UserName) {
        SharedPreferences preferences = context.getSharedPreferences("HealthyBee", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserName", UserName);
        editor.apply();
    }
    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("HealthyBee", Context.MODE_PRIVATE);
        return prefs.getString("UserId", "");
    }
    public static void setUserId(Context context, String UserId) {
        SharedPreferences preferences = context.getSharedPreferences("HealthyBee", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserId", UserId);
        editor.apply();
    }
    public static String getCreatedAt(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("HealthyBee", Context.MODE_PRIVATE);
        return prefs.getString("CreatedAt", "");
    }
    public static void setCreatedAt(Context context, String CreatedAt) {
        SharedPreferences preferences = context.getSharedPreferences("HealthyBee", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("CreatedAt", CreatedAt);
        editor.apply();
    }
    public static String getUserName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("HealthyBee", Context.MODE_PRIVATE);
        return prefs.getString("UserName", "");
    }
    public static void setUserPicture(Context context, String UserPicture) {
        SharedPreferences preferences = context.getSharedPreferences("HealthyBee", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserPicture", UserPicture);
        editor.apply();
    }

    public static String getUserPicture(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("HealthyBee", Context.MODE_PRIVATE);
        return prefs.getString("UserPicture", "");
    }
    public static void setUserEmail(Context context, String UserEmail) {
        SharedPreferences preferences = context.getSharedPreferences("HealthyBee", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserEmail", UserEmail);
        editor.apply();
    }

    public static String getUserEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("HealthyBee", Context.MODE_PRIVATE);
        return prefs.getString("UserEmail", "");
    }
    public static void setUserMobile(Context context, String UserMobile) {
        SharedPreferences preferences = context.getSharedPreferences("HealthyBee", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserMobile", UserMobile);
        editor.apply();
    }

    public static String getUserMobile(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("HealthyBee", Context.MODE_PRIVATE);
        return prefs.getString("UserMobile", "");
    }
}
