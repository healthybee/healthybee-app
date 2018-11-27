package com.app.healthybee.listeners;

/**
 * Created by root on 1/11/18.
 */

public interface VolleyResponseListener<T> {


    void onResponse(T[] object, String message);

    void onError(String message);


    interface PostResponse {

        void onResponse(String id);

        void onError(String message);

    }

}