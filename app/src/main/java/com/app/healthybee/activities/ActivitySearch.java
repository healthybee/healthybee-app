package com.app.healthybee.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.healthybee.MyApplication;
import com.app.healthybee.adapter.AdapterCategoryItem;
import com.app.healthybee.dboperation.DbHelper;
import com.app.healthybee.fragment.FragmentItemDetails;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.listeners.UpdateCartCategoryItem;
import com.app.healthybee.models.CartLocal;
import com.app.healthybee.models.CategoryItem;
import com.app.healthybee.utils.Config;
import com.app.healthybee.R;
import com.app.healthybee.utils.GridSpacingItemDecoration;
import com.app.healthybee.utils.MyCustomProgressDialog;
import com.app.healthybee.utils.NetworkCheck;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.utils.SharedPrefUtil;
import com.app.healthybee.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ActivitySearch extends AppCompatActivity {

    private EditText et_search;

    private ImageButton bt_clear;
    private ProgressBar progressBar;
    private Activity activity;

    private RecyclerView recyclerView;
    private AdapterCategoryItem adapter;
    private ArrayList<CategoryItem> categoryItemList;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        activity = ActivitySearch.this;
        if (Config.ENABLE_RTL_MODE) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        recyclerView = findViewById(R.id.recyclerView);

        dbHelper = new DbHelper(activity);
        categoryItemList = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new GridLayoutManager(activity, 2);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(activity, 2, dpToPx(7), true));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        initComponent();
        setupToolbar();

    }

    private void initComponent() {
        et_search = findViewById(R.id.et_search);
        bt_clear = findViewById(R.id.bt_clear);
        bt_clear.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progressBar);

        et_search.addTextChangedListener(textWatcher);


        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_search.setText("");
            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    searchAction();
                    return true;
                }
                return false;
            }
        });

    }

    public void setupToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence c, int i, int i1, int i2) {
            if (c.toString().trim().length() == 0) {
                bt_clear.setVisibility(View.GONE);
            } else {
                bt_clear.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private void requestSearchApi(final String query) {
        if (NetworkConstants.isConnectingToInternet(Objects.requireNonNull(activity))) {
            Log.e("4343", UrlConstants.SearchItem + query);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    (UrlConstants.SearchItem + query).replace(" ", "%20"),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            progressBar.setVisibility(View.GONE);
                            Log.d("4343", response.toString());
                            int length = response.length();
                            if (length == 0) {
//                                showNoItemView(true);
//                                swipe_refresh.setRefreshing(false);
                            } else {
                                showFailedView(false,"");
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
//
                                        categoryItemList.add(categoryItem);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                adapter = new AdapterCategoryItem(activity, categoryItemList, new CustomItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {
                                        Log.d("TAG", "clicked position:" + position);

                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable("itemDetails", categoryItemList.get(position));
                                        bundle.putParcelableArrayList("itemList", categoryItemList);
                                        Fragment fragment = new FragmentItemDetails();
                                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
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
                                            if (NetworkConstants.isConnectingToInternet(Objects.requireNonNull(activity))) {
                                                MyCustomProgressDialog.showDialog(activity, activity.getString(R.string.please_wait));
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
                                                              //  ((MainActivity) Objects.requireNonNull(activity)).setCountText();
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
                                                        headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(activity));
                                                        return headers;
                                                    }
                                                };
                                                Log.d("4343", jsonObjectRequest.toString());
                                                MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

                                            } else {
                                                MyCustomProgressDialog.showAlertDialogMessage(activity, activity.getString(R.string.network_title), activity.getString(R.string.network_message));
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
//                                swipe_refresh.setRefreshing(false);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            onFailRequest();

                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(activity));
                    return headers;
                }
            };

            // Add JsonArrayRequest to the RequestQueue
            MyApplication.getInstance().addToRequestQueue(jsonArrayRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(activity, getString(R.string.network_title), getString(R.string.network_message));
        }

    }

    private void onFailRequest() {
        if (NetworkCheck.isConnect(this)) {
            showFailedView(true, getString(R.string.msg_no_network));
        } else {
            showFailedView(true, getString(R.string.msg_offline));
        }
    }

    private void searchAction() {
        //lyt_suggestion.setVisibility(View.GONE);
        showFailedView(false, "");
        showNotFoundView(false);
        final String query = et_search.getText().toString().trim();
        if (!query.equals("")) {
           // mAdapterSuggestion.addSearchHistory(query);
            // mAdapter.resetListData();
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestSearchApi(query);
                }
            }, 3000);
        } else {
            Toast.makeText(this, R.string.msg_search_input, Toast.LENGTH_SHORT).show();
        }
    }

//    private void showSuggestionSearch() {
//        mAdapterSuggestion.refreshItems();
//        lyt_suggestion.setVisibility(View.VISIBLE);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else {
          //  Snackbar.make(parent_view, item.getTitle() + " clicked", Snackbar.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showFailedView(boolean show, String message) {
        View lyt_failed = findViewById(R.id.lyt_failed);
        ((TextView) findViewById(R.id.failed_message)).setText(message);
        if (show) {
            recyclerView.setVisibility(View.GONE);
            lyt_failed.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lyt_failed.setVisibility(View.GONE);
        }
        findViewById(R.id.failed_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //searchAction();
            }
        });
    }

    private void showNotFoundView(boolean show) {
        View lyt_no_item = findViewById(R.id.lyt_no_item);
        ((TextView) findViewById(R.id.no_item_message)).setText(R.string.msg_no_news_found);
        if (show) {
            recyclerView.setVisibility(View.GONE);
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lyt_no_item.setVisibility(View.GONE);
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onBackPressed() {
        if (et_search.length() > 0) {
            et_search.setText("");
        } else {
            super.onBackPressed();
        }

    }

    private void UpdateCart(final int position, final String cartId, final int CartCount) {
        if (NetworkConstants.isConnectingToInternet(Objects.requireNonNull(activity))) {
            MyCustomProgressDialog.showDialog(activity, activity.getString(R.string.please_wait));
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
                            //((MainActivity) Objects.requireNonNull(activity)).setCountText();
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
                    headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(activity));
                    return headers;
                }
            };
            Log.d("4343", jsonObjectRequest.toString());
            MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(activity, activity.getString(R.string.network_title), activity.getString(R.string.network_message));
        }
    }

    private void DeleteCart(final int position, final String cartId) {
        if (NetworkConstants.isConnectingToInternet(Objects.requireNonNull(activity))) {
            MyCustomProgressDialog.showDialog(activity, activity.getString(R.string.please_wait));
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
                           // ((MainActivity) Objects.requireNonNull(activity)).setCountText();
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
                    headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(activity));

                    return headers;
                }
            };
            Log.d("4343", jsonObjectRequest.toString());
            MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(activity, activity.getString(R.string.network_title), activity.getString(R.string.network_message));
        }
    }
}
