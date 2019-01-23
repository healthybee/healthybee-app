package com.app.healthybee.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.healthybee.R;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.adapter.AdapterFavourite;
import com.app.healthybee.adapter.AdapterMyOrder;
import com.app.healthybee.models.FavouriteModel;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentFavorite extends Fragment {

    private ArrayList<FavouriteModel> list;
    private View root_view;
    private RecyclerView recyclerView;
    private AdapterFavourite mAdapter;
    private ImageView ivBack;

    private Toolbar toolbar;

    public FragmentFavorite() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @SuppressLint("InflateParams")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_favorite, null);
        toolbar = root_view.findViewById(R.id.toolbarFavorite);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        list = new ArrayList<>();
        addData();
        recyclerView = root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

//        //set data and list adapter
        mAdapter = new AdapterFavourite(getActivity(), list);
        recyclerView.setAdapter(mAdapter);


        ivBack = root_view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).exitApp();
                //finish();
            }
        });
        return root_view;
    }

    private void addData() {
        list.add(new FavouriteModel("Veg Cheese Grilled Toast", "90", "75"));
        list.add(new FavouriteModel("Veg Cheese Grilled Sandwich", "90", "75"));
        list.add(new FavouriteModel("Choco Lava Cake", "90", "75"));
        list.add(new FavouriteModel("Paneer Wraps", "90", "75"));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

//    @Override
//    public void onResume() {
//        showNoItemView(true);
//        if(RealmController.with(this).getNewsSize() > 0){
//            displayData(RealmController.with(this).getNews());
//        } else {
//            showNoItemView(true);
//        }
//        super.onResume();
//    }

//    private void displayData(final List<News> posts) {
//        mAdapter.resetListData();
//        mAdapter.insertData(posts);
//        if (posts.size() == 0) {
//            showNoItemView(true);
//        }
//    }

    private void showNoItemView(boolean show) {
        View lyt_no_item = root_view.findViewById(R.id.lyt_no_item_later);
        ((TextView) root_view.findViewById(R.id.no_item_message)).setText(R.string.no_favorite_found);
        if (show) {
            recyclerView.setVisibility(View.GONE);
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lyt_no_item.setVisibility(View.GONE);
        }
    }
}
