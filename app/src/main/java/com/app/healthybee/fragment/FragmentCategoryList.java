package com.app.healthybee.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.healthybee.MyApplication;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.dboperation.DbHelper;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.models.CartLocal;
import com.app.healthybee.utils.GridSpacingItemDecoration;
import com.app.healthybee.utils.ListPaddingDecoration;
import com.app.healthybee.utils.MyCustomProgressDialog;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.R;
import com.app.healthybee.listeners.UpdateCartCategoryItem;
import com.app.healthybee.utils.SharedPrefUtil;
import com.app.healthybee.utils.UrlConstants;
import com.app.healthybee.adapter.AdapterCategoryItem;
import com.app.healthybee.models.CategoryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FragmentCategoryList extends Fragment {
    private View rootView;
    private RecyclerView recyclerView;
    private AdapterCategoryItem adapter;
    private ArrayList<CategoryItem> categoryItemList;
    private String category = "";
    private DbHelper dbHelper;
    private SwipeRefreshLayout swipe_refresh;
    private ImageView imageViewGrid, imageViewList;


    public FragmentCategoryList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.system_app_list, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        swipe_refresh = rootView.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh.setColorSchemeResources(R.color.colorOrange, R.color.colorGreyDark, R.color.colorBlue, R.color.colorRed);
        imageViewGrid = rootView.findViewById(R.id.imageViewGrid);
        imageViewList = rootView.findViewById(R.id.imageViewList);

        if (MainActivity.mFlagDisplayList) {
            imageViewGrid.setImageResource(R.drawable.ic_gridview_disable);
            imageViewList.setImageResource(R.drawable.ic_listview_enable);
        } else {
            imageViewList.setImageResource(R.drawable.ic_listview_disable);
            imageViewGrid.setImageResource(R.drawable.ic_gridview_enable);
        }
        imageViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mFlagDisplayList = true;
                imageViewGrid.setImageResource(R.drawable.ic_gridview_disable);
                imageViewList.setImageResource(R.drawable.ic_listview_enable);
                refreshFragment();
            }
        });
        imageViewGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mFlagDisplayList = false;
                imageViewList.setImageResource(R.drawable.ic_listview_disable);
                imageViewGrid.setImageResource(R.drawable.ic_gridview_enable);
                refreshFragment();

            }
        });

        dbHelper = new DbHelper(getActivity());
        categoryItemList = new ArrayList<>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            category = bundle.getString("category", null);
        }

        RecyclerView.LayoutManager mLayoutManager;
        if (MainActivity.mFlagDisplayList) {
            mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.addItemDecoration(new ListPaddingDecoration(getActivity()));
        } else {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(getActivity(), 2, dpToPx(7), true));
        }
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (NetworkConstants.isConnectingToInternet(getActivity())) {
            try {
                getCategoryList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // on swipe list
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!categoryItemList.isEmpty()) {
                    categoryItemList.clear();
                    adapter.notifyDataSetChanged();
                }

                if (NetworkConstants.isConnectingToInternet(getActivity())) {
                    try {
                        getCategoryList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return rootView;
    }

    public void refreshFragment() {
        FragmentHome.refreshFragment();
        //recyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getCategoryList() {
        if (NetworkConstants.isConnectingToInternet(Objects.requireNonNull(getActivity()))) {
            Log.e("4343", UrlConstants.getCategoryItemList + category);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    (UrlConstants.getCategoryItemList + category + "&page=1&limit=10").replace(" ", "%20"),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("4343", response.toString());
                            int length = response.length();
                            if (length == 0) {
                                showNoItemView(true);
                                swipe_refresh.setRefreshing(false);
                            } else {
                                showNoItemView(false);
                                for (int i = 0; i < length; i++) {
                                    try {
                                        JSONObject jsonObject = (JSONObject) response.get(i);
                                        CategoryItem categoryItem = new CategoryItem();
                                        categoryItem.setId(jsonObject.optString("id"));
                                        categoryItem.setAdd_on_price(jsonObject.optString("add_on_price"));
                                        categoryItem.setPrice(jsonObject.optString("price"));
                                        categoryItem.setAdd_on(jsonObject.optString("add_on"));
                                        categoryItem.setDescription(jsonObject.optString("description"));
                                        categoryItem.setImage_url(jsonObject.optString("image_url"));
                                        categoryItem.setFood_type(jsonObject.optString("food_type"));
                                        categoryItem.setOld_price(jsonObject.optString("old_price"));
                                        categoryItem.setNutrition(jsonObject.optString("nutrition"));
                                        categoryItem.setName(jsonObject.optString("name"));
                                        categoryItem.setCategory(jsonObject.optString("category"));
                                        categoryItem.setCreatedAt(jsonObject.optString("createdAt"));
                                        categoryItem.setUpdatedAt(jsonObject.optString("updatedAt"));
                                        //  13/11/18

                                        categoryItem.setCount(dbHelper.getCartCount(jsonObject.optString("id")));

                                        categoryItemList.add(categoryItem);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                adapter = new AdapterCategoryItem(getActivity(), categoryItemList, new CustomItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {
                                        Log.d("TAG", "clicked position:" + position);

                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable("itemDetails", categoryItemList.get(position));
                                        bundle.putParcelableArrayList("itemList", categoryItemList);
                                        Fragment fragment = new FragmentItemDetails();
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        fragment.setArguments(bundle);
                                        transaction.replace(R.id.container, fragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }
                                }, new UpdateCartCategoryItem() {
                                    @Override
                                    public void OnAddItemToCart(final CategoryItem categoryItem, final int count, int card_plus_minus, final int position) {
                                        Log.d("TAG", "add to cart" + categoryItem.getName());
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
                                                                categoryItemList.get(position).setCount(response.optInt("quantity"));
                                                                adapter.notifyDataSetChanged();
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
                                recyclerView.setAdapter(adapter);
                                swipe_refresh.setRefreshing(false);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(getActivity()));
                    return headers;
                }
            };

            // Add JsonArrayRequest to the RequestQueue
            MyApplication.getInstance().addToRequestQueue(jsonArrayRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(getActivity(), getString(R.string.network_title), getString(R.string.network_message));
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setHasOptionsMenu(true);

    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void showNoItemView(boolean show) {
        View lyt_no_item = rootView.findViewById(R.id.lyt_no_item_category);
        if (show) {
            recyclerView.setVisibility(View.GONE);
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lyt_no_item.setVisibility(View.GONE);
        }
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
                            categoryItemList.get(position).setCount(response.optInt("quantity"));
                            adapter.notifyDataSetChanged();
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
                    Map<String, String> headers = new HashMap<String, String>();
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
                            categoryItemList.get(position).setCount(0);
                            adapter.notifyDataSetChanged();
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
