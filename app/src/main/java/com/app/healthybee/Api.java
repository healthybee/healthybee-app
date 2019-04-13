package com.app.healthybee;

import com.app.healthybee.models.Register;
import com.app.healthybee.utils.UrlConstants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("users?access_token="+UrlConstants.token)
    Call<Register> CreateUser(
            @Field("email") String user_email,
            @Field("password") String user_password,
            @Field("mobile") String user_mobile
    );

    @FormUrlEncoded
    @POST("auth?access_token="+UrlConstants.token)
    Call<AuthUser> AuthUser(
            @Header("Authorization") String basic_auth,
            @Field("email") String user_email,
            @Field("password") String user_password
    );

}
