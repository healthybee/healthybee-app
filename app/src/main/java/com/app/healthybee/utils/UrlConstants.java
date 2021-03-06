package com.app.healthybee.utils;

/*
 * Created by Amod on 1/11/18.
 */

public class UrlConstants {
    public static final String users = "users";
    private static final String auth = "auth";
    public static final String access_token = "access_token";
    public static final String token = "3biGa1hRAPnwN7Ad9hdMOhm6NGBGC4MU";
    public static String baseUrl = "https://healthybee-mob-api.herokuapp.com/";

    public static String createUser = baseUrl+users+"?"+access_token+"="+token;
    public static String authUser = baseUrl +auth+"?"+access_token+"="+token;
    public static String deliverySupport = baseUrl +"delivery-supports";
    public static String retrieveAddresses = baseUrl +"addresses";
    public static String createAddresses = baseUrl +"addresses";
    public static String updateAddresses = baseUrl +"addresses/";
    public static String updatePassword = baseUrl+users+"/";
    public static String getCategory = "https://healthybee-mob-api.herokuapp.com/menus/categories";
    public static String getCategoryItemList = "https://healthybee-mob-api.herokuapp.com/menus?category=";
    public static String SearchItem = "https://healthybee-mob-api.herokuapp.com/menus?name=";
    public static String CreateCart = "https://healthybee-mob-api.herokuapp.com/carts";
    public static String RetrieveCart = baseUrl+"carts";
    public static String UpdateCart = "https://healthybee-mob-api.herokuapp.com/carts/";
    public static String DeleteCart = "https://healthybee-mob-api.herokuapp.com/carts/";
    public static String CreateFavourite = "https://healthybee-mob-api.herokuapp.com/favourites";
    public static String RetrieveFavourite = "https://healthybee-mob-api.herokuapp.com/favourites";
    public static String DeleteFavourite = "https://healthybee-mob-api.herokuapp.com/favourites/";


    public static String SendEmail = "https://healthybee-mob-api.herokuapp.com/password-resets?access_token="+token;




    //public static String getCategory = "https://us-central1-healthybee-subscription.cloudfunctions.net/api/menu/category";
   // public static String getCategoryItemList = "https://us-central1-healthybee-subscription.cloudfunctions.net/api/menu?category=";


}
