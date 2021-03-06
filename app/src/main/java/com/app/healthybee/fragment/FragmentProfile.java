package com.app.healthybee.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.healthybee.RoundedCornersTransformation;
import com.app.healthybee.activities.ActivityUserLogin;
import com.app.healthybee.activities.MainActivity;
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
import java.util.Objects;


public class FragmentProfile extends Fragment {

    private View root_view;
    private TextView txtUserEmail;
    private TextView txtUserMobile;
    private TextView txtUserName;
    private ImageView imageViewLogout;
    private ImageView ivEditProfile;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private AdapterAbout adapterAbout;
    private CircularImageView civProfileImage;
    private Toolbar toolbar;
    private ImageView ivBack;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.raw_pro, null);
        toolbar = root_view.findViewById(R.id.toolbarProfile);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        ivBack = root_view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).exitApp();
                //finish();
            }
        });
        txtUserEmail= root_view.findViewById(R.id.txtUserEmail);
        civProfileImage= root_view.findViewById(R.id.civProfileImage);
        txtUserMobile= root_view.findViewById(R.id.txtUserMobile);
        txtUserName= root_view.findViewById(R.id.txtUserName);
        imageViewLogout= root_view.findViewById(R.id.imageViewLogout);
        ivEditProfile = root_view.findViewById(R.id.ivEditProfile);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getResources().getString(R.string.title_please_wait));
        progressDialog.setMessage(getResources().getString(R.string.logout_process));
        progressDialog.setCancelable(false);


        recyclerView = root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterAbout = new AdapterAbout(getDataInformation(), getActivity());
        recyclerView.setAdapter(adapterAbout);

        setProfileData();

        adapterAbout.setOnItemClickListener(new AdapterAbout.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Data obj, int position) {
                if (position == 0) {
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

                } else if (position == 3) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction().addToBackStack("null");
                    FragmentDeliverySupport f1 = new FragmentDeliverySupport();
                    fragmentTransaction.replace(R.id.container, f1);
                    fragmentTransaction.commit();
                }
                else if (position == 4) {
                    //  31/12/18 Add refund balance
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction().addToBackStack("null");
                    FragmentDeliverySupport f1 = new FragmentDeliverySupport();
                    fragmentTransaction.replace(R.id.container, f1);
                    fragmentTransaction.commit();
                }

            }
        });

        ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction().addToBackStack("null");
                FragmentEditProfile f1 = new FragmentEditProfile();
                fragmentTransaction.replace(R.id.container, f1);
                fragmentTransaction.commit();
            }
        });
        imageViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();

            }
        });
        return root_view;
    }


    private void setProfileData() {
        txtUserName.setText(SharedPrefUtil.getUserName(Objects.requireNonNull(getActivity())));
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
