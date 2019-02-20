package com.app.healthybee.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.healthybee.R;
import com.app.healthybee.activities.MainActivity;


public class FragmentNotification extends Fragment {

    private View root_view;
    private Toolbar toolbar;
    private ImageView ivBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_notification, null);
        toolbar = root_view.findViewById(R.id.toolbarNotification);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ivBack = root_view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).exitApp();
            }
        });


        return root_view;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
