package com.app.healthybee.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.healthybee.R;
import com.app.healthybee.activities.ActivitySubscribe;
import com.app.healthybee.activities.ActivityUserLogin;
import com.app.healthybee.activities.Applications;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.dboperation.DbHelper;
import com.app.healthybee.models.Address;
import com.app.healthybee.models.AddressModule;
import com.app.healthybee.utils.MyCustomProgressDialog;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.utils.SharedPrefUtil;
import com.app.healthybee.utils.UrlConstants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FragmentEditAddress extends Fragment {

    private View root_view;

    private AddressModule address;
    private Toolbar toolbar;
    private ImageView ivBack;
    private TextView tvSaveAddress;
    private TextInputEditText etAddressType;
    private TextInputEditText etAddressLine1;
    private TextInputEditText etAddressLine2;
    private EditText etAddressCity;
    private EditText etAddressState;
    private TextInputEditText etAddressZipCode;
    private TextInputEditText etAddressLandmark;
    private String strAddressType;
    private String strAddressLine1;
    private String strAddressLine2;
    private String strAddressCity;
    private String strAddressState;
    private String strAddressZipCode;
    private String strAddressLandmark;
    private String operationType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.activity_edit_address, null);
        toolbar = root_view.findViewById(R.id.toolbarEditAddress);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        initView(root_view);

        address = new AddressModule();
        Bundle bundle = getArguments();
        assert bundle != null;
        address= bundle.getParcelable("addressObj");
        operationType= bundle.getString("operationType");
        ivBack = root_view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).exitApp();
                //finish();
            }
        });
        tvSaveAddress = root_view.findViewById(R.id.tvSaveAddress);
        tvSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData() == 1) {
                    setAddressData();
                    if (operationType.equalsIgnoreCase("ADD")) {
                        SaveAddressData();
                    }
                    if (operationType.equalsIgnoreCase("EDIT")){
                        UpdateAddressData();
                    }
                }
            }
        });

        setInitialAddress();

        return root_view;
    }

    private void UpdateAddressData() {
        if (NetworkConstants.isConnectingToInternet(getActivity())) {
            MyCustomProgressDialog.showDialog(getActivity(), getString(R.string.please_wait));
            Map<String, String> params = new HashMap<>();
            params.put("addressType",strAddressType);
            params.put("line1", strAddressLine1);
            params.put("line2",strAddressLine2);
            params.put("city", strAddressCity);
            params.put("state",strAddressState);
            params.put("zipcode", strAddressZipCode);
            params.put("landmark",strAddressLandmark);
            Log.d("4343", new JSONObject(params).toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    UrlConstants.updateAddresses+address.getId(),
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("4343", response.toString());
                            MyCustomProgressDialog.dismissDialog();
                            ((MainActivity) getActivity()).exitApp();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("4343", "Site Info Error: " + error.getMessage());
                    MyCustomProgressDialog.dismissDialog();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(getActivity()));

                    return headers;
                }
            };
            Log.d("4343", jsonObjectRequest.toString());
            Applications.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(getActivity(), getString(R.string.network_title), getString(R.string.network_message));
        }
    }


    private void SaveAddressData() {
        if (NetworkConstants.isConnectingToInternet(getActivity())) {
            MyCustomProgressDialog.showDialog(getActivity(), getString(R.string.please_wait));
            Map<String, String> params = new HashMap<>();
            params.put("addressType",strAddressType);
            params.put("line1", strAddressLine1);
            params.put("line2",strAddressLine2);
            params.put("city", strAddressCity);
            params.put("state",strAddressState);
            params.put("zipcode", strAddressZipCode);
            params.put("landmark",strAddressLandmark);
            Log.d("4343", new JSONObject(params).toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    UrlConstants.createAddresses,
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("4343", response.toString());
                            MyCustomProgressDialog.dismissDialog();
                            ((MainActivity) getActivity()).exitApp();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("4343", "Site Info Error: " + error.getMessage());
                    MyCustomProgressDialog.dismissDialog();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(getActivity()));

                    return headers;
                }
            };
            Log.d("4343", jsonObjectRequest.toString());
            Applications.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            MyCustomProgressDialog.showAlertDialogMessage(getActivity(), getString(R.string.network_title), getString(R.string.network_message));
        }
    }

    private void setInitialAddress() {
        if (null != address.getAddressType()) {
            etAddressType.setText(address.getAddressType());
        }
        if (null != address.getLine1()) {
            etAddressLine1.setText(address.getLine1());
        }
        if (null != address.getLine2()) {
            etAddressLine2.setText(address.getLine2());
        }
        if (null != address.getCity()) {
            etAddressCity.setText(address.getCity());
        }
        if (null != address.getState()) {
            etAddressState.setText(address.getState());
        }
        if (null != address.getCreatedAt()) {
            etAddressZipCode.setText(address.getZipcode());
        }
        if (null != address.getUpdatedAt()) {
            etAddressLandmark.setText(address.getLandmark());
        }
    }

    private void setAddressData() {
        strAddressType = etAddressType.getText().toString().trim();
        strAddressLine1 = etAddressLine1.getText().toString().trim();
        strAddressLine2 = etAddressLine2.getText().toString().trim();
        strAddressCity = etAddressCity.getText().toString().trim();
        strAddressState = etAddressState.getText().toString().trim();
        strAddressZipCode = etAddressZipCode.getText().toString().trim();
        strAddressLandmark = etAddressLandmark.getText().toString().trim();
        address.setAddressType(strAddressType);
        address.setLine1(strAddressLine1);
        address.setLine2(strAddressLine2);
        address.setCity(strAddressCity);
        address.setState(strAddressState);
        address.setZipcode(strAddressZipCode);
        address.setLandmark(strAddressLandmark);
    }

    private int validateData() {
        strAddressType = etAddressType.getText().toString().trim();
        strAddressLine1 = etAddressLine1.getText().toString().trim();
        strAddressLine2 = etAddressLine2.getText().toString().trim();
        strAddressCity = etAddressCity.getText().toString().trim();
        strAddressState = etAddressState.getText().toString().trim();
        strAddressZipCode = etAddressZipCode.getText().toString().trim();
        strAddressLandmark = etAddressLandmark.getText().toString().trim();

        if (TextUtils.isEmpty(strAddressType)) {
            etAddressType.setError("Enter address type");
            return 0;
        } else if (TextUtils.isEmpty(strAddressLine1)) {
            etAddressLine1.setError("Enter address line 1");
            return 0;

        } else if (TextUtils.isEmpty(strAddressLine2)) {
            etAddressLine2.setError("Enter address line 2");
            return 0;

        } else if (TextUtils.isEmpty(strAddressCity)) {
            etAddressCity.setError("Enter city");
            return 0;

        } else if (TextUtils.isEmpty(strAddressState)) {
            etAddressState.setError("Enter state");
            return 0;
        } else if (TextUtils.isEmpty(strAddressZipCode)) {
            etAddressZipCode.setError("Enter zip code");
            return 0;
        } else if (TextUtils.isEmpty(strAddressLandmark)) {
            etAddressLandmark.setError("Enter landmark");
            return 0;
        } else {
            return 1;
        }


    }

    private void initView(View root_view) {
        etAddressType = root_view.findViewById(R.id.etAddressType);
        etAddressLine1 = root_view.findViewById(R.id.etAddressLine1);
        etAddressLine2 = root_view.findViewById(R.id.etAddressLine2);
        etAddressCity = root_view.findViewById(R.id.etAddressCity);
        etAddressState = root_view.findViewById(R.id.etAddressState);
        etAddressZipCode = root_view.findViewById(R.id.etAddressZipCode);
        etAddressLandmark = root_view.findViewById(R.id.etAddressLandmark);
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
