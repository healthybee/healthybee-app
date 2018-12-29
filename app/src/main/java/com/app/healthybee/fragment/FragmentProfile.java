package com.app.healthybee.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.healthybee.RoundedCornersTransformation;
import com.app.healthybee.activities.ActivityUserLogin;
import com.app.healthybee.utils.Config;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.R;
import com.app.healthybee.adapter.AdapterAbout;
import com.app.healthybee.adapter.AdapterPause;
import com.app.healthybee.models.Pause;
import com.app.healthybee.utils.Constant;
import com.app.healthybee.utils.SharedPrefUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;


public class FragmentProfile extends Fragment {

    private View root_view;
    private TextView txtUserEmail,txtUserMobile,txtUserName;
    private TextView tv_logout;
    private TextView txtedit;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private AdapterAbout adapterAbout;
    private LinearLayout lyt_root;
    private ImageView ivPauseNextDay;
    private CircularImageView civProfileImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_profile, null);
        lyt_root = root_view.findViewById(R.id.root_layout);

       // myApplication = Applications.getInstance();
        txtUserEmail= root_view.findViewById(R.id.txtUserEmail);
        civProfileImage= root_view.findViewById(R.id.civProfileImage);
        txtUserMobile= root_view.findViewById(R.id.txtUserMobile);
        txtUserName= root_view.findViewById(R.id.txtUserName);
        tv_logout= root_view.findViewById(R.id.tv_logout);
        txtedit = root_view.findViewById(R.id.txtedit);
        ivPauseNextDay = root_view.findViewById(R.id.ivPauseNextDay);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getResources().getString(R.string.title_please_wait));
        progressDialog.setMessage(getResources().getString(R.string.logout_process));
        progressDialog.setCancelable(false);

        recyclerView = root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterAbout = new AdapterAbout(getDataInformation(), getActivity());
        recyclerView.setAdapter(adapterAbout);

        setProfileData();

        if (Config.ENABLE_RTL_MODE) {
            lyt_root.setRotationY(180);
        }

        adapterAbout.setOnItemClickListener(new AdapterAbout.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Data obj, int position) {
                if (position == 0) {
                    // TODO: 22/9/18
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction().addToBackStack("null");
                    FragmentNotification f1 = new FragmentNotification();
                    fragmentTransaction.replace(R.id.container, f1);
                    fragmentTransaction.commit();
                }
                if (position == 1) {

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction().addToBackStack("null");
                    FragmentAddress f1 = new FragmentAddress();
                    fragmentTransaction.replace(R.id.container, f1);
                    fragmentTransaction.commit();

                } else if (position == 2) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends awesome app for you HealthyBee");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.play_more_apps))));
                } else if (position == 3) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction().addToBackStack("null");
                    FragmentMyOrder f1 = new FragmentMyOrder();
                    fragmentTransaction.replace(R.id.container, f1);
                    fragmentTransaction.commit();
                }
                else if (position == 4) {
                    // TODO: 20/9/18
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction().addToBackStack("null");
                    FragmentDeliverySupport f1 = new FragmentDeliverySupport();
                    fragmentTransaction.replace(R.id.container, f1);
                    fragmentTransaction.commit();
                }

            }
        });
        ivPauseNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOrderDialog();
            }
        });

        txtedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction().addToBackStack("null");
                FragmentEditProfile f1 = new FragmentEditProfile();
                fragmentTransaction.replace(R.id.container, f1);
                fragmentTransaction.commit();
            }
        });
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();

            }
        });
        return root_view;
    }


    private void setProfileData() {
        txtUserName.setText(SharedPrefUtil.getUserName(getActivity()));
        txtUserEmail.setText(SharedPrefUtil.getUserEmail(getActivity()));
        txtUserMobile.setText(SharedPrefUtil.getUserMobile(getActivity()));
        Glide.with(getActivity())
                .load(SharedPrefUtil.getUserPicture(getActivity()).replace(" ", "%20"))
                .apply(RequestOptions
                        .bitmapTransform(new RoundedCornersTransformation(getActivity(),10, 0))
                        .error(R.drawable.ic_profile_unselected))
                .into(civProfileImage);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.logout_title);
        builder.setMessage(R.string.logout_message);
        builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface di, int i) {

               // Applications.getInstance().saveIsLogin(false);
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(R.string.logout_success);
                        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPrefUtil.setIsLogin(getActivity(),false);
                                startActivity(new Intent(getActivity(),ActivityUserLogin.class));
                                getActivity().finishAffinity();
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();
                    }
                }, Constant.DELAY_PROGRESS_DIALOG);

            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.show();

    }

    private List<Data> getDataInformation() {

        List<Data> data = new ArrayList<>();

        data.add(new Data(
                R.drawable.ic_drawer_privacy, "Payment Cards",
                "Add a credit or debit card"
        ));

        data.add(new Data(
                R.drawable.ic_drawer_rate,
                "Address",
                "Add or remove a delivery address"
        ));

        data.add(new Data(
                R.drawable.ic_refund,
               "Refer Friends",
               "Get Rs. 100.00 FREE"
        ));

        data.add(new Data(
                R.drawable.ic_my_orders,
              "My Orders",
                ""
        ));
        data.add(new Data(
                R.drawable.ic_delivery_support,
                "Delivery Support",
                ""
        ));

        data.add(new Data(
                R.drawable.ic_refund,
                "Refund / Cancellation Policy",
                ""
        ));
        return data;
    }

    public class Data {
        private int image;
        private String title;
        private String sub_title;

        public int getImage() {
            return image;
        }

        public String getTitle() {
            return title;
        }

        public String getSub_title() {
            return sub_title;
        }

        public Data(int image, String title, String sub_title) {
            this.image = image;
            this.title = title;
            this.sub_title = sub_title;
        }
    }

    public void myOrderDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog_myorder);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        ((TextView) dialog.findViewById(R.id.tv_pauseForNext))
//                .setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
//        final View mView = layoutInflaterAndroid.inflate(R.layout.custom_dialog_myorder, null);
        final ArrayList<Pause> pauseArrayList = new ArrayList<>();
        RecyclerView itemsList=dialog.findViewById(R.id.recyclerView_order_dialog);
        TextView bt_pauseForNext=dialog.findViewById(R.id.tv_pauseForNext);
        TextView tv_cancel=dialog.findViewById(R.id.tv_cancel);
        itemsList.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        itemsList.setLayoutManager(mLinearLayoutManager);

        pauseArrayList.add(
                new Pause(
                        "First Item",
                        false
                ));

        pauseArrayList.add(
                new Pause(
                        "Second Item",
                        false
                ));
        pauseArrayList.add(
                new Pause(
                        "Third Item",
                        false
                ));
        pauseArrayList.add(
                new Pause(
                        "Forth Item",
                        false
                ));
        pauseArrayList.add(
                new Pause(
                        "Fifth Item Fifth Item",
                        false
                ));
        pauseArrayList.add(
                new Pause(
                        "Third Item",
                        false
                ));

        AdapterPause adapter = new AdapterPause(getActivity(), pauseArrayList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("TAG", "clicked position:" + position);
                String postId = pauseArrayList.get(position).getOrderTitle();
                Toast.makeText(getActivity(), postId, Toast.LENGTH_SHORT).show();

            }
        });
        itemsList.setAdapter(adapter);
//        final Dialog alert = new Dialog(getActivity());
//        alert.setView(mView);
//        alert.setCancelable(false);
//
        bt_pauseForNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
