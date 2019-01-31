package com.app.healthybee.testcode;


import com.app.healthybee.models.CategoryItem;
import com.app.healthybee.utils.Config;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    String CACHE = "Cache-Control: max-age=0";
    String AGENT = "Data-Agent: Healthy Bee";

    //    @Headers({CACHE, AGENT})
//    @GET("api/get_news_detail/?id=")
//    Call<PushNotif> getNotificationDetail(
//            @Query("id") long id
//    );
//
//    @Headers({CACHE, AGENT})
//    @GET("api/get_post_detail")
//    Call<CallbackNewsDetail> getPostDetail(
//            @Query("id") long id
//    );
    // https://healthybee-mob-api.herokuapp.com/menus?category=
/*
    @Headers({CACHE, AGENT})
    @GET("menus?category=" + "SANDWHICHES")
    Call<CallbackRecent> getRecentPost(
            @Query("page") int page,
            @Query("limit") int count,
            String strToken);

*/


    @GET("menus?category=" + "SANDWHICHES")
    Call<CategoryItem> getRecentPost(
            @Query("page") int page,
            @Query("limit") int count,
            @Header("Content-Type") String contentType,
            @Header("Authorization") String authHeader);



    @Headers({CACHE, AGENT})
    @GET("api/get_video_posts/?api_key=" + Config.API_KEY)
    Call<CallbackRecent> getVideoPost(
            @Query("page") int page,
            @Query("count") int count
    );
//
//    @Headers({CACHE, AGENT})
//    @GET("api/get_category_index/?api_key=" + Config.API_KEY)
//    Call<CallbackCategories> getAllCategories();
//
//    @Headers({CACHE, AGENT})
//    @GET("api/get_category_posts" )
//    Call<CallbackCategoryDetails> getCategoryDetailsByPage(
//            @Query("id") long id,
//            @Query("page") long page,
//            @Query("count") long count
//    );

    @Headers({CACHE, AGENT})
    @GET("api/get_search_results/?api_key=" + Config.API_KEY)
    Call<CallbackRecent> getSearchPosts(
            @Query("search") String search,
            @Query("count") int count
    );

//    @Headers({CACHE, AGENT})
//    @GET("api/get_comments")
//    Call<CallbackComments> getComments(@Query("nid") Long nid
//    );
//
//    @FormUrlEncoded
//    @POST("api/post_comment")
//    Call<Value> sendComment(@Field("nid") Long nid,
//                            @Field("user_id") String user_id,
//                            @Field("content") String content,
//                            @Field("date_time") String date_time);
//
//    @FormUrlEncoded
//    @POST("api/update_comment")
//    Call<Value> updateComment(@Field("comment_id") String comment_id,
//                              @Field("date_time") String date_time,
//                              @Field("content") String content);
//
//    @FormUrlEncoded
//    @POST("api/delete_comment")
//    Call<Value> deleteComment(@Field("comment_id") String comment_id);
//
//    @Headers({CACHE, AGENT})
//    @GET("api/get_privacy_policy")
//    Call<Settings> getPrivacyPolicy();

}
