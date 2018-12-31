package com.app.healthybee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.app.healthybee.R;
import com.app.healthybee.utils.Config;
import com.app.healthybee.utils.SharedPrefUtil;

public class ActivitySplash extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        new CountDownTimer(Config.SPLASH_TIME, 1000) {
            @Override
            public void onFinish() {
                Intent intent;
                if (SharedPrefUtil.getIsLogin(ActivitySplash.this)) {
                    intent = new Intent(getBaseContext(), MainActivity.class);
//                    intent = new Intent(getBaseContext(), TempActivity.class);

                } else {
                    intent = new Intent(getBaseContext(), ActivityUserLogin.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();

    }
}
