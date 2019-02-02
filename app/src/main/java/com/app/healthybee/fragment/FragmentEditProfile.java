package com.app.healthybee.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.healthybee.MyApplication;
import com.app.healthybee.R;
import com.app.healthybee.RoundedCornersTransformation;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.utils.MyCustomProgressDialog;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.utils.SharedPrefUtil;
import com.app.healthybee.utils.UrlConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FragmentEditProfile extends Fragment {

    private View root_view;
    private Toolbar toolbar;
    private ImageView ivBack;
    private TextView tvChangePassword;
    private CardView cvChangePassword;
    private Button btChangePassword;
    private TextInputEditText etUserName, etUserEmail, etUserMobile;
    private TextView tvSaveProfile;
    private CircularImageView civProfileImage;
    private EditText tieNewPassword;
    private EditText tieReNewPassword;
    private String strNewPassword;
    private String strReNewPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.activity_edit_profile, null);
        toolbar = root_view.findViewById(R.id.toolbarEditProfile);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ivBack = root_view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).exitApp();
                //finish();
            }
        });

        init(root_view);
        setProfileData();
        tvSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();

            }
        });
        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cvChangePassword.isShown()) {
                    cvChangePassword.setVisibility(View.GONE);

                } else {
                    cvChangePassword.setVisibility(View.VISIBLE);
                }
            }
        });
        btChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidPassword() == 1) {
                    changePassword();
                }
            }
        });

        return root_view;
    }

    private void changePassword() {
        if (NetworkConstants.isConnectingToInternet(getActivity())) {
            MyCustomProgressDialog.showDialog(getActivity(), getString(R.string.please_wait));
            Map<String, String> params = new HashMap<>();
            params.put("password", strNewPassword);
            Log.d("4343", new JSONObject(params).toString());
            //https://healthybee-mob-api.herokuapp.com/users/5c208b7d3233c40017aa4058/password?access_token=3biGa1hRAPnwN7Ad9hdMOhm6NGBGC4MU
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    UrlConstants.updatePassword + SharedPrefUtil.getUserId(getActivity()) + "/password?" + UrlConstants.access_token + "=" + SharedPrefUtil.getToken(getActivity()),
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("4343", response.toString());
                            MyCustomProgressDialog.dismissDialog();
                            if (response.has("message")) {
                                try {
                                    MyCustomProgressDialog.showAlertDialogMessage(getActivity(), "Something went wrong", response.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                SharedPrefUtil.setUserPassword(getActivity(), strNewPassword);
                                AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Password Changed");
                                builder.setMessage("Your password is changed successfully");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        ((MainActivity) getActivity()).exitApp();
                                    }
                                });
                                builder.show();
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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = SharedPrefUtil.getUserEmail(getActivity()) + ":" + SharedPrefUtil.getUserPassword(getActivity());
                    String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Basic " + base64EncodedCredentials);

                    return headers;
                }
            };
            Log.d("4343", jsonObjectRequest.toString());
            MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(getActivity(), getString(R.string.network_title), getString(R.string.network_message));
        }
    }

    private int isValidPassword() {
        strNewPassword = tieNewPassword.getText().toString();
        strReNewPassword = tieReNewPassword.getText().toString();
        if (TextUtils.isEmpty(strNewPassword)) {
            Toast.makeText(getActivity(), "Invalid Password", Toast.LENGTH_SHORT).show();
            return 0;
        } else if (TextUtils.isEmpty(strReNewPassword)) {
            Toast.makeText(getActivity(), "Invalid Password", Toast.LENGTH_SHORT).show();
            return 0;
        } else if (!(strNewPassword.equalsIgnoreCase(strReNewPassword))) {
            Toast.makeText(getActivity(), "Password does not matched", Toast.LENGTH_SHORT).show();
            return 0;
        } else {
            return 1;
        }
    }

    private void updateUserProfile() {
        if (NetworkConstants.isConnectingToInternet(getActivity())) {
            MyCustomProgressDialog.showDialog(getActivity(), getString(R.string.please_wait));
            Map<String, String> params = new HashMap<>();
            params.put("name", etUserName.getText().toString().trim());
            params.put("email", etUserEmail.getText().toString().trim());
            params.put("mobile", etUserMobile.getText().toString().trim());
            Log.d("4343", new JSONObject(params).toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    UrlConstants.baseUrl + UrlConstants.users + "/" + SharedPrefUtil.getUserId(getActivity()),
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("4343", response.toString());
                            MyCustomProgressDialog.dismissDialog();
                            if (response.has("id")) {
                                SharedPrefUtil.setUserId(getActivity(), response.optString("id"));
                            }
                            if (response.has("name")) {
                                SharedPrefUtil.setUserName(getActivity(), response.optString("name"));
                            }

                            if (response.has("email")) {
                                SharedPrefUtil.setUserEmail(getActivity(), response.optString("email"));
                            }
                            if (response.has("mobile")) {
                                SharedPrefUtil.setUserMobile(getActivity(), response.optString("mobile"));
                            }
                            if (response.has("createdAt")) {
                                SharedPrefUtil.setCreatedAt(getActivity(), response.optString("createdAt"));
                            }
                            setProfileData();
//                            Snackbar snackbar1 = Snackbar.make(linearLayout, "Profile updated successfully", Snackbar.LENGTH_SHORT);
//                            snackbar1.show();
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
                    headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(getActivity()));

                    return headers;
                }
            };
            Log.d("4343", jsonObjectRequest.toString());
            MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(getActivity(), getString(R.string.network_title), getString(R.string.network_message));
        }
    }

    private void init(View root_view) {
        etUserName = root_view.findViewById(R.id.etUserName);
        etUserEmail = root_view.findViewById(R.id.etUserEmail);
        etUserMobile = root_view.findViewById(R.id.etUserMobile);
        civProfileImage = root_view.findViewById(R.id.civProfileImage);
        tvSaveProfile = root_view.findViewById(R.id.tvSaveProfile);
        tvChangePassword = root_view.findViewById(R.id.tvChangePassword);
        cvChangePassword = root_view.findViewById(R.id.cvChangePassword);
        btChangePassword = root_view.findViewById(R.id.btChangePassword);
        tieNewPassword = root_view.findViewById(R.id.tieNewPassword);
        tieReNewPassword = root_view.findViewById(R.id.tieReNewPassword);

    }

    private void setProfileData() {
        etUserName.setText(SharedPrefUtil.getUserName(getActivity()));
        etUserEmail.setText(SharedPrefUtil.getUserEmail(getActivity()));
        etUserMobile.setText(SharedPrefUtil.getUserMobile(getActivity()));
        Glide.with(getActivity())
                .load(SharedPrefUtil.getUserPicture(getActivity()).replace(" ", "%20"))
                .apply(RequestOptions
                        .bitmapTransform(new RoundedCornersTransformation(getActivity(), 10, 0))
                        .error(R.drawable.ic_profile_unselected))
                .into(civProfileImage);

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
