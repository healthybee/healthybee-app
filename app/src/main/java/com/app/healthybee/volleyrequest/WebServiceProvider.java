package com.app.healthybee.volleyrequest;


import android.content.Context;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class WebServiceProvider {
    private RequestQueue requestQueue;
    public static WebServiceProvider instance;

    private WebServiceProvider(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.start();
    }

    public static WebServiceProvider getInstance(Context context) {
        if (instance == null) {
            instance = new WebServiceProvider(context);
        }
        return instance;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }
}