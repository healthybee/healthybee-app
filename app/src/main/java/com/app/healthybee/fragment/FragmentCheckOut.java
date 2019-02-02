package com.app.healthybee.fragment;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.app.healthybee.Api;
import com.app.healthybee.Checksum;
import com.app.healthybee.Common;
import com.app.healthybee.Constants;
import com.app.healthybee.Paytm;
import com.app.healthybee.activities.ActivityCheckOut;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.adapter.AdapterTimeSlot;
import com.app.healthybee.listeners.UpdateCart1;
import com.app.healthybee.listeners.VolleyResponseListener;
import com.app.healthybee.models.CartModule;
import com.app.healthybee.models.CategoryItem;
import com.app.healthybee.adapter.AdapterCheckOut;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.R;
import com.app.healthybee.models.TimeSlot;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.utils.Tools;
import com.app.healthybee.utils.UrlConstants;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCheckOut extends Fragment implements PaytmPaymentTransactionCallback {
    private RecyclerView recyclerViewItemsList;
    private AdapterCheckOut adapter;
    private AdapterTimeSlot adapterTimeSlot;
    private NestedScrollView sv;
    private ArrayList<CartModule> data;
    private ArrayList<TimeSlot> timeSlotArrayList;

    private SwipeRefreshLayout swipe_refresh;
    private String selectedDate;
    private TextView tv_date;
    private RelativeLayout rlDate, rlAddress;
    private ImageView ivBack;
    private RecyclerView recyclerViewTimeSlot;
    private RecyclerView.LayoutManager mLayoutManagerTimeSlot;

    private TextView tvNoOfItemInCart;

    private TextView tvBasicPrice;
    private TextView tvGstSgst;
    private TextView tvDeliveryCharge;
    private TextView tvTotal;
    private TextView tvAddMoreItem, tv_checkout;
    private TextView tvAddress;
    private Toolbar toolbar;

    ArrayList<String> mSpinnerData;
    Calendar cal;


    public FragmentCheckOut() {
        // Required empty public constructor
    }

    public static FragmentCheckOut newInstance() {
        return new FragmentCheckOut();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);
        cal = Calendar.getInstance();

        toolbar = view.findViewById(R.id.toolbarCheckout);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        data = new ArrayList<>();
        mSpinnerData = new ArrayList<>();
        mSpinnerData.add("Monthly Subscription");
        mSpinnerData.add("Weakly Subscription");
        timeSlotArrayList = new ArrayList<>();
        swipe_refresh = view.findViewById(R.id.swipe_refresh_layout_home);
        swipe_refresh.setColorSchemeResources(R.color.colorOrange, R.color.colorGreen, R.color.colorBlue, R.color.colorRed);
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

        timeSlotArrayList.add(new TimeSlot("8:00 to 9:00 AM", false));
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

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!data.isEmpty()) {
                    data.clear();
                }
                RetrieveCarts();
//                tvNoOfItemInCart.setText(Html.fromHtml(data.size()+" Item in cart"));
//                recyclerViewItemsList.setAdapter(adapter);
//                calculateTotalPrice(data);
//                swipe_refresh.setRefreshing(false);
            }
        });

//        tvNoOfItemInCart.setText(Html.fromHtml(data.size()+" Item in cart"));
//        calculateTotalPrice(data);


        tvAddMoreItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;
                fragment = new FragmentHome();
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
                    Intent intent = new Intent(getActivity(), ActivityCheckOut.class);
                    startActivity(intent);
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

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void calculateTotalPrice(ArrayList<CategoryItem> dataList) {
        double basicPrice = 0;

        for (int i = 0; i < dataList.size(); i++) {
            CategoryItem categoryItem = dataList.get(i);
            double price = Double.parseDouble(categoryItem.getPrice());
            double cartCount = categoryItem.getCount();
            basicPrice = basicPrice + (price * cartCount);
        }
        double gst = (basicPrice * 12) / 100;

        tvBasicPrice.setText(Html.fromHtml(getResources().getString(R.string.rs) + " " + String.format("%.2f", basicPrice)));
        tvTotal.setText(Html.fromHtml(getResources().getString(R.string.rs) + " " + String.format("%.2f", basicPrice + gst)));
        tvGstSgst.setText(Html.fromHtml(getResources().getString(R.string.rs) + " " + String.format("%.2f", gst)));
        tvDeliveryCharge.setText(Html.fromHtml(getResources().getString(R.string.rs) + " 0.00"));

    }


    @Override
    public void onTransactionSuccess(Bundle bundle) {
        Toast.makeText(getActivity(), bundle.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionFailure(String s, Bundle bundle) {
        Toast.makeText(getActivity(), s + bundle.toString(), Toast.LENGTH_LONG).show();
    }

    /*  @Override
      public void onTransactionResponse(Bundle inResponse) {
          Toast.makeText(getActivity(), inResponse.toString(), Toast.LENGTH_LONG).show();
      }
  */
    @Override
    public void networkNotAvailable() {
        Toast.makeText(getActivity(), "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_LONG).show();
    }

   /* @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
        Toast.makeText(getActivity(), "Transaction Cancel", Toast.LENGTH_LONG).show();
    }*/

    private void generateCheckSum() {

        //getting the tax amount first.
        String txnAmount = tvTotal.getText().toString().trim();

        //creating a retrofit object.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating the retrofit api service
        Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        final Paytm paytm = new Paytm(
                Constants.M_ID,
                Constants.CHANNEL_ID,
                txnAmount,
                Constants.WEBSITE,
                Constants.CALLBACK_URL,
                Constants.INDUSTRY_TYPE_ID
        );

        //creating a call object from the apiService
        Call<Checksum> call = apiService.getChecksum(
                paytm.getmId(),
                paytm.getOrderId(),
                paytm.getCustId(),
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl(),
                paytm.getIndustryTypeId()
        );

        //making the call to generate checksum
//        call.enqueue(new Callback<Checksum>() {
//            @Override
//            public void onResponse(Call<Checksum> call, Response<Checksum> response) {
//
//                //once we get the checksum we will initiailize the payment.
//                //the method is taking the checksum we got and the paytm object as the parameter
//                initializePaytmPayment(response.body().getChecksumHash(), paytm);
//            }
//
//            @Override
//            public void onFailure(Call<Checksum> call, Throwable t) {
//
//            }
//        });
    }

    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getStagingService();

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        //creating a paytm order object using the hashmap
        PaytmOrder Order = new PaytmOrder((HashMap<String, String>) paramMap);
        // amod code
        PaytmMerchant Merchant = new PaytmMerchant(
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");
        //intializing the paytm service

        Service.initialize(Order, Merchant, null);
        // Service.initialize(Order, null);

        //finally starting the payment transaction
        //  Service.startPaymentTransaction(getActivity(), true, true, this);

    }

    private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

    }

    private void RetrieveCarts() {
//        CartModule cart=new CartModule();
//        String requestBody= new GsonBuilder().create().toJson(cart);
//        GsonRequest<CartModule> cartGsonRequest=GsonRequest.getGsonRequest(getActivity(), GsonRequest.REQ_TYPE.RETRIEVE_CARTS, requestBody, CartModule.class,
//                new Response.Listener<CartModule>() {
//                    @Override
//                    public void onResponse(CartModule response) {
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//        MyApplication.getWebServiceProvider().addToRequestQueue(cartGsonRequest);
        HashMap<String, String> hashMap = new HashMap<>();

        NetworkConstants.getWebservice(true, getActivity(), Request.Method.GET, UrlConstants.RetrieveCart, hashMap, new VolleyResponseListener<CartModule>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(CartModule[] object, String message) {
                swipe_refresh.setRefreshing(false);
                if (object[0] != null) {
                    data.addAll(Arrays.asList(object));
                    adapter = new AdapterCheckOut(getActivity(), data, mSpinnerData, new CustomItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            Log.d("TAG", "clicked position:" + position);
                            String id = data.get(position).getId();
                        }
                    }, new UpdateCart1() {
                        @Override
                        public void OnAddItemToCart(int position, CartModule categoryItem, int i1, int card_plus_minus) {
                            Log.d("TAG", "add to cart" + categoryItem.getId());
                            if (card_plus_minus == -1) {
                                Common.DeleteCart(getActivity(),categoryItem.getId());

                                // dbHelper.deleteCartRow(categoryItem.getName());
                                //((MainActivity) getActivity()).setCountText(0);
                                if (!data.isEmpty()) {
                                    data.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                                // data.addAll(dbHelper.getCartList());
                                recyclerViewItemsList.setAdapter(adapter);
                                // calculateTotalPrice(data);
                                tvNoOfItemInCart.setText(Html.fromHtml(data.size() + " Item in cart"));
                            } else {
                                Common.UpdateCart(getActivity(),categoryItem.getId(),categoryItem.getQuantity());

                                // dbHelper.insertUpdateCart(categoryItem);
//                                ((MainActivity) getActivity()).setCountText(0);

                                if (!data.isEmpty()) {
                                    adapter.notifyDataSetChanged();
                                }
                                // data.addAll(dbHelper.getCartList());
                                //recyclerViewItemsList.setAdapter(adapter);
                                 //calculateTotalPrice(data);
                                tvNoOfItemInCart.setText(Html.fromHtml(data.size() + " Item in cart"));
                            }

                        }
                    });
                    tvNoOfItemInCart.setText(Html.fromHtml(data.size() + " Item in cart"));
                    recyclerViewItemsList.setAdapter(adapter);
                }
            }

            @Override
            public void onError(String message) {
                swipe_refresh.setRefreshing(false);
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }, CartModule[].class);
    }
}
