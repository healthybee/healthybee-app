package com.app.healthybee;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.models.CategoryItem;
import com.app.healthybee.utils.Constant;
import com.app.healthybee.utils.MyCustomProgressDialog;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.utils.SharedPrefUtil;
import com.app.healthybee.utils.UrlConstants;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Common {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void AddCart(final Context context, CategoryItem categoryItem, int CartCount){
        if (NetworkConstants.isConnectingToInternet(Objects.requireNonNull(context))) {
            MyCustomProgressDialog.showDialog(context, context.getString(R.string.please_wait));
            Map<String, String> params = new HashMap<>();
            params.put("productId", categoryItem.getId());
            params.put("quantity", CartCount+"");
            Log.d("4343", new JSONObject(params).toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    UrlConstants.CreateCart,
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MainActivity.cartCount=MainActivity.cartCount+1;
                            ((MainActivity) Objects.requireNonNull(context)).setCountText();
                            Log.d("4343", response.toString());
                            MyCustomProgressDialog.dismissDialog();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("4343", "Site Info Error: " + error.getMessage());
                    MyCustomProgressDialog.dismissDialog();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(context));
                    return headers;
                }
            };
            Log.d("4343", jsonObjectRequest.toString());
            MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(context, context.getString(R.string.network_title), context.getString(R.string.network_message));
        }
    }
    public static void UpdateCart(final Context context, String cartId, int CartCount, final int card_plus_minus){
        if (NetworkConstants.isConnectingToInternet(Objects.requireNonNull(context))) {
            MyCustomProgressDialog.showDialog(context, context.getString(R.string.please_wait));
            Map<String, String> params = new HashMap<>();
            params.put("quantity", CartCount+"");
            Log.d("4343", new JSONObject(params).toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    UrlConstants.UpdateCart+cartId,
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("4343", response.toString());
                            MyCustomProgressDialog.dismissDialog();
                            if (card_plus_minus==Constant.CARD_PLUS){
                                MainActivity.cartCount=MainActivity.cartCount+1;
                                ((MainActivity) Objects.requireNonNull(context)).setCountText();
                            }
                            if (card_plus_minus==Constant.CARD_MINUS){
                                MainActivity.cartCount=MainActivity.cartCount-1;
                                ((MainActivity) Objects.requireNonNull(context)).setCountText();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("4343", "Site Info Error: " + error.getMessage());
                    MyCustomProgressDialog.dismissDialog();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(context));

                    return headers;
                }
            };
            Log.d("4343", jsonObjectRequest.toString());
            MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(context, context.getString(R.string.network_title), context.getString(R.string.network_message));
        }
    }
    public static void DeleteCart(final Context context, String cartId){
        if (NetworkConstants.isConnectingToInternet(Objects.requireNonNull(context))) {
            MyCustomProgressDialog.showDialog(context, context.getString(R.string.please_wait));
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.DELETE,
                    UrlConstants.DeleteCart+cartId,
                    null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("4343", response.toString());
                            MyCustomProgressDialog.dismissDialog();
                            MainActivity.cartCount=MainActivity.cartCount-1;
                            ((MainActivity) Objects.requireNonNull(context)).setCountText();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("4343", "Site Info Error: " + error.getMessage());
                    MyCustomProgressDialog.dismissDialog();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(context));

                    return headers;
                }
            };
            Log.d("4343", jsonObjectRequest.toString());
            MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(context, context.getString(R.string.network_title), context.getString(R.string.network_message));
        }
    }

}
