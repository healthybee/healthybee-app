package com.app.healthybee.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.healthybee.R;
import com.app.healthybee.MyApplication;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.adapter.AdapterDeliverySupport;
import com.app.healthybee.models.ModelDeliverySupport;
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

public class FragmentDeliverySupport extends Fragment {
    private RecyclerView recView;
    private AdapterDeliverySupport adapterDeliverySupport;
    private ArrayList<ModelDeliverySupport> list;
    private LinearLayoutManager layoutManager;
    private ImageView ivBack;
    private Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery_support, container, false);
        toolbar = view.findViewById(R.id.toolbarDeliverySupport);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        recView = view.findViewById(R.id.recyclerView);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(layoutManager);
        getDeliverySupport();
        ivBack = view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).exitApp();
            }
        });
        return view;
    }

    private void getDeliverySupport() {
        if (NetworkConstants.isConnectingToInternet(getActivity())) {
            MyCustomProgressDialog.showDialog(getActivity(), getString(R.string.please_wait));
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(UrlConstants.deliverySupport,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray jsonArray) {
                            MyCustomProgressDialog.dismissDialog();
                            for (int i=0;i<jsonArray.length();i++){
                                try {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    ModelDeliverySupport modelDeliverySupport=new ModelDeliverySupport();
                                    modelDeliverySupport.setId(jsonObject.optString("id"));
                                    modelDeliverySupport.setQuestion(jsonObject.optString("question"));
                                    modelDeliverySupport.setAnswer(jsonObject.optString("answer"));
                                    modelDeliverySupport.setCreatedAt(jsonObject.optString("createdAt"));
                                    modelDeliverySupport.setUpdatedAt(jsonObject.optString("updatedAt"));
                                    list.add(modelDeliverySupport);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapterDeliverySupport = new AdapterDeliverySupport(getActivity(), list);
                            recView.setAdapter(adapterDeliverySupport);
                            //adapterDeliverySupport.notifyDataSetChanged();

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

}
