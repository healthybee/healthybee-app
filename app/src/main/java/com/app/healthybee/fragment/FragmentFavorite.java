package com.app.healthybee.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.healthybee.R;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.adapter.AdapterFavourite;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.listeners.VolleyResponseListener;
import com.app.healthybee.models.CategoryItem;
import com.app.healthybee.models.FavouriteModel;
import com.app.healthybee.models.ResultFavorite;
import com.app.healthybee.utils.ListPaddingDecoration;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.utils.UrlConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FragmentFavorite extends Fragment {

    private ArrayList<FavouriteModel> favouriteModelArrayList;
    private View root_view;
    private RecyclerView recyclerView;
    private AdapterFavourite mAdapter;

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
        Toolbar toolbar = root_view.findViewById(R.id.toolbarFavorite);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        favouriteModelArrayList = new ArrayList<>();
        recyclerView = root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new ListPaddingDecoration(getActivity()));
        recyclerView.setHasFixedSize(true);
        RetrieveFavorite();

        ImageView ivBack = root_view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).exitApp();
                //finish();
            }
        });
        return root_view;
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

    public void RetrieveFavorite(){
        HashMap<String, String> hashMap = new HashMap<>();
        NetworkConstants.getWebservice(true, getActivity(), Request.Method.GET, UrlConstants.RetrieveFavourite, hashMap, new VolleyResponseListener<FavouriteModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(FavouriteModel[] object, String message) {
                //swipe_refresh.setRefreshing(false);
                if (object[0] != null) {
                    favouriteModelArrayList.addAll(Arrays.asList(object));
                    mAdapter = new AdapterFavourite(getActivity(), favouriteModelArrayList, new CustomItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            Log.d("TAG", "clicked position:" + position);
                            String id = favouriteModelArrayList.get(position).getId();
                            List<ResultFavorite> result =favouriteModelArrayList.get(position).getResult();
                            ResultFavorite resultFavorite=result.get(0);
                            CategoryItem categoryItem =new CategoryItem();

                            categoryItem.setId(resultFavorite.getId());
                            categoryItem.setAdd_on(resultFavorite.getAddOn());
                            categoryItem.setAdd_on_price(String.valueOf(resultFavorite.getAddOnPrice()));
                            categoryItem.setCategory(resultFavorite.getCategory());
                            categoryItem.setDescription(resultFavorite.getDescription());
                            categoryItem.setFood_type(resultFavorite.getFoodType());
                            categoryItem.setImage_url(resultFavorite.getImageUrl());
                            categoryItem.setName(resultFavorite.getName());
                            categoryItem.setNutrition(resultFavorite.getNutrition());
                            categoryItem.setOld_price(String.valueOf(resultFavorite.getOldPrice()));
                            categoryItem.setPrice(String.valueOf(resultFavorite.getPrice()));
                            categoryItem.setCreatedAt(resultFavorite.getCreatedAt());
                            categoryItem.setUpdatedAt(resultFavorite.getUpdatedAt());
                           // categoryItem.setCount(resultFavorite.getId());

                            Bundle bundle = new Bundle();
                            bundle.putParcelable("itemDetails", categoryItem);
                            bundle.putParcelableArrayList("itemList", null);
                            Fragment fragment = new FragmentItemDetails();
                            FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                            fragment.setArguments(bundle);
                            transaction.replace(R.id.container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    });
                  //  tvNoOfItemInCart.setText(Html.fromHtml(data.size() + " Item in cart"));
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onError(String message) {
              //  swipe_refresh.setRefreshing(false);
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }, FavouriteModel[].class);
    }
}
