package com.app.healthybee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.app.healthybee.R;
import com.app.healthybee.utils.Config;

public class ActivitySplash extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        new CountDownTimer(Config.SPLASH_TIME, 1000) {
            @Override
            public void onFinish() {
                Intent intent = new Intent(getBaseContext(), ActivityUserLogin.class);
//                Intent intent = new Intent(getBaseContext(), ActivityLogin.class);
//                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();

    }
}
