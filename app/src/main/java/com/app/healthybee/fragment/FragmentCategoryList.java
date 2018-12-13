package com.app.healthybee.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
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


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.utils.GridSpacingItemDecoration;
import com.app.healthybee.utils.ListPaddingDecoration;
import com.app.healthybee.utils.MyCustomProgressDialog;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.R;
import com.app.healthybee.listeners.UpdateCart;
import com.app.healthybee.utils.UrlConstants;
import com.app.healthybee.activities.Applications;
import com.app.healthybee.adapter.AdapterCategoryItem;
import com.app.healthybee.dboperation.DbHelper;
import com.app.healthybee.models.CategoryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class FragmentCategoryList extends Fragment {
    private View rootView;
    private RecyclerView recyclerView;
    private AdapterCategoryItem adapter;
    private ArrayList<CategoryItem> categoryItemList;
    private RecyclerView.LayoutManager mLayoutManager;
    private String category="";
    private DbHelper dbHelper;
    private SwipeRefreshLayout swipe_refresh;




    public FragmentCategoryList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.system_app_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        swipe_refresh = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh.setColorSchemeResources(R.color.colorOrange, R.color.colorGreyDark, R.color.colorBlue, R.color.colorRed);
        dbHelper=new DbHelper(getActivity());
        categoryItemList = new ArrayList<>();


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            category = bundle.getString("category", null);
        }

        if (MainActivity.mFlagDisplayList) {
            mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.addItemDecoration(new ListPaddingDecoration(getActivity()));
        } else {

            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(getActivity(),2, dpToPx(3), true));
        }
        recyclerView.setLayoutManager(mLayoutManager);

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
                if (!categoryItemList.isEmpty()){
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

    private void getCategoryList() {
        if (NetworkConstants.isConnectingToInternet(getActivity())) {
            Log.e("4343",UrlConstants.getCategoryItemList+category);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    UrlConstants.getCategoryItemList+category,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            int length=response.length();
                            if (length==0){
                                showNoItemView(true);
                                swipe_refresh.setRefreshing(false);
                            }else {
                                showNoItemView(false);
                                dbHelper.openDB();
                                for(int i=0;i<length;i++){
                                    try {
                                        JSONObject jsonObject= (JSONObject) response.get(i);
                                        CategoryItem categoryItem=new CategoryItem();
                                        categoryItem.setItemId(jsonObject.optInt("id"));
                                        categoryItem.setPrice(jsonObject.optString("price"));
                                        categoryItem.setAdd_on(jsonObject.optString("add_on"));
                                        categoryItem.setDescription(jsonObject.optString("description"));
                                        categoryItem.setImage_url(jsonObject.optString("image_url"));
                                        categoryItem.setFood_type(jsonObject.optString("food_type"));
                                        categoryItem.setOld_price(jsonObject.optString("old_price"));
                                        categoryItem.setNutrition(jsonObject.optString("nutrition"));
                                        categoryItem.setName(jsonObject.optString("name"));
                                        categoryItem.setCategory(jsonObject.optString("category"));
                                        // TODO: 13/11/18
                                        categoryItem.setCount(dbHelper.getCartCount(jsonObject.optString("name")));

                                        categoryItemList.add(categoryItem);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        dbHelper.closeDB();
                                    }

                                }
                                dbHelper.closeDB();
                                adapter = new AdapterCategoryItem(getActivity(), categoryItemList, new CustomItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {
                                        Log.d("TAG", "clicked position:" + position);

//                                        Intent intent =new Intent(getActivity(),ActivityItemDetails.class);
//                                        intent.putExtra("itemDetails",categoryItemList.get(position));
//                                        intent.putParcelableArrayListExtra("itemList", (ArrayList<? extends Parcelable>) categoryItemList);
//                                        startActivity(intent);


                                    Bundle bundle=new Bundle();
                                    bundle.putParcelable("itemDetails",categoryItemList.get(position));
                                    bundle.putParcelableArrayList("itemList", (ArrayList<? extends Parcelable>) categoryItemList);
                                    Fragment fragment = new FragmentItemDetails();
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragment.setArguments(bundle);
                                    transaction.replace(R.id.container, fragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();




                                    }
                                }, new UpdateCart() {
                                    @Override
                                    public void OnAddItemToCart(CategoryItem categoryItem, int count, int card_plus_minus) {
                                        Log.d("TAG", "add to cart" + categoryItem.getName());
                                        dbHelper.insertUpdateCart(categoryItem);
                                        ((MainActivity) getActivity()).setCountText();
                                    }
                                });
                                recyclerView.setAdapter(adapter);
                                swipe_refresh.setRefreshing(false);
                            }
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){

                        }
                    }
            );

            // Add JsonArrayRequest to the RequestQueue
            Applications.getInstance().addToRequestQueue(jsonArrayRequest);

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
       // ((TextView) rootView.findViewById(R.id.no_item_message)).setText(message);
        if (show) {
            recyclerView.setVisibility(View.GONE);
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lyt_no_item.setVisibility(View.GONE);
        }
    }
}