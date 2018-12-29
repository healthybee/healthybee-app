package com.app.healthybee.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.healthybee.R;
import com.app.healthybee.RoundedCornersTransformation;
import com.app.healthybee.utils.MyCustomProgressDialog;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.utils.SharedPrefUtil;
import com.app.healthybee.utils.UrlConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ActivityEditProfile extends AppCompatActivity {
    private Activity activity;
    private TextInputEditText etUserName, etUserEmail, etUserMobile;
    private TextView tv_save;
    private LinearLayout linearLayout;
    private CircularImageView civProfileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        activity = ActivityEditProfile.this;
        linearLayout=findViewById(R.id.root_layout);
        init();
        setProfileData();
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();

            }
        });

    }

    private void updateUserProfile() {
        if (NetworkConstants.isConnectingToInternet(activity)) {
            MyCustomProgressDialog.showDialog(activity, getString(R.string.please_wait));
            Map<String, String> params = new HashMap<>();
            params.put("name", etUserName.getText().toString().trim());
            params.put("email", etUserEmail.getText().toString().trim());
            params.put("mobile", etUserMobile.getText().toString().trim());
            Log.d("4343", new JSONObject(params).toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    UrlConstants.baseUrl + UrlConstants.users + "/" + SharedPrefUtil.getUserId(activity),
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("4343", response.toString());
                            MyCustomProgressDialog.dismissDialog();
                            if (response.has("id")) {
                                SharedPrefUtil.setUserId(activity, response.optString("id"));
                            }
                            if (response.has("name")) {
                                SharedPrefUtil.setUserName(activity, response.optString("name"));
                            }

                            if (response.has("email")) {
                                SharedPrefUtil.setUserEmail(activity, response.optString("email"));
                            }
                            if (response.has("mobile")) {
                                SharedPrefUtil.setUserMobile(activity, response.optString("mobile"));
                            }
                            if (response.has("createdAt")) {
                                SharedPrefUtil.setCreatedAt(activity, response.optString("createdAt"));
                            }
                            setProfileData();
                            Snackbar snackbar1 = Snackbar.make(linearLayout, "Profile updated successfully", Snackbar.LENGTH_SHORT);
                            snackbar1.show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("4343", "Site Info Error: " + error.getMessage());
                    MyCustomProgressDialog.dismissDialog();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(activity));

                    return headers;
                }
            };
            Log.d("4343", jsonObjectRequest.toString());
            Applications.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(activity, getString(R.string.network_title), getString(R.string.network_message));
        }
    }

    private void init() {
        etUserName = findViewById(R.id.etUserName);
        etUserEmail = findViewById(R.id.etUserEmail);
        etUserMobile = findViewById(R.id.etUserMobile);
        civProfileImage= findViewById(R.id.civProfileImage);
        tv_save = findViewById(R.id.tv_save);

    }

    private void setProfileData() {
        etUserName.setText(SharedPrefUtil.getUserName(activity));
        etUserEmail.setText(SharedPrefUtil.getUserEmail(activity));
        etUserMobile.setText(SharedPrefUtil.getUserMobile(activity));
        Glide.with(activity)
                .load(SharedPrefUtil.getUserPicture(activity).replace(" ", "%20"))
                .apply(RequestOptions
                        .bitmapTransform(new RoundedCornersTransformation(activity,10, 0))
                        .error(R.drawable.ic_profile_unselected))
                .into(civProfileImage);

    }

}
