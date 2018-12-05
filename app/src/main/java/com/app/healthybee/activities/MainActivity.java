package com.app.healthybee.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.healthybee.R;
import com.app.healthybee.dboperation.DbHelper;
import com.app.healthybee.fragment.FragmentCheckOut;
import com.app.healthybee.fragment.FragmentHome;
import com.app.healthybee.fragment.FragmentProfile;
import com.app.healthybee.models.CategoryItem;
import com.app.healthybee.utils.Constant;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout rlMenu, rlCart, rlProfile;
    private ImageView ivMenu, ivProfile,ivDownArrow;
    private TextView tvProfile, tvCart, tvMenu;
    private long exitTime = 0;
    private int count = 0;
    private DbHelper dbHelper;
    private ArrayList<CategoryItem> list;
    private ImageView ivSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setBottomNavigation();
    }

    private void initView() {

    }

    private void setBottomNavigation() {
        rlMenu = findViewById(R.id.rlMenu);
        rlCart = findViewById(R.id.rlCart);
        rlProfile = findViewById(R.id.rlProfile);
        tvProfile = findViewById(R.id.tvProfile);
        tvCart = findViewById(R.id.tvCart);
        tvMenu = findViewById(R.id.tvMenu);
        ivMenu = findViewById(R.id.ivMenu);
        ivProfile = findViewById(R.id.ivProfile);

        dbHelper = new DbHelper(getApplicationContext());
        count = 0;

        ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_room_service_orange_24dp));
        ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_gray_24dp));
        tvProfile.setTextColor(Color.parseColor("#C2C2C2"));
        tvMenu.setTextColor(Color.parseColor("#FF9900"));

        Fragment fragment;
        fragment = new FragmentHome();
        loadFragment(fragment);

        rlMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_room_service_orange_24dp));
                ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_gray_24dp));
                tvProfile.setTextColor(Color.parseColor("#C2C2C2"));
                tvMenu.setTextColor(Color.parseColor("#FF9900"));
                Fragment fragment;
                fragment = new FragmentHome();
                loadFragment(fragment);

            }
        });
        rlCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_room_service_gray_24dp));
                ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_gray_24dp));
                tvProfile.setTextColor(Color.parseColor("#C2C2C2"));
                tvMenu.setTextColor(Color.parseColor("#C2C2C2"));
                Fragment fragment;
                fragment = new FragmentCheckOut();
                loadFragment(fragment);

            }
        });
        rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_room_service_gray_24dp));
                ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_orange_24dp));
                tvProfile.setTextColor(Color.parseColor("#FF9900"));
                tvMenu.setTextColor(Color.parseColor("#C2C2C2"));
                Fragment fragment;
                fragment = new FragmentProfile();
                loadFragment(fragment);
            }
        });

        ivSearch = findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivitySearch.class);
                startActivity(intent);
            }
        });
        ivDownArrow= findViewById(R.id.ivDownArrow);
        ivDownArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 29/11/18  address popup showing
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        exitApp();

    }

    public void exitApp() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
       /* } else if (getSupportFragmentManager().getBackStackEntryCount() == 2) {
            super.onBackPressed();*/
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setCountText();
            }
        }, Constant.CARD_UPDATE_TIME_RESUME);
    }


    public void setCountText() {
        list = new ArrayList<>();
        list.addAll(dbHelper.getCartList());
        if (!list.isEmpty()) {
            count = 0;
            for (int i = 0; i < list.size(); i++) {
                CategoryItem categoryItem = list.get(i);
                count = count + categoryItem.getCount();
            }
        } else {
            count = 0;
        }
        tvCart.setText("" + count);
    }
}
