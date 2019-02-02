package com.app.healthybee.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.healthybee.MyApplication;
import com.app.healthybee.R;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.adapter.AdapterAddress;
import com.app.healthybee.listeners.CustomAddClickListener;
import com.app.healthybee.models.AddressModule;
import com.app.healthybee.utils.MyCustomProgressDialog;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.utils.SharedPrefUtil;
import com.app.healthybee.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FragmentAddress extends Fragment {

    private View root_view;
    private RecyclerView itemsList;
    private AddressModule address;
    private Toolbar toolbar;
    private ImageView ivBack;
    private ImageView ivAddAddress;
    private AdapterAddress adapter;
    private ArrayList<AddressModule> dataList ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.activity_address, null);

        toolbar = root_view.findViewById(R.id.toolbarAddress);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        dataList= new ArrayList<>();
        address = new AddressModule();
        itemsList = (RecyclerView) root_view.findViewById(R.id.recycler_view_address);
        itemsList.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        itemsList.setLayoutManager(mLinearLayoutManager);
        getSavedAddress();
        adapter = new AdapterAddress(getActivity(), dataList, new CustomAddClickListener() {
            @Override
            public void onItemClick(View v, int position, String type) {
                if (type.equalsIgnoreCase("select")) {
                    Log.d("TAG", "clicked position:" + position);
                    MainActivity.address=dataList.get(position);
                    ((MainActivity) getActivity()).exitApp();
                }
                if (type.equalsIgnoreCase("edit")) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("addressObj", dataList.get(position));
                    bundle.putString("operationType", "EDIT");
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction().addToBackStack("null");
                    FragmentEditAddress f1 = new FragmentEditAddress();
                    f1.setArguments(bundle);
                    fragmentTransaction.replace(R.id.container, f1);
                    fragmentTransaction.commit();
                }
            }
        });
        itemsList.setAdapter(adapter);

        ivBack = root_view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).exitApp();
            }
        });
        ivAddAddress = root_view.findViewById(R.id.ivAddAddress);
        ivAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putParcelable("addressObj",  address);
                bundle.putString("operationType", "ADD");
                FragmentManager fm =getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction().addToBackStack("null");
                FragmentEditAddress f1 = new FragmentEditAddress();
                f1.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, f1);
                fragmentTransaction.commit();
            }
        });
        return root_view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    private void getSavedAddress() {
        if (NetworkConstants.isConnectingToInternet(getActivity())) {
            MyCustomProgressDialog.showDialog(getActivity(), getString(R.string.please_wait));
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(UrlConstants.retrieveAddresses,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray jsonArray) {
                            Log.d("TAG", jsonArray.toString());
                            MyCustomProgressDialog.dismissDialog();
                            for (int i=0;i<jsonArray.length();i++){
                                try {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    AddressModule addressModule=new AddressModule();

                                    addressModule.setId(jsonObject.optString("id"));
                                    addressModule.setUser(jsonObject.optString("user"));
                                    addressModule.setAddressType(jsonObject.optString("addressType"));
                                    addressModule.setLine1(jsonObject.optString("line1"));
                                    addressModule.setLine2(jsonObject.optString("line2"));
                                    addressModule.setCity(jsonObject.optString("city"));
                                    addressModule.setState(jsonObject.optString("state"));
                                    addressModule.setZipcode(jsonObject.optString("zipcode"));
                                    addressModule.setLandmark(jsonObject.optString("landmark"));
                                    addressModule.setCreatedAt(jsonObject.optString("createdAt"));
                                    addressModule.setUpdatedAt(jsonObject.optString("updatedAt"));
                                    dataList.add(addressModule);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            adapter.notifyDataSetChanged();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("TAG", volleyError.getMessage());
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
}
