package com.app.healthybee.fragment;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.healthybee.MyApplication;
import com.app.healthybee.adapter.AdapterItemsList;
import com.app.healthybee.R;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.dboperation.DbHelper;
import com.app.healthybee.models.CategoryItem;
import com.app.healthybee.utils.MyCustomProgressDialog;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.utils.SharedPrefUtil;
import com.app.healthybee.utils.UrlConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentItemDetails extends Fragment {

    private RecyclerView itemsList;
    private AdapterItemsList adapter;
    private NestedScrollView sv;
    private ArrayList<CategoryItem> data = new ArrayList<>();
    private CategoryItem itemDetails = new CategoryItem();
    private ImageView my_image;
    private ImageView IvFavorite;
    private TextView tv_itemTitle;
    private TextView tvCalories;
    private TextView tvProtein;
    private TextView tvFat;
    private TextView tvCarbs;
    private ImageView IvBack;
    private TextView tvDescription;

    private TextView tvMonthlyPrice;
    private TextView tvmPrice;

    private TextView tvWeeklyPrice;
    private TextView tvwPrice;

    private CheckBox checkw, checkm;
    private TextView tv_subscribe_monthly;
    private TextView tv_subscribe_weakly;


    private DbHelper dbHelper;

    public FragmentItemDetails() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_details, container, false);
        sv = view.findViewById(R.id.sv);
        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.scrollTo(0, 0);
            }
        });

        dbHelper = new DbHelper(getActivity());
        my_image = view.findViewById(R.id.my_image);
        tvCalories = view.findViewById(R.id.tvCalories);
        tv_itemTitle = view.findViewById(R.id.tv_itemTitle);
        tvProtein = view.findViewById(R.id.tvProtein);
        tvFat = view.findViewById(R.id.tvFat);
        tvCarbs = view.findViewById(R.id.tvCarbs);
        IvBack = view.findViewById(R.id.IvBack);
        IvFavorite = view.findViewById(R.id.IvFavorite);
        itemsList = view.findViewById(R.id.recycler_view);
        itemsList.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        itemsList.setLayoutManager(mLinearLayoutManager);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            itemDetails = bundle.getParcelable("itemDetails");
            //  data.addAll(Objects.requireNonNull(bundle.<CategoryItem>getParcelableArrayList("itemList")));
        }

//        adapter = new AdapterItemsList(getActivity(), data, new CustomItemClickListener() {
//            @Override
//            public void onItemClick(View v, int position) {
//                Log.d("TAG", "clicked position:" + position);
//                String postId = data.get(position).getName();
//                Toast.makeText(getActivity(), postId, Toast.LENGTH_SHORT).show();
//
//                // do what ever you want to do with it
//            }
//        }, new UpdateCartCategoryItem() {
//            @Override
//            public void OnAddItemToCart(CategoryItem categoryItem, int i1, int card_plus_minus) {
//                Log.d("TAG", "add to cart" + categoryItem.getName());
//                if (card_plus_minus==-1){
//                    dbHelper.deleteCartRow(categoryItem.getName());
//                    ((MainActivity) Objects.requireNonNull(getActivity())).setCountText(0);
//                }else {
//                    dbHelper.insertUpdateCart(categoryItem);
//                    ((MainActivity) Objects.requireNonNull(getActivity())).setCountText(0);
//                }
//            }
//        });

        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) Objects.requireNonNull(getActivity())).exitApp();
            }
        });
        IvFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateFavorite();
            }
        });
        //  set item details
        tv_itemTitle.setText(Html.fromHtml(itemDetails.getAdd_on()));
        Glide.with(Objects.requireNonNull(getActivity()))
                .load(itemDetails.getImage_url().replace(" ", "%20"))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.load)
                        .fitCenter()
                        .error(R.drawable.ic_no_item))
                .into(my_image);

        itemsList.setAdapter(adapter);
        String nutrition = itemDetails.getNutrition().replace(" ", "").trim();
        nutrition = nutrition.replace("|", ":").trim();
        String[] separated = nutrition.split(":");
        String Calories = separated[0];
        tvCalories.setText(Html.fromHtml(Calories));
        String Protein = separated[1];
        tvProtein.setText(Html.fromHtml(Protein));
        String Fat = separated[2];
        tvFat.setText(Html.fromHtml(Fat));
        String Carbs = separated[3];
        tvCarbs.setText(Html.fromHtml(Carbs));


        /*Description*/
        tvDescription = view.findViewById(R.id.tvDescription);
        tvDescription.setText(Html.fromHtml(itemDetails.getDescription()));

        /*Price*/
        tvmPrice = view.findViewById(R.id.tvmPrice);
        tvmPrice.setText(Html.fromHtml(getActivity().getResources().getString(R.string.rs) + " " + itemDetails.getPrice() + "/Meal"));

        /*MonthlyPrice*/
        tvMonthlyPrice = view.findViewById(R.id.tvMonthlyPrice);
        double price = Double.valueOf(itemDetails.getPrice());
        double itemTotalPrice = price * 30;
        tvMonthlyPrice.setText(Html.fromHtml("(" + getActivity().getResources().getString(R.string.rs) + " " + String.valueOf(itemTotalPrice) + "/Month)"));

        /*Price weekly*/
        tvwPrice = view.findViewById(R.id.tvwPrice);
        tvwPrice.setText(Html.fromHtml(getActivity().getResources().getString(R.string.rs) + " " + itemDetails.getPrice() + "/Meal"));

        /*weeklyPrice*/
        tvWeeklyPrice = view.findViewById(R.id.tvWeeklyPrice);
        double priceW = Double.valueOf(itemDetails.getPrice());
        double itemTotalPriceW = priceW * 7;
        tvWeeklyPrice.setText(Html.fromHtml("(" + getActivity().getResources().getString(R.string.rs) + " " + String.valueOf(itemTotalPriceW) + "/Week)"));


        tv_subscribe_monthly= view.findViewById(R.id.tv_subscribe_monthly);
        tv_subscribe_weakly= view.findViewById(R.id.tv_subscribe_weakly);
        /*check box monthly*/
        checkm = view.findViewById(R.id.checkm);
        checkm.setChecked(true);
        checkw = view.findViewById(R.id.checkw);
        checkw.setChecked(false);

//        tv_subscribe_monthly.setBackground(getActivity().getDrawable(R.drawable.button_shap_orange));
//        tv_subscribe_monthly.setTextColor(getResources().getColor(R.color.colorWhite));
//        tv_subscribe_weakly.setBackground(getActivity().getDrawable(R.drawable.button_shap_orange_transparent));
//        tv_subscribe_weakly.setTextColor(getResources().getColor(R.color.colorOrange));

        checkm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkw.setChecked(false);
//                    tv_subscribe_monthly.setBackground(getActivity().getDrawable(R.drawable.button_shap_orange));
//                    tv_subscribe_monthly.setTextColor(getResources().getColor(R.color.colorWhite));
//                    tv_subscribe_weakly.setBackground(getActivity().getDrawable(R.drawable.button_shap_orange_transparent));
//                    tv_subscribe_weakly.setTextColor(getResources().getColor(R.color.colorOrange));
                }
//                else {
//                    checkw.setChecked(true);
//                    tv_subscribe_weakly.setBackground(getActivity().getDrawable(R.drawable.button_shap_orange));
//                    tv_subscribe_weakly.setTextColor(getResources().getColor(R.color.colorWhite));
//                    tv_subscribe_monthly.setBackground(getActivity().getDrawable(R.drawable.button_shap_orange_transparent));
//                    tv_subscribe_monthly.setTextColor(getResources().getColor(R.color.colorOrange));
//                }

            }
        });
        checkw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkm.setChecked(false);
//                    tv_subscribe_weakly.setBackground(getActivity().getDrawable(R.drawable.button_shap_orange));
//                    tv_subscribe_weakly.setTextColor(getResources().getColor(R.color.colorWhite));
//                    tv_subscribe_monthly.setBackground(getActivity().getDrawable(R.drawable.button_shap_orange_transparent));
//                    tv_subscribe_monthly.setTextColor(getResources().getColor(R.color.colorOrange));
                }
//                else {
//                    checkm.setChecked(true);
//                    tv_subscribe_monthly.setBackground(getActivity().getDrawable(R.drawable.button_shap_orange));
//                    tv_subscribe_monthly.setTextColor(getResources().getColor(R.color.colorWhite));
//                    tv_subscribe_weakly.setBackground(getActivity().getDrawable(R.drawable.button_shap_orange_transparent));
//                    tv_subscribe_weakly.setTextColor(getResources().getColor(R.color.colorOrange));
//                }

            }
        });


        return view;
    }

    private void CreateFavorite() {
        if (NetworkConstants.isConnectingToInternet(Objects.requireNonNull(getActivity()))) {
            MyCustomProgressDialog.showDialog(getActivity(), getString(R.string.please_wait));
            Map<String, String> params = new HashMap<>();
            params.put("productId", itemDetails.getId());
            Log.d("4343", new JSONObject(params).toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    UrlConstants.CreateFavourite,
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("4343", response.toString());
                            MyCustomProgressDialog.dismissDialog();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Item added in favorite");
                            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.setCancelable(false);
                            builder.show();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("4343", "Site Info Error: " + error.getMessage());
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
            MyCustomProgressDialog.showAlertDialogMessage(getActivity(), getString(R.string.network_title), getString(R.string.network_message));
        }
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
