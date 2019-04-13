package com.app.healthybee.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.app.healthybee.AuthUser;
import com.app.healthybee.R;
import com.app.healthybee.RetrofitClient;
import com.app.healthybee.User;
import com.app.healthybee.utils.Constant;
import com.app.healthybee.utils.SharedPrefUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityUserLogin extends AppCompatActivity implements View.OnClickListener {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_otp);
        activity = ActivityUserLogin.this;
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (ValidateUser() == 1) {
                    AuthUser1();
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

    private void AuthUser1() {
        final ProgressDialog progressDialog = new ProgressDialog(ActivityUserLogin.this);
        progressDialog.setTitle(getResources().getString(R.string.title_please_wait));
        progressDialog.setMessage(getResources().getString(R.string.login_process));
        progressDialog.setCancelable(false);
        progressDialog.show();
        String credentials = strLoginId + ":" + strPassword;
        String basic_auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Call<AuthUser> call =RetrofitClient.getInstance().getApi().AuthUser(basic_auth,strLoginId,strPassword);
        call.enqueue(new Callback<AuthUser>() {
            @Override
            public void onResponse(Call<AuthUser> call, Response<AuthUser> response) {
                if (response.code() == 201) {
                    Log.d("4343", response.toString());
                    AuthUser authUser = response.body();
                    assert authUser != null;
                    SharedPrefUtil.setToken(activity, authUser.getToken());
                    User user=authUser.getUser();
                    SharedPrefUtil.setUserId(activity, user.getId());
                    SharedPrefUtil.setUserName(activity, user.getName());
                    SharedPrefUtil.setUserPicture(activity, user.getPicture());
                    SharedPrefUtil.setUserEmail(activity, user.getEmail());
                    SharedPrefUtil.setUserMobile(activity, user.getMobile());
                    SharedPrefUtil.setCreatedAt(activity, user.getCreatedAt());
                    // for login session set from local
                    SharedPrefUtil.setUserPassword(activity, strPassword);
                    SharedPrefUtil.setIsLogin(activity, true);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Intent intent = new Intent(activity, ActivitySubscribe.class);
                            startActivity(intent);
                            finish();
                        }
                    }, Constant.DELAY_PROGRESS_DIALOG);

                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuthUser> call, Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
