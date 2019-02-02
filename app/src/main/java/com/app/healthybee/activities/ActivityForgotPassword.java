package com.app.healthybee.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.healthybee.MyApplication;
import com.app.healthybee.utils.Config;
import com.app.healthybee.R;
import com.app.healthybee.utils.MyCustomProgressDialog;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.utils.UrlConstants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityForgotPassword extends AppCompatActivity {

    private EditText etUserEmail;
    private Activity activity;

    private String strUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forgot);
        activity = ActivityForgotPassword.this;
        if (Config.ENABLE_RTL_MODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        etUserEmail = findViewById(R.id.etUserEmail);
        Button btnForgot = findViewById(R.id.btnForgot);
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strUserEmail = etUserEmail.getText().toString();
                if (strUserEmail.isEmpty()) {
                    Toast.makeText(ActivityForgotPassword.this, "Please enter email id", Toast.LENGTH_SHORT).show();
                } else {
                    if (!isValidEmail(strUserEmail)) {
                        Toast.makeText(ActivityForgotPassword.this, "Please enter valid email id", Toast.LENGTH_SHORT).show();
                    } else {
                        SendEmailForForgotPassword();
                    }
                }
            }
        });

    }

    private void SendEmailForForgotPassword() {
        if (NetworkConstants.isConnectingToInternet(activity)) {
            MyCustomProgressDialog.showDialog(activity, getString(R.string.please_wait));
            Map<String, String> params = new HashMap<>();
            params.put("email", strUserEmail);
            params.put("link", "test");// TODO: 21/1/19  add linc for forgot password
            Log.d("4343", new JSONObject(params).toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    UrlConstants.SendEmail,
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("4343", response.toString());
                            MyCustomProgressDialog.dismissDialog();
                            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                            dialog.setTitle(R.string.register_title);
                            dialog.setMessage(R.string.register_success);
                            dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
                            dialog.setCancelable(false);
                            dialog.show();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("4343", "Site Info Error: " + error.getMessage());
                    Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                    MyCustomProgressDialog.dismissDialog();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            Log.d("4343", jsonObjectRequest.toString());
            MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(activity, getString(R.string.network_title), getString(R.string.network_message));
        }
    }

    public boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}


