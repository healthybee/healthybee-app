package com.app.healthybee.fragment;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.healthybee.MyApplication;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.adapter.AdapterTimeSlot;
import com.app.healthybee.dboperation.DbHelper;
import com.app.healthybee.listeners.PriceUpdaterListener;
import com.app.healthybee.listeners.UpdateCartCartModule;
import com.app.healthybee.models.CartLocal;
import com.app.healthybee.models.CartModule;
import com.app.healthybee.adapter.AdapterCheckOut;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.R;
import com.app.healthybee.models.Result;
import com.app.healthybee.models.TimeSlot;
import com.app.healthybee.utils.MyCustomProgressDialog;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.utils.SharedPrefUtil;
import com.app.healthybee.utils.Tools;
import com.app.healthybee.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;



/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCheckOut extends Fragment  {
    private RecyclerView recyclerViewItemsList;
    private AdapterCheckOut adapter;
    private AdapterTimeSlot adapterTimeSlot;
    private NestedScrollView sv;
    private ArrayList<CartModule> data;
    private ArrayList<TimeSlot> timeSlotArrayList;

    //private SwipeRefreshLayout swipe_refresh;
    private String selectedDate;
    private TextView tv_date;
    private RelativeLayout rlDate;
    private RelativeLayout rlAddress;
    private ImageView ivBack;
    private RecyclerView recyclerViewTimeSlot;
    private RecyclerView.LayoutManager mLayoutManagerTimeSlot;

    private TextView tvNoOfItemInCart;

    private TextView tvBasicPrice;
    private TextView tvGstSgst;
    private TextView tvDeliveryCharge;
    private TextView tvTotal;
    private TextView tvAddMoreItem;
    private TextView tv_checkout;
    private TextView tvAddress;
    private Toolbar toolbar;

    private ArrayList<String> mSpinnerData;
    private Calendar cal;
    private DbHelper dbHelper;
    private ProgressDialog progressDialog;

    public FragmentCheckOut() {
        // Required empty public constructor
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);
        cal = Calendar.getInstance();
        dbHelper = new DbHelper(getActivity());
        toolbar = view.findViewById(R.id.toolbarCheckout);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        data = new ArrayList<>();
        mSpinnerData = new ArrayList<>();
        mSpinnerData.add("Monthly Subscription");
        mSpinnerData.add("Weakly Subscription");
        timeSlotArrayList = new ArrayList<>();
//        swipe_refresh = view.findViewById(R.id.swipe_refresh_layout_home);
//        swipe_refresh.setColorSchemeResources(R.color.colorOrange, R.color.colorGreen, R.color.colorBlue, R.color.colorRed);
        tv_date = view.findViewById(R.id.tv_date);
        rlDate = view.findViewById(R.id.rlDate);
        rlAddress = view.findViewById(R.id.rlAddress);

        tvNoOfItemInCart = view.findViewById(R.id.tvNoOfItemInCart);

        tvBasicPrice = view.findViewById(R.id.tvBasicPrice);
        tvGstSgst = view.findViewById(R.id.tvGstSgst);
        tvDeliveryCharge = view.findViewById(R.id.tvDeliveryCharge);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvAddMoreItem = view.findViewById(R.id.tvAddMoreItem);
        tv_checkout = view.findViewById(R.id.tv_checkout);
        tvAddress = view.findViewById(R.id.tvAddress);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getResources().getString(R.string.title_please_wait));
        progressDialog.setMessage(getResources().getString(R.string.logout_process));
        progressDialog.setCancelable(false);



        if (null != MainActivity.address.getAddressType()) {
            tvAddress.setText(MainActivity.address.getAddressType());
            tv_checkout.setText("Pay");
        } else {
            tvAddress.setHint("Please select address");
            tv_checkout.setText("Select address");

        }


        sv = view.findViewById(R.id.nsv);
        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.scrollTo(0, 0);
            }
        });

        rlAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction().addToBackStack("null");
                FragmentAddress f1 = new FragmentAddress();
                fragmentTransaction.replace(R.id.container, f1);
                fragmentTransaction.commit();
            }
        });

        rlDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDatePicker(getActivity());
            }
        });


        recyclerViewItemsList = view.findViewById(R.id.recyclerView);
        recyclerViewItemsList.setHasFixedSize(true);


        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewItemsList.setLayoutManager(mLinearLayoutManager);
        ivBack = view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).exitApp();
            }
        });

        recyclerViewTimeSlot = view.findViewById(R.id.recyclerViewTimeSlot);
        recyclerViewTimeSlot.setHasFixedSize(true);
        mLayoutManagerTimeSlot = new GridLayoutManager(getActivity(), 2);
        recyclerViewTimeSlot.setLayoutManager(mLayoutManagerTimeSlot);

        timeSlotArrayList.add(new TimeSlot("8:00 to 9:00 AM", true));
        timeSlotArrayList.add(new TimeSlot("11:00 to 12:00 AM", false));
        timeSlotArrayList.add(new TimeSlot("6:00 to 7:00 PM", false));
        timeSlotArrayList.add(new TimeSlot("8:00 to 9:00 PM", false));
        adapterTimeSlot = new AdapterTimeSlot(getActivity(), timeSlotArrayList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("TAG", "clicked position:" + position);
                String timeSlot = timeSlotArrayList.get(position).getTimeSlot();
                //Toast.makeText(getActivity(), timeSlot, Toast.LENGTH_SHORT).show();
                timeSlotArrayList.get(position).setSelected(true);
                for (int i = 0; i < timeSlotArrayList.size(); i++) {
                    if (!(i == position)) {
                        timeSlotArrayList.get(i).setSelected(false);
                    }
                }
                adapterTimeSlot.notifyDataSetChanged();

                // do what ever you want to do with it
            }
        });
        recyclerViewTimeSlot.setAdapter(adapterTimeSlot);

//        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (!data.isEmpty()) {
//                    data.clear();
//                }
//                RetrieveCarts();
//            }
//        });

        //tvNoOfItemInCart.setText(Html.fromHtml(data.size()+" Item in cart"));
//        calculateTotalPrice(data);


        tvAddMoreItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;
                fragment = new FragmentHome();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        tv_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method generateCheckSum() which will generate the paytm checksum for payment
                // generateCheckSum();
                if (null != MainActivity.address.getAddressType()) {
                    if (validateFields()){

                        Map<String, String> params = new HashMap<String, String>();
                        JSONObject JObject=new JSONObject();
                        try {
                            JObject.put("orderName", "first order");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONArray jsonArray=new JSONArray();
                        for (int i=0;i<3;i++){
                            JSONObject jsonObject=new JSONObject();
                            try {
                                jsonObject.put("old_price","100");
                                jsonObject.put("nutrition","681Cal | 21g | 16g | 100g");
                                jsonObject.put("name","Panner mayo Grilled sandwhich");
                                jsonObject.put("category","Sandwhiches");
                                jsonObject.put("price","110");
                                jsonObject.put("add_on","Cheese");
                                jsonObject.put("description","A food with a sharp taste. Often used to refer to tart or sour foods as well");
                                jsonObject.put("image_url","https://image.ibb.co/c15Rcf/SHR00678.jpg");
                                jsonObject.put("add_on_price","10");
                                jsonObject.put("food_type","veg");
                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            JObject.put("products", jsonArray);
                            JObject.put("productId", "123456");
                            JObject.put("quantity", "2");
                            JObject.put("isActive", "true");
                            JObject.put("deliverySlot", "8:00 am to 10:00 am");
                            JObject.put("total", "620");
                            JObject.put("startDate", "2019/02/14");
                            JObject.put("payment", "no");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        System.out.println("4343 " + JObject.toString());
                    }

                } else {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction().addToBackStack("null");
                    FragmentAddress f1 = new FragmentAddress();
                    fragmentTransaction.replace(R.id.container, f1);
                    fragmentTransaction.commit();
                }
            }
        });

        selectedDate = Tools.formatDateForDisplay(cal.getTime(), "yyyy-MMM-dd");
        tv_date.setText(Html.fromHtml(Tools.convertDateyyyymmddToddmmyyyy(selectedDate)));

        RetrieveCarts();
        return view;
    }

    private boolean validateFields() {

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private void selectDatePicker(Context context) {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                selectedDate = Tools.formatDateForDisplay(cal.getTime(), "yyyy-MMM-dd");
                tv_date.setText(Html.fromHtml(Tools.convertDateyyyymmddToddmmyyyy(selectedDate)));

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        dlg.show();
    }

    private void RetrieveCarts() {
        if (NetworkConstants.isConnectingToInternet(getActivity())) {
            MyCustomProgressDialog.showDialog(getActivity(), getString(R.string.please_wait));
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(UrlConstants.RetrieveCart,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray jsonArray) {
                            MyCustomProgressDialog.dismissDialog();
                            for (int i=0;i<jsonArray.length();i++){
                                try {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    CartModule cartModule=new CartModule();
                                    cartModule.setId(jsonObject.optString("_id"));
                                    cartModule.setQuantity(jsonObject.optInt("quantity"));
                                    cartModule.setProductId(jsonObject.optString("productId"));
                                    cartModule.setUser(jsonObject.optString("user"));
                                    cartModule.setCreatedAt(jsonObject.optString("createdAt"));
                                    cartModule.setUpdatedAt(jsonObject.optString("updatedAt"));

                                    JSONArray jsonArray1=jsonObject.optJSONArray("result");
                                    JSONObject jsonObject1=jsonArray1.optJSONObject(0); //it contains only one object
                                    List<Result> resultList = new ArrayList<>();
                                    Result result=new Result();
                                    result.setId(jsonObject1.optString("_id"));
                                    result.setPrice(jsonObject1.optInt("price"));
                                    result.setAddOn(jsonObject1.optString("add_on"));
                                    result.setDescription(jsonObject1.optString("description"));
                                    result.setImageUrl(jsonObject1.optString("image_url"));
                                    result.setAddOnPrice(jsonObject1.optInt("add_on_price"));
                                    result.setFoodType(jsonObject1.optString("food_type"));
                                    result.setOldPrice(jsonObject1.optInt("old_price"));
                                    result.setNutrition(jsonObject1.optString("nutrition"));
                                    result.setName(jsonObject1.optString("name"));
                                    result.setCategory(jsonObject1.optString("category"));
                                    result.setCreatedAt(jsonObject1.optString("createdAt"));
                                    result.setUpdatedAt(jsonObject1.optString("updatedAt"));
                                    resultList.add(result);
                                    cartModule.setResult(resultList);
                                    cartModule.setSubscriptionType("M");
                                    data.add(cartModule);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapter = new AdapterCheckOut(getActivity(), data, mSpinnerData, new PriceUpdaterListener() {
                                @SuppressLint("DefaultLocale")
                                @Override
                                public void onUpdatePrice(double basicPrice) {
                                    Log.d("TAG", "Basic Price:" + basicPrice);
                                    double gst = (basicPrice * 12) / 100;
                                    tvBasicPrice.setText(Html.fromHtml(getResources().getString(R.string.rs) + " " + String.format("%.2f", basicPrice)));
                                    tvTotal.setText(Html.fromHtml(getResources().getString(R.string.rs) + " " + String.format("%.2f", basicPrice + gst)));
                                    tvGstSgst.setText(Html.fromHtml(getResources().getString(R.string.rs) + " " + String.format("%.2f", gst)));
                                    tvDeliveryCharge.setText(Html.fromHtml(getResources().getString(R.string.rs) + " 0.00"));
                                }
                            }, new UpdateCartCartModule() {
                                @Override
                                public void OnAddItemToCart(int position, CartModule cartModule, int count, int card_plus_minus) {
                                    Log.d("TAG", "add to cart" + cartModule.getId());
                                    if (card_plus_minus == 1) {
                                        UpdateCart(position,cartModule.getId(),count+1);
                                    }
                                    if (card_plus_minus == 0) {
                                        UpdateCart(position,cartModule.getId(),count-1);
                                    }
                                    if (card_plus_minus == -1) {
                                        DeleteCart(position,cartModule.getId());
                                    }
                                }
                            });
                            tvNoOfItemInCart.setText(Html.fromHtml(data.size() + " Item in cart"));
                            recyclerViewItemsList.setAdapter(adapter);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            MyCustomProgressDialog.dismissDialog();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(getActivity()));

                    return headers;
                }
            };

            MyApplication.getInstance().addToRequestQueue(jsonArrayRequest);


        } else {
            MyCustomProgressDialog.showAlertDialogMessage(getActivity(), getString(R.string.network_title), getString(R.string.network_message));
        }
    }


    private void UpdateCart(final int position, final String cartId, final int CartCount){
        if (NetworkConstants.isConnectingToInternet(Objects.requireNonNull(getActivity()))) {
            MyCustomProgressDialog.showDialog(getActivity(), getActivity().getString(R.string.please_wait));
            Map<String, String> params = new HashMap<>();
            params.put("quantity", CartCount+"");
            Log.d("4343", new JSONObject(params).toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    UrlConstants.UpdateCart+cartId,
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("4343", response.toString());
                            dbHelper.insertUpdateCart(new CartLocal(response.optString("productId"),response.optInt("quantity"),response.optString("id")));
                            data.get(position).setQuantity(response.optInt("quantity"));
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
                    headers.put("Content-Type","application/json");
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
    private void DeleteCart(final int position, final String cartId){
        if (NetworkConstants.isConnectingToInternet(Objects.requireNonNull(getActivity()))) {
            MyCustomProgressDialog.showDialog(getActivity(), getActivity().getString(R.string.please_wait));
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.DELETE,
                    UrlConstants.DeleteCart+cartId,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyCustomProgressDialog.dismissDialog();
                            Log.d("4343", response.toString());
                            dbHelper.deleteCartRow(response.optString("productId"));
                            data.remove(position);
                            adapter.notifyDataSetChanged();
                            ((MainActivity) Objects.requireNonNull(getActivity())).setCountText();
                            if (data.size()>0){
                                tvNoOfItemInCart.setText(Html.fromHtml(data.size()+" Item in cart"));
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Oops no item in cart tap OK to add item in cart");
                                builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ((MainActivity) getActivity()).exitApp();
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.setCancelable(false);
                                builder.show();

                            }
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
                    headers.put("Content-Type","application/json");
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
