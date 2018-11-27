package com.app.healthybee.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.healthybee.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class ActivityUserLogin extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 1;
    private EditText edt_login_id;
    private EditText edt_login_password;
    private Button btn_login;
    private String strLoginId;
    private String strPassword;
    private TextView tv_newUser;
    private Button bt_password,bt_otp;
    private boolean isOtpSelected=false;
    private ImageView iv_google;
    GoogleSignInClient mGoogleSignInClient;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        init();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        edt_login_id = (EditText) findViewById(R.id.edt_login_id);
        edt_login_password = (EditText) findViewById(R.id.edt_login_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        tv_newUser = (TextView) findViewById(R.id.tv_newUser);
        tv_newUser.setOnClickListener(this);
        bt_password = (Button) findViewById(R.id.bt_password);
        bt_password.setOnClickListener(this);
        bt_otp = (Button) findViewById(R.id.bt_otp);
        bt_otp.setOnClickListener(this);

        //set default Selected
        bt_otp.setBackground(getDrawable(R.drawable.button_shap_right_round));
        bt_password.setBackground(getDrawable(R.drawable.button_shap_left_round_selected));
        bt_otp.setTextColor(getResources().getColor(R.color.colorWhite));
        bt_password.setTextColor(getResources().getColor(R.color.background_color));

        iv_google=findViewById(R.id.iv_google);
        iv_google.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (ValidateUser(v) == 1) {
                    // getLogin();
//                    Intent intent = new Intent(LoginActivity.this, SkipActivity.class);
                    Intent intent = new Intent(ActivityUserLogin.this, ActivitySubscribe.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.tv_newUser:
                Intent intent = new Intent(ActivityUserLogin.this, ActivityUserRegister.class);
                startActivity(intent);
                break;
            case R.id.bt_password:
                isOtpSelected=false;
                bt_otp.setBackground(getDrawable(R.drawable.button_shap_right_round));
                bt_password.setBackground(getDrawable(R.drawable.button_shap_left_round_selected));
                bt_otp.setTextColor(getResources().getColor(R.color.colorWhite));
                bt_password.setTextColor(getResources().getColor(R.color.background_color));
                break;
            case R.id.bt_otp:
                isOtpSelected=true;
                bt_password.setBackground(getDrawable(R.drawable.button_shap_left_round));
                bt_otp.setBackground(getDrawable(R.drawable.button_shap_right_round_selected));
                bt_password.setTextColor(getResources().getColor(R.color.colorWhite));
                bt_otp.setTextColor(getResources().getColor(R.color.background_color));
                break;
            case R.id.iv_google:
                signInGoogle();
                break;
            default:
                break;
        }
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            Log.e("TAG", account.getDisplayName());

// TODO: 4/10/18 sign in

        } else {
            // TODO: 4/10/18 sign out
        }
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
//    public void getLogin() {
//        RequestQueue mRequestQueue;
//        // Instantiate the cache
//        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
//        // Set up the network to use HttpURLConnection as the HTTP client.
//        Network network = new BasicNetwork(new HurlStack());
//        // Instantiate the RequestQueue with the cache and network.
//        mRequestQueue = new RequestQueue(cache, network);
//        // Start the queue
//        mRequestQueue.start();
//
//        String url = "http://localhost:5000/healthybee-subscription/us-central1/api/auth/login";
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                url,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        if (response.has("statusMessage")) {
//                            try {
//                                if (response.getString("statusMessage").equalsIgnoreCase("ok")) {
//                                    Intent intent = new Intent(LoginActivity.this, SkipActivity.class);
//                                    startActivity(intent);
//                                    // finish();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("1604", "onErrorResponse: " + error.getMessage());
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<>();
//                params.put("email", strLoginId);
//                params.put("password", strPassword);
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("content-type", "application/json");
//                return params;
//            }
//
//        };
//
//        // Add JsonObjectRequest to the RequestQueue
//        mRequestQueue.add(jsonObjectRequest);
//
//
//    }
    private int ValidateUser(View v) {
        strLoginId = edt_login_id.getText().toString().trim();
        strPassword = edt_login_password.getText().toString().trim();
        if (!isValidEmail(strLoginId)) {
            Toast.makeText(this, "Invalid email id", Toast.LENGTH_SHORT).show();
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
}