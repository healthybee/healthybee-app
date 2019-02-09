package com.app.healthybee;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.app.healthybee.utils.FontsOverride;

public class MyApplication extends Application {

    public Typeface font = null;
    public static final String TAG = MyApplication.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    public static volatile Context mMainContext;
    public static volatile Handler mMainHandler;
    public static volatile LayoutInflater mMainLayoutInflater;
    @SuppressLint("StaticFieldLeak")
    private static MyApplication mInstance;
    private RequestQueue mRequestQueue;


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mMainContext = getApplicationContext();
        mMainHandler = new Handler(getMainLooper());
        mMainLayoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/Roboto-Regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Roboto-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/Roboto-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/Roboto-Regular.ttf");


    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
