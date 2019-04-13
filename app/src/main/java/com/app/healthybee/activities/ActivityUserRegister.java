package com.app.healthybee.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.app.healthybee.ErrorResponse;
import com.app.healthybee.RetrofitClient;
import com.app.healthybee.models.Register;
import com.app.healthybee.utils.Config;
import com.app.healthybee.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityUserRegister extends AppCompatActivity {
    private EditText edt_password;
    private EditText edt_mobile;
    private EditText edt_email;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        activity = ActivityUserRegister.this;
        if (Config.ENABLE_RTL_MODE) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        edt_mobile = findViewById(R.id.edt_mobile);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);

        TextView txt_terms = findViewById(R.id.txt_terms);
        Button btn_register = findViewById(R.id.btn_register);
        Button btn_login = findViewById(R.id.btn_login);

        txt_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), ActivityPrivacyPolicy.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivityUserLogin.class));
                finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //registerUser();
                registerUser1();
            }
        });
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


//    private void registerUser() {
//        if (NetworkConstants.isConnectingToInternet(activity)) {
//            MyCustomProgressDialog.showDialog(activity, getString(R.string.please_wait));
//            Map<String, String> params = new HashMap<>();
//            params.put("email", edt_email.getText().toString());
//            params.put("password", edt_password.getText().toString());
//            params.put("mobile", edt_mobile.getText().toString());
//            Log.d("4343", new JSONObject(params).toString());
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                    Request.Method.POST,
//                    UrlConstants.createUser,
//                    new JSONObject(params),
//                    new Response.Listener<JSONObject>() {
//
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Log.d("4343", response.toString());
//                            MyCustomProgressDialog.dismissDialog();
//                            AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityUserRegister.this);
//                            dialog.setTitle(R.string.register_title);
//                            dialog.setMessage(R.string.register_success);
//                            dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    Intent intent = new Intent(getApplicationContext(), ActivityUserLogin.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            });
//                            dialog.setCancelable(false);
//                            dialog.show();
//
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("4343", "Site Info Error: " + error.getMessage());
//                    Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
//                    MyCustomProgressDialog.dismissDialog();
//                }
//            }) {
//
//                @Override
//                public Map<String, String> getHeaders() {
//                    Map<String, String> headers = new HashMap<>();
//                    headers.put("Content-Type", "application/json");
//                    return headers;
//                }
//            };
//            Log.d("4343", jsonObjectRequest.toString());
//            MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
//
//        } else {
//            MyCustomProgressDialog.showAlertDialogMessage(activity, getString(R.string.network_title), getString(R.string.network_message));
//        }
//    }
    private void registerUser1() {
        Call<Register> call =RetrofitClient.getInstance().getApi().CreateUser(edt_email.getText().toString(),edt_password.getText().toString(),edt_mobile.getText().toString());
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (response.code() == 201) {
                    Register register = response.body();
                    Toast.makeText(activity, register.getName(), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 409) {
                    Gson gson = new GsonBuilder().create();
                    ErrorResponse mError = new ErrorResponse();
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        Toast.makeText(getApplicationContext(), mError.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        // handle failure to read error
                    }
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

