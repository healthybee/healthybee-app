package com.app.healthybee.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
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

import com.android.volley.Request;
import com.app.healthybee.R;
import com.app.healthybee.dboperation.DbHelper;
import com.app.healthybee.fragment.FragmentCheckOut;
import com.app.healthybee.fragment.FragmentFavorite;
import com.app.healthybee.fragment.FragmentHome;
import com.app.healthybee.fragment.FragmentMyOrder;
import com.app.healthybee.fragment.FragmentProfile;
import com.app.healthybee.fragment.FragmentSearch;
import com.app.healthybee.listeners.VolleyResponseListener;
import com.app.healthybee.models.AddressModule;
import com.app.healthybee.models.CartLocal;
import com.app.healthybee.models.CartModule;
import com.app.healthybee.utils.Constant;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.utils.SharedPrefUtil;
import com.app.healthybee.utils.UrlConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements FragmentSearch.OnFragmentInteractionListener {
    private Activity activity;
    public static String strToken;
    private ImageView ivMenu;
    private ImageView ivProfile;
    private ImageView ivMyOrder;
    private ImageView ivNotification;
    private TextView tvProfile;
    private TextView tvCart;
    private TextView tvMenu;
    private TextView tvMyOrder;
    private TextView tvNotification;
    private long exitTime = 0;
    public static boolean mFlagDisplayList = false;

    public static AddressModule address;

    //    private Fragment mContent;
    public ArrayList<CartModule> cartModuleArrayList ;
    private DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = MainActivity.this;
        mFlagDisplayList = false;
        cartModuleArrayList =new ArrayList<>();
        dbHelper=new DbHelper(activity);
        address = new AddressModule();
        setBottomNavigation();
        strToken = SharedPrefUtil.getToken(MainActivity.this);
        RetrieveCarts();
    }

    private void setBottomNavigation() {
        RelativeLayout rlMenu = findViewById(R.id.rlMenu);
        RelativeLayout rlCart = findViewById(R.id.rlCart);
        RelativeLayout rlProfile = findViewById(R.id.rlProfile);
        RelativeLayout rlMyOrder = findViewById(R.id.rlMyOrder);
        RelativeLayout rlNotification = findViewById(R.id.rlNotification);
        tvProfile = findViewById(R.id.tvProfile);
        tvCart = findViewById(R.id.tvCart);
        tvMenu = findViewById(R.id.tvMenu);
        tvMyOrder = findViewById(R.id.tvMyOrder);
        tvNotification = findViewById(R.id.tvNotification);
        ivMenu = findViewById(R.id.ivMenu);
        ivProfile = findViewById(R.id.ivProfile);
        ivMyOrder = findViewById(R.id.ivMyOrder);
        ivNotification = findViewById(R.id.ivNotification);

        ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_selected));
        ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_unselected));
        ivMyOrder.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_orders_unselected));
        ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_drawer_favorite));
        tvMyOrder.setTextColor(Color.parseColor("#C2C2C2"));
        tvNotification.setTextColor(Color.parseColor("#C2C2C2"));
        tvProfile.setTextColor(Color.parseColor("#C2C2C2"));
        tvMenu.setTextColor(Color.parseColor("#FF9900"));

//        Fragment fragment = new FragmentHome();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.container, fragment, "FragmentHome");
//        transaction.addToBackStack(null);
//        transaction.commit();

        rlMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_selected));
                ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_unselected));
                ivMyOrder.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_orders_unselected));
                ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_drawer_favorite));
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
                ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_drawer_favorite));
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
                ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_drawer_favorite));
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
                ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_drawer_favorite));
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
                ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_drawer_favorite));
                tvMyOrder.setTextColor(Color.parseColor("#C2C2C2"));
                tvNotification.setTextColor(Color.parseColor("#FF9900"));
                tvProfile.setTextColor(Color.parseColor("#C2C2C2"));
                tvMenu.setTextColor(Color.parseColor("#C2C2C2"));
                Fragment fragment;
                fragment = new FragmentFavorite();
                loadFragment(fragment, "FragmentFavourite");

            }
        });

    }

    private void loadFragment(Fragment fragment, String fragmentTag) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, fragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //    private void loadFragmentHome(Fragment fragment, String fragmentTag) {
//        // load fragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.container, fragment,fragmentTag);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
    @Override
    public void onBackPressed() {
//        exitApp();
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }

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
                setCountText();
            }
        }, Constant.CARD_UPDATE_TIME_RESUME);
    }


    @SuppressLint("SetTextI18n")
    public void setCountText() {
        int count ;
        ArrayList<CartLocal> list = new ArrayList<>(dbHelper.getCartList());
        if (!list.isEmpty()) {
             count = 0;
            for (int i = 0; i < list.size(); i++) {
                CartLocal cartLocal = list.get(i);
                count = count + cartLocal.getCount();
            }
        } else {
            count = 0;
        }
        tvCart.setText("" + count);
    }

    public int getCount() {
        return Integer.parseInt(tvCart.getText().toString().trim());
    }

    private void RetrieveCarts() {
        dbHelper.deleteAll();
        HashMap<String, String> hashMap = new HashMap<>();
        NetworkConstants.getWebservice(false, activity, Request.Method.GET, UrlConstants.RetrieveCart, hashMap, new VolleyResponseListener<CartModule>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(CartModule[] object, String message) {
                if (object[0] != null) {
                    cartModuleArrayList.addAll(Arrays.asList(object));
                    for (int i = 0; i < cartModuleArrayList.size(); i++) {
                        CartModule cartModule = cartModuleArrayList.get(i);
                        dbHelper.insertUpdateCart(new CartLocal(cartModule.getProductId(),cartModule.getQuantity(),cartModule.getId()));
                    }
                    Fragment fragment = new FragmentHome();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.container, fragment, "FragmentHome");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }

            @Override
            public void onError(String message) {
                Fragment fragment = new FragmentHome();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.container, fragment, "FragmentHome");
                transaction.addToBackStack(null);
                transaction.commit();
                //Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        }, CartModule[].class);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
