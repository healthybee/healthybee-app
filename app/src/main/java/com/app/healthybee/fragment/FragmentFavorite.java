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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.healthybee.MyApplication;
import com.app.healthybee.R;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.adapter.AdapterFavourite;
import com.app.healthybee.dboperation.DbHelper;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.listeners.UpdateCartCategoryItem;
import com.app.healthybee.listeners.VolleyResponseListener;
import com.app.healthybee.models.CartLocal;
import com.app.healthybee.models.CategoryItem;
import com.app.healthybee.models.FavouriteModel;
import com.app.healthybee.models.ResultFavorite;
import com.app.healthybee.utils.ListPaddingDecoration;
import com.app.healthybee.utils.MyCustomProgressDialog;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.utils.SharedPrefUtil;
import com.app.healthybee.utils.UrlConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FragmentFavorite extends Fragment {

    private ArrayList<FavouriteModel> favouriteModelArrayList;
    private ArrayList<CategoryItem> categoryItemArrayList;
    private View root_view;
    private RecyclerView recyclerView;
    private AdapterFavourite mAdapter;
    private DbHelper dbHelper;

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
        dbHelper = new DbHelper(getActivity());
        favouriteModelArrayList = new ArrayList<>();
        categoryItemArrayList = new ArrayList<>();
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

    public void RetrieveFavorite() {
        HashMap<String, String> hashMap = new HashMap<>();
        NetworkConstants.getWebservice(true, getActivity(), Request.Method.GET, UrlConstants.RetrieveFavourite, hashMap, new VolleyResponseListener<FavouriteModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(FavouriteModel[] object, String message) {
                //swipe_refresh.setRefreshing(false);
                if (object[0] != null) {
                    favouriteModelArrayList.addAll(Arrays.asList(object));
                    int size = favouriteModelArrayList.size();
                    for (int i = 0; i < size; i++) {
                        List<ResultFavorite> result = favouriteModelArrayList.get(i).getResult();
                        ResultFavorite resultFavorite = result.get(0);
                        CategoryItem categoryItem = new CategoryItem();

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
                        categoryItem.setCount(dbHelper.getCartCount(resultFavorite.getId()));
                        categoryItemArrayList.add(categoryItem);
                    }
                    mAdapter = new AdapterFavourite(getActivity(), categoryItemArrayList, new CustomItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            Log.d("TAG", "clicked position:" + position);
                            String id = favouriteModelArrayList.get(position).getId();
                            List<ResultFavorite> result = favouriteModelArrayList.get(position).getResult();
                            ResultFavorite resultFavorite = result.get(0);
                            CategoryItem categoryItem = new CategoryItem();

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
                    }, new UpdateCartCategoryItem() {
                        @Override
                        public void OnAddItemToCart(CategoryItem categoryItem, int count, int card_plus_minus, final int position) {
                            if (count == 0) {
                                if (NetworkConstants.isConnectingToInternet(Objects.requireNonNull(getActivity()))) {
                                    MyCustomProgressDialog.showDialog(getActivity(), getActivity().getString(R.string.please_wait));
                                    Map<String, String> params = new HashMap<>();
                                    params.put("productId", categoryItem.getId());
                                    params.put("quantity", (count + 1) + "");
                                    Log.d("4343", new JSONObject(params).toString());
                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                            Request.Method.POST,
                                            UrlConstants.CreateCart,
                                            new JSONObject(params),
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    dbHelper.insertUpdateCart(new CartLocal(response.optString("productId"), response.optInt("quantity"), response.optString("id")));
                                                    categoryItemArrayList.get(position).setCount(response.optInt("quantity"));
                                                    mAdapter.notifyDataSetChanged();
                                                    ((MainActivity) Objects.requireNonNull(getActivity())).setCountText();
                                                    MyCustomProgressDialog.dismissDialog();
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("4343", "Site Info Error: " + error.getMessage());
                                            MyCustomProgressDialog.dismissDialog();
                                        }
                                    }) {

                                        @Override
                                        public Map<String, String> getHeaders() {
                                            Map<String, String> headers = new HashMap<>();
                                            headers.put("Content-Type", "application/json");
                                            headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(getActivity()));
                                            return headers;
                                        }
                                    };
                                    Log.d("4343", jsonObjectRequest.toString());
                                    MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

                                } else {
                                    MyCustomProgressDialog.showAlertDialogMessage(getActivity(), getActivity().getString(R.string.network_title), getActivity().getString(R.string.network_message));
                                }
                            } else {
                                // update cart
                                if (card_plus_minus == 1) {
                                    UpdateCart(position, dbHelper.getCartId(categoryItem.getId()), (count + 1));
                                }
                                if (card_plus_minus == 0) {
                                    UpdateCart(position, dbHelper.getCartId(categoryItem.getId()), (count - 1));
                                }
                                if (card_plus_minus == -1) {
                                    DeleteCart(position, dbHelper.getCartId(categoryItem.getId()));
                                }
                            }
                        }
                    });
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

    private void UpdateCart(final int position, final String cartId, final int CartCount) {
        if (NetworkConstants.isConnectingToInternet(Objects.requireNonNull(getActivity()))) {
            MyCustomProgressDialog.showDialog(getActivity(), getActivity().getString(R.string.please_wait));
            Map<String, String> params = new HashMap<>();
            params.put("quantity", CartCount + "");
            Log.d("4343", new JSONObject(params).toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    UrlConstants.UpdateCart + cartId,
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("4343", response.toString());
                            dbHelper.insertUpdateCart(new CartLocal(response.optString("productId"), response.optInt("quantity"), response.optString("id")));
                            categoryItemArrayList.get(position).setCount(response.optInt("quantity"));
                            mAdapter.notifyDataSetChanged();
                            ((MainActivity) Objects.requireNonNull(getActivity())).setCountText();
                            MyCustomProgressDialog.dismissDialog();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("4343", "Site Info Error: " + error.getMessage());
                    MyCustomProgressDialog.dismissDialog();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(getActivity()));
                    return headers;
                }
            };
            Log.d("4343", jsonObjectRequest.toString());
            MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(getActivity(), getActivity().getString(R.string.network_title), getActivity().getString(R.string.network_message));
        }
    }

    private void DeleteCart(final int position, final String cartId) {
        if (NetworkConstants.isConnectingToInternet(Objects.requireNonNull(getActivity()))) {
            MyCustomProgressDialog.showDialog(getActivity(), getActivity().getString(R.string.please_wait));
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.DELETE,
                    UrlConstants.DeleteCart + cartId,
                    null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("4343", response.toString());
                            dbHelper.deleteCartRow(response.optString("productId"));
                            categoryItemArrayList.get(position).setCount(0);
                            mAdapter.notifyDataSetChanged();
                            ((MainActivity) Objects.requireNonNull(getActivity())).setCountText();
                            MyCustomProgressDialog.dismissDialog();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("4343", "Site Info Error: " + error.getMessage());
                    MyCustomProgressDialog.dismissDialog();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(getActivity()));

                    return headers;
                }
            };
            Log.d("4343", jsonObjectRequest.toString());
            MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(getActivity(), getActivity().getString(R.string.network_title), getActivity().getString(R.string.network_message));
        }
    }
}
