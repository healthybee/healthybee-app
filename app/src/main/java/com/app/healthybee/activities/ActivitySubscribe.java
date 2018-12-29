package com.app.healthybee.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.healthybee.R;
import com.app.healthybee.RoundedCornersTransformation;
import com.app.healthybee.utils.SharedPrefUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivitySubscribe extends AppCompatActivity {
    private Button btn_subscribe;
    private TextView tvUserName;
    private Activity activity;
    private CircleImageView profile_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        activity=ActivitySubscribe.this;
        btn_subscribe=findViewById(R.id.btn_subscribe);
        tvUserName=findViewById(R.id.tvUserName);
        profile_image=findViewById(R.id.profile_image);
        btn_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitySubscribe.this, MainActivity.class));
                finish();
            }
        });
        setDefaultValue();

    }

    private void setDefaultValue() {
        tvUserName.setText(SharedPrefUtil.getUserName(activity));
        Glide.with(activity)
                .load(SharedPrefUtil.getUserPicture(activity).replace(" ", "%20"))
                .apply(RequestOptions
                        .bitmapTransform(new RoundedCornersTransformation(activity,10, 0))
                        .error(R.drawable.ic_profile_unselected))
                .into(profile_image);
    }
}
