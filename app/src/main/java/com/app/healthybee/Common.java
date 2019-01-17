package com.app.healthybee;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.healthybee.activities.Applications;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.models.CategoryItem;
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
            Applications.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(context, context.getString(R.string.network_title), context.getString(R.string.network_message));
        }
    }
}