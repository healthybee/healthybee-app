package com.app.healthybee.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.healthybee.R;
import com.app.healthybee.activities.Applications;
import com.app.healthybee.listeners.VolleyResponseListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amod on 1/11/18.
 */

public class NetworkConstants {
    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static boolean isConnectedToNetwork(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    public static JSONArray jsonArray1 = null;

    public synchronized static <T>
    void getWebservice(final boolean progress, final Context context, int method, String url,
                       final HashMap<String, String> param, final VolleyResponseListener listener,
                       final Class<T[]> aClass) {

        if (isConnectedToNetwork(context)) {

            if (progress) {
                MyCustomProgressDialog.showDialog(context, "Please Wait...");
            }
            StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("4343" + response);
                    if (progress) {
                        MyCustomProgressDialog.dismissDialog();
                    }
                    if (!response.equalsIgnoreCase("[]")) {
                        try {
                            jsonArray1 = new JSONArray(response);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            Object[] object = gson.fromJson(String.valueOf(jsonArray1), aClass);
                            listener.onResponse(object, "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (progress) {
                                MyCustomProgressDialog.dismissDialog();
                                Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                                listener.onError(context.getString(R.string.something_went_wrong));
                            }
                        }
                    } else {
                        listener.onError(context.getString(R.string.something_went_wrong));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progress) {
                        MyCustomProgressDialog.dismissDialog();
                    }
                    listener.onError(error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("Content-Type", "application/json");
                    header.put("Authorization", "Bearer " + SharedPrefUtil.getToken(context));
                    return header;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return param;
                }
            };
            Applications.getInstance().addToRequestQueue(stringRequest);
        } else {
            MyCustomProgressDialog.showAlertDialogMessage(context, context.getString(R.string.network_title), context.getString(R.string.network_message));
        }
    }

}

