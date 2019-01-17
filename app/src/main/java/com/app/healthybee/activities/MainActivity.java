package com.app.healthybee.activities;

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
import com.app.healthybee.fragment.FragmentMyOrder;
import com.app.healthybee.fragment.FragmentNotification;
import com.app.healthybee.fragment.FragmentProfile;
import com.app.healthybee.models.AddressModule;
import com.app.healthybee.models.CategoryItem;
import com.app.healthybee.utils.Constant;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout rlMenu, rlCart, rlProfile,rlMyOrder,rlNotification;
    private ImageView ivMenu, ivProfile,ivMyOrder,ivNotification;
    private TextView tvProfile, tvCart, tvMenu,tvMyOrder,tvNotification;
    private long exitTime = 0;
    private int count = 0;
    private DbHelper dbHelper;
    private ArrayList<CategoryItem> list;
    public static boolean mFlagDisplayList = false;

    public static AddressModule address;
//    private Fragment mContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (null!=savedInstanceState)
//        mContent = getSupportFragmentManager().getFragment(savedInstanceState, "");
        mFlagDisplayList = false;
        address=new AddressModule();
        setBottomNavigation();
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        getSupportFragmentManager().putFragment(outState, "", mContent);
//    }

    private void setBottomNavigation() {
        rlMenu = findViewById(R.id.rlMenu);
        rlCart = findViewById(R.id.rlCart);
        rlProfile = findViewById(R.id.rlProfile);
        rlMyOrder = findViewById(R.id.rlMyOrder);
        rlNotification = findViewById(R.id.rlNotification);
        tvProfile = findViewById(R.id.tvProfile);
        tvCart = findViewById(R.id.tvCart);
        tvMenu = findViewById(R.id.tvMenu);
        tvMyOrder = findViewById(R.id.tvMyOrder);
        tvNotification = findViewById(R.id.tvNotification);
        ivMenu = findViewById(R.id.ivMenu);
        ivProfile = findViewById(R.id.ivProfile);
        ivMyOrder = findViewById(R.id.ivMyOrder);
        ivNotification = findViewById(R.id.ivNotification);

        dbHelper = new DbHelper(getApplicationContext());
        count = 0;

        ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_selected));
        ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_unselected));
        ivMyOrder.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_orders_unselected));
        ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification_unselected));
        tvMyOrder.setTextColor(Color.parseColor("#C2C2C2"));
        tvNotification.setTextColor(Color.parseColor("#C2C2C2"));
        tvProfile.setTextColor(Color.parseColor("#C2C2C2"));
        tvMenu.setTextColor(Color.parseColor("#FF9900"));
        Fragment fragment;
        fragment = new FragmentHome();
        loadFragment(fragment,"FragmentHome");

        rlMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_selected));
                ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_unselected));
                ivMyOrder.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_orders_unselected));
                ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification_unselected));
                tvMyOrder.setTextColor(Color.parseColor("#C2C2C2"));
                tvNotification.setTextColor(Color.parseColor("#C2C2C2"));
                tvProfile.setTextColor(Color.parseColor("#C2C2C2"));
                tvMenu.setTextColor(Color.parseColor("#FF9900"));
                Fragment fragment;
                fragment = new FragmentHome();
                loadFragment(fragment, "FragmentHome");

            }
        });
        rlCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_unselected));
                ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_unselected));
                ivMyOrder.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_orders_unselected));
                ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification_unselected));
                tvMyOrder.setTextColor(Color.parseColor("#C2C2C2"));
                tvNotification.setTextColor(Color.parseColor("#C2C2C2"));
                tvProfile.setTextColor(Color.parseColor("#C2C2C2"));
                tvMenu.setTextColor(Color.parseColor("#C2C2C2"));
                Fragment fragment;
                fragment = new FragmentCheckOut();
                loadFragment(fragment, "FragmentCheckOut");

            }
        });
        rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_unselected));
                ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_selected));
                ivMyOrder.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_orders_unselected));
                ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification_unselected));
                tvMyOrder.setTextColor(Color.parseColor("#C2C2C2"));
                tvNotification.setTextColor(Color.parseColor("#C2C2C2"));
                tvProfile.setTextColor(Color.parseColor("#FF9900"));
                tvMenu.setTextColor(Color.parseColor("#C2C2C2"));
                Fragment fragment;
                fragment = new FragmentProfile();
                loadFragment(fragment, "FragmentProfile");


            }
        });

        rlMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_unselected));
                ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_unselected));
                ivMyOrder.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_orders));
                ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification_unselected));
                tvMyOrder.setTextColor(Color.parseColor("#FF9900"));
                tvNotification.setTextColor(Color.parseColor("#C2C2C2"));
                tvProfile.setTextColor(Color.parseColor("#C2C2C2"));
                tvMenu.setTextColor(Color.parseColor("#C2C2C2"));
                Fragment fragment;
                fragment = new FragmentMyOrder();
                loadFragment(fragment, "FragmentMyOrder");
            }
        });

        rlNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_unselected));
                ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_unselected));
                ivMyOrder.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_orders_unselected));
                ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_selected));
                tvMyOrder.setTextColor(Color.parseColor("#C2C2C2"));
                tvNotification.setTextColor(Color.parseColor("#FF9900"));
                tvProfile.setTextColor(Color.parseColor("#C2C2C2"));
                tvMenu.setTextColor(Color.parseColor("#C2C2C2"));
                Fragment fragment;
                fragment = new FragmentNotification();
                loadFragment(fragment, "FragmentNotification");

            }
        });

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setCountText(0);
            }
        }, Constant.CARD_UPDATE_TIME_RESUME);
    }


    public void setCountText(int count) {
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
        tvCart.setText("" + count);
    }
    public int getCount() {
        return Integer.parseInt(tvCart.getText().toString().trim());
    }
}
