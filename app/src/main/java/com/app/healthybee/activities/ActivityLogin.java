package com.app.healthybee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.healthybee.R;

public class ActivityLogin extends AppCompatActivity {

private Button btn_generate_otp,btn_verify_otp;
private LinearLayout ll_login,ll_otp;
private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ll_login=findViewById(R.id.ll_login);
        ll_otp=findViewById(R.id.ll_otp);
        ll_login.setVisibility(View.VISIBLE);
        ll_otp.setVisibility(View.GONE);
        imageView=findViewById(R.id.imageView);
        imageView.setVisibility(View.INVISIBLE);
        btn_generate_otp=findViewById(R.id.btn_generate_otp);
        btn_generate_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_login.setVisibility(View.GONE);
                ll_otp.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_login.setVisibility(View.VISIBLE);
                ll_otp.setVisibility(View.GONE);
                imageView.setVisibility(View.INVISIBLE);
            }
        });

        btn_verify_otp=findViewById(R.id.btn_verify_otp);
        btn_verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(),MainActivity.class));
               finish();
            }
        });
    }
}
