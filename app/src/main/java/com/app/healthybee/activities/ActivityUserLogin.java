package com.app.healthybee.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.healthybee.MyApplication;
import com.app.healthybee.R;
import com.app.healthybee.utils.MyCustomProgressDialog;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.utils.SharedPrefUtil;
import com.app.healthybee.utils.UrlConstants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ActivityUserLogin extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 1;
    //GoogleSignInClient mGoogleSignInClient;
    private EditText edt_email_id;
    private EditText edt_mobile;
    private EditText edt_otp;
    private EditText edt_login_password;
    private Button btn_login;
    private String strLoginId;
    private String strPassword;
    private TextView tv_newUser;
    private TextView tv_forgot_password;
    private TextView tv_resend_otp;
    private Button bt_password;
    private Button bt_otp;
    private Button btn_generate_otp;
    private ImageView iv_google;
    private Activity activity;
//    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_otp);
        activity = ActivityUserLogin.this;
//        progressBar= findViewById(R.id.progressBar);

        init();
    }


    private void init() {
        edt_email_id = findViewById(R.id.edt_email_id);
        edt_login_password = findViewById(R.id.edt_login_password);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_otp = findViewById(R.id.edt_otp);
        btn_login = findViewById(R.id.btn_login);
        btn_generate_otp = findViewById(R.id.btn_generate_otp);
        btn_generate_otp.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_newUser = findViewById(R.id.tv_newUser);
        tv_resend_otp = findViewById(R.id.tv_resend_otp);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        tv_newUser.setOnClickListener(this);
        bt_password = findViewById(R.id.bt_password);
        bt_password.setOnClickListener(this);
        bt_otp = findViewById(R.id.bt_otp);
        bt_otp.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);

        //set default Selected
        bt_otp.setBackground(getDrawable(R.drawable.button_shap_right_round));
        bt_password.setBackground(getDrawable(R.drawable.button_shap_left_round_selected));
        bt_otp.setTextColor(getResources().getColor(R.color.colorWhite));
        bt_password.setTextColor(getResources().getColor(R.color.background_color));

        iv_google = findViewById(R.id.iv_google);
        iv_google.setOnClickListener(this);
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
       // mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (ValidateUser() == 1) {
                    AuthUser();
                }
                break;
            case R.id.tv_newUser:
                Intent intent = new Intent(ActivityUserLogin.this, ActivityUserRegister.class);
                startActivity(intent);
                break;
            case R.id.bt_password:
                bt_otp.setBackground(getDrawable(R.drawable.button_shap_right_round));
                bt_password.setBackground(getDrawable(R.drawable.button_shap_left_round_selected));
                bt_otp.setTextColor(getResources().getColor(R.color.colorWhite));
                bt_password.setTextColor(getResources().getColor(R.color.background_color));
                btn_generate_otp.setVisibility(View.GONE);
                edt_mobile.setVisibility(View.GONE);
                edt_email_id.setVisibility(View.VISIBLE);
                edt_login_password.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.VISIBLE);
                tv_forgot_password.setVisibility(View.VISIBLE);
                tv_resend_otp.setVisibility(View.GONE);
                edt_otp.setVisibility(View.GONE);
                break;
            case R.id.bt_otp:
                bt_password.setBackground(getDrawable(R.drawable.button_shap_left_round));
                bt_otp.setBackground(getDrawable(R.drawable.button_shap_right_round_selected));
                bt_password.setTextColor(getResources().getColor(R.color.colorWhite));
                bt_otp.setTextColor(getResources().getColor(R.color.background_color));
                edt_email_id.setVisibility(View.GONE);
                edt_login_password.setVisibility(View.GONE);
                btn_generate_otp.setVisibility(View.VISIBLE);
                edt_mobile.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.GONE);
                tv_forgot_password.setVisibility(View.INVISIBLE);
                tv_resend_otp.setVisibility(View.GONE);
                edt_otp.setVisibility(View.GONE);
                break;

            case R.id.btn_generate_otp:
                edt_email_id.setVisibility(View.GONE);
                edt_login_password.setVisibility(View.GONE);
                btn_generate_otp.setVisibility(View.GONE);
                edt_mobile.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.VISIBLE);
                tv_forgot_password.setVisibility(View.GONE);
                tv_resend_otp.setVisibility(View.VISIBLE);
                edt_otp.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_forgot_password:
                Intent intent1 = new Intent(ActivityUserLogin.this, ActivityForgotPassword.class);
                startActivity(intent1);
                break;
            case R.id.iv_google:
               // signInGoogle();
                break;

            default:
                break;
        }
    }

//    private void signInGoogle() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//    }
//
//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            updateUI(account);
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//        }
//    }

//    private void updateUI(@Nullable GoogleSignInAccount account) {
//        if (account != null) {
//            Log.e("TAG", account.getDisplayName());
//
////  4/10/18 sign in
//
//        } else {
//            //  4/10/18 sign out
//        }
//    }

//    private void signOut() {
//        mGoogleSignInClient.signOut()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // [START_EXCLUDE]
//                        updateUI(null);
//                        // [END_EXCLUDE]
//                    }
//                });
//    }

    private int ValidateUser() {
        strLoginId = edt_email_id.getText().toString();
        strPassword = edt_login_password.getText().toString();
        if (!isValidEmail(strLoginId)) {
            Toast.makeText(this, "Invalid Email Id", Toast.LENGTH_SHORT).show();
            return 0;
        } else if (TextUtils.isEmpty(strPassword)) {
            Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
            return 0;
        }
        return 1;
    }

    public boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void AuthUser() {
        if (NetworkConstants.isConnectingToInternet(activity)) {
            MyCustomProgressDialog.showDialog(activity, getString(R.string.please_wait));
            Map<String, String> params = new HashMap<>();
            params.put("email", strLoginId);
            params.put("password", strPassword);
            Log.d("4343", new JSONObject(params).toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    UrlConstants.authUser,
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("4343", response.toString());
                            MyCustomProgressDialog.dismissDialog();

                            if (response.has("token")) {
                                SharedPrefUtil.setToken(activity, response.optString("token"));
                                Log.d("4343", response.optString("token"));
                            }
                            if (response.has("user")) {
                                JSONObject jsonObject = response.optJSONObject("user");
                                if (jsonObject.has("id")) {
                                    SharedPrefUtil.setUserId(activity, jsonObject.optString("id"));
                                }
                                if (jsonObject.has("name")) {
                                    SharedPrefUtil.setUserName(activity, jsonObject.optString("name"));
                                }
                                if (jsonObject.has("picture")) {
                                    SharedPrefUtil.setUserPicture(activity, jsonObject.optString("picture"));
                                }
                                if (jsonObject.has("email")) {
                                    SharedPrefUtil.setUserEmail(activity, jsonObject.optString("email"));
                                }
                                if (jsonObject.has("mobile")) {
                                    SharedPrefUtil.setUserMobile(activity, jsonObject.optString("mobile"));
                                }
                                if (jsonObject.has("createdAt")) {
                                    SharedPrefUtil.setCreatedAt(activity, jsonObject.optString("createdAt"));
                                }
                            }
                            SharedPrefUtil.setUserPassword(activity, strPassword);
                            // for login session
                            SharedPrefUtil.setIsLogin(activity, true);
                            Intent intent = new Intent(activity, ActivitySubscribe.class);
                            startActivity(intent);
                            finish();

                            //  {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjMjM1NzZiNzNhN2FhMDAxN2RkNjY2ZSIsImlhdCI6MTU0NTgyMjcwMX0.ekBniz5xdlYHjDy3rKV4AUWIA3Lupek1Jn3LcHxWGPo",
                            // "user":{"id":"5c23576b73a7aa0017dd666e",
                            // "name":"amod2android",
                            // "picture":"https:\/\/gravatar.com\/avatar\/e0ced4403e76e8bc5cdea71754834388?d=identicon",
                            // "email":"amod2android@gmail.com",
                            // "mobile":"9284326399",
                            // "createdAt":"2018-12-26T10:26:51.868Z"}}

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
                    String credentials = edt_email_id.getText().toString().trim() + ":" + edt_login_password.getText().toString().trim();
                    String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Basic " + base64EncodedCredentials);

                    return headers;
                }
            };
            Log.d("4343", jsonObjectRequest.toString());
            MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(activity, getString(R.string.network_title), getString(R.string.network_message));
        }
    }
}
