package com.app.healthybee.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.healthybee.utils.Config;
import com.app.healthybee.R;
import com.app.healthybee.utils.Constant;
import com.app.healthybee.utils.NetworkCheck;
//import com.app.healthybee.utils.validation.Validator;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class ActivityUserRegister extends AppCompatActivity {


    EditText edtPassword;



    Button btnsignup, btn_login;
    TextView txt_terms;
    String strFullname, strEmail, strPassword, strMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        if (Config.ENABLE_RTL_MODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

//        edtFullName = findViewById(R.id.edt_user);
//        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);

        txt_terms = findViewById(R.id.txt_terms);
        btnsignup = findViewById(R.id.btn_update);
        btn_login = findViewById(R.id.btn_login);

        txt_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), ActivityPrivacyPolicy.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), ActivityUserLogin.class));
            }
        });

//        btnsignup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                validator.validateAsync();
//            }
//        });
//
//        validator = new Validator(this);
//        validator.setValidationListener(this);

        setupToolbar();

    }

    public void setupToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setTitle("");
        }

        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        if (appBarLayout.getLayoutParams() != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
            appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return false;
                }
            });
            layoutParams.setBehavior(appBarLayoutBehaviour);
        }
    }

//    @Override
//    public void onValidationSucceeded() {
//        strFullname = edtFullName.getText().toString().replace(" ", "%20");
//        strEmail = edtEmail.getText().toString();
//        strPassword = edtPassword.getText().toString();
//
//
//        if (NetworkCheck.isNetworkAvailable(ActivityUserRegister.this)) {
//            new MyTaskRegister().execute(Constant.REGISTER_URL + strFullname + "&email=" + strEmail + "&password=" + strPassword);
//        } else {
//            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//            dialog.setTitle(R.string.whops);
//            dialog.setMessage(R.string.msg_no_network);
//            dialog.setPositiveButton(R.string.dialog_ok, null);
//            dialog.setCancelable(false);
//            dialog.show();
//        }
//
//    }
//
//    @Override
//    public void onValidationFailed(View failedView, Rule<?> failedRule) {
//        String message = failedRule.getFailureMessage();
//        if (failedView instanceof EditText) {
//            failedView.requestFocus();
//            ((EditText) failedView).setError(message);
//        } else {
//            Toast.makeText(this, "Record Not Saved", Toast.LENGTH_SHORT).show();
//        }
//    }

    private class MyTaskRegister extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityUserRegister.this);
            progressDialog.setTitle(getResources().getString(R.string.title_please_wait));
            progressDialog.setMessage(getResources().getString(R.string.register_process));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return NetworkCheck.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (null == result || result.length() == 0) {
                showToast("No Data Found!!!");
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.CATEGORY_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        strMessage = objJson.getString(Constant.MSG);
                        Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != progressDialog && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        setResult();
                    }
                }, Constant.DELAY_PROGRESS_DIALOG);
            }

        }
    }

    public void setResult() {

        if (Constant.GET_SUCCESS_MSG == 0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityUserRegister.this);
            dialog.setTitle(R.string.whops);
            dialog.setMessage(R.string.register_exist);
            dialog.setPositiveButton(R.string.dialog_ok, null);
            dialog.setCancelable(false);
            dialog.show();

//            edtEmail.setText("");
//            edtEmail.requestFocus();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityUserRegister.this);
            dialog.setTitle(R.string.register_title);
            dialog.setMessage(R.string.register_success);
            dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getApplicationContext(), ActivityUserLogin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(ActivityUserRegister.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}

