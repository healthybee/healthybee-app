package com.app.healthybee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.app.healthybee.R;

public class ActivitySubscribe extends AppCompatActivity {
    private Button btn_subscribe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        btn_subscribe=findViewById(R.id.btn_subscribe);
        btn_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitySubscribe.this, MainActivity.class));
                finish();
            }
        });
    }
}
