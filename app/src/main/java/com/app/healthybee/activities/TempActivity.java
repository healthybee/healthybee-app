package com.app.healthybee.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.healthybee.R;
import com.app.healthybee.fragment.FragmentCheckOut;
import com.app.healthybee.fragment.FragmentHome;
import com.app.healthybee.fragment.FragmentMyOrder;
import com.app.healthybee.fragment.FragmentNotification;
import com.app.healthybee.fragment.FragmentProfile;
import com.app.healthybee.models.AddressModule;
import com.app.healthybee.models.CategoryItem;


import java.util.ArrayList;

public class TempActivity extends AppCompatActivity {
    private long exitTime = 0;
    private int count = 0;
    private ArrayList<CategoryItem> list;
    private ImageView ivSearch;
    //private RelativeLayout relativeLayout;
    public static boolean mFlagDisplayList = false;
    BottomNavigationView navigation1 ;
    BottomNavigationView navigation2 ;
    public static AddressModule address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp1);
        mFlagDisplayList = false;
        address=new AddressModule();
         navigation1 = (BottomNavigationView) findViewById(R.id.bottomnav);
         navigation2 = (BottomNavigationView) findViewById(R.id.bottomnav1);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        navigation2.setItemTextColor(ColorStateList.valueOf(Color.GRAY));
        navigation2.setItemIconTintList(ColorStateList.valueOf(Color.GRAY));
        navigation1.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        navigation2.setItemTextColor(ColorStateList.valueOf(Color.GRAY));
                        navigation2.setItemIconTintList(ColorStateList.valueOf(Color.GRAY));
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                selectedFragment = FragmentHome.newInstance();
//                                navigation1.setItemTextColor(ColorStateList.valueOf(getColor(R.color.bottom_nav_color)));
//                                navigation1.setItemIconTintList(ColorStateList.valueOf(getColor(R.color.bottom_nav_color)));
                                break;
                            case R.id.navigation_notification:
                                selectedFragment = FragmentNotification.newInstance();
//                                navigation1.setItemTextColor(ColorStateList.valueOf(getColor(R.color.bottom_nav_color)));
//                                navigation1.setItemIconTintList(ColorStateList.valueOf(getColor(R.color.bottom_nav_color)));
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });
        navigation2.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        navigation1.setItemTextColor(ColorStateList.valueOf(Color.GRAY));
                        navigation1.setItemIconTintList(ColorStateList.valueOf(Color.GRAY));
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_my_order:
                                selectedFragment = FragmentMyOrder.newInstance();
//                                navigation2.setItemTextColor(ColorStateList.valueOf(getColor(R.color.bottom_nav_color)));
//                                navigation2.setItemIconTintList(ColorStateList.valueOf(getColor(R.color.bottom_nav_color)));
                                break;
                            case R.id.navigation_profile:
                                selectedFragment = FragmentProfile.newInstance();
//                                navigation2.setItemTextColor(ColorStateList.valueOf(getColor(R.color.bottom_nav_color)));
//                                navigation2.setItemIconTintList(ColorStateList.valueOf(getColor(R.color.bottom_nav_color)));
                                break;

                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, FragmentCheckOut.newInstance());
                transaction.commit();
            }
        });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, FragmentHome.newInstance());
        transaction.commit();
    }


    private void loadFragment(Fragment fragment, String fragmentTag) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment,fragmentTag);
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
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            //    setCountText();
//            }
//        }, Constant.CARD_UPDATE_TIME_RESUME);
    }


//    public void setCountText() {
//        list = new ArrayList<>();
//        list.addAll(dbHelper.getCartList());
//        if (!list.isEmpty()) {
//            count = 0;
//            for (int i = 0; i < list.size(); i++) {
//                CategoryItem categoryItem = list.get(i);
//                count = count + categoryItem.getCount();
//            }
//        } else {
//            count = 0;
//        }
//        tvCart.setText("" + count);
//    }
}

