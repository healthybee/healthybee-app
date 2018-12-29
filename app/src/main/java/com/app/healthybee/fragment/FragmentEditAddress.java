package com.app.healthybee.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.healthybee.R;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.dboperation.DbHelper;
import com.app.healthybee.models.Address;


public class FragmentEditAddress extends Fragment {

    private View root_view;
    private LinearLayout lyt_root;


    private Address address;
    private Toolbar toolbar;
    private ImageView ivBack;
    private TextView tvSaveAddress;
    private DbHelper dbHelper;

    //    private EditText etAddressType;
//    private EditText etAddressLine1;
//    private EditText etAddressLine2;

    private TextInputEditText etAddressType;
    private TextInputEditText etAddressLine1;
    private TextInputEditText etAddressLine2;

    private EditText etAddressCity;
    private EditText etAddressState;
    //    private EditText etAddressZipCode;
//    private EditText etAddressLandmark;
    private TextInputEditText etAddressZipCode;
    private TextInputEditText etAddressLandmark;
    private String strAddressType;
    private String strAddressLine1;
    private String strAddressLine2;
    private String strAddressCity;
    private String strAddressState;
    private String strAddressZipCode;
    private String strAddressLandmark;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.activity_edit_address, null);
        lyt_root = root_view.findViewById(R.id.root_layout);
        toolbar = root_view.findViewById(R.id.toolbarEditAddress);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        initView(root_view);

//        //let us add some items into the list
//        address = new Address();
//        Intent intent = getIntent();
//        if (null != intent) {
//            if (intent.hasExtra("addressObj")) {
//                address = intent.getParcelableExtra("addressObj");
//            }
//        }
        address = new Address();
        Bundle bundle = getArguments();
        assert bundle != null;
        address= bundle.getParcelable("addressObj");

        dbHelper = new DbHelper(getActivity());
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
                    long isSaved = dbHelper.insertUpdateAddress(address);
                    if (isSaved > 0) {
                        ((MainActivity) getActivity()).exitApp();
                    }else {
                        Toast.makeText(getActivity(), "Address not saved please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        setInitialAddress();

        return root_view;
    }
    private void setInitialAddress() {
        if (null != address.getAddressType()) {
            etAddressType.setText(address.getAddressType());
        }
        if (null != address.getAddressLine1()) {
            etAddressLine1.setText(address.getAddressLine1());
        }
        if (null != address.getAddressLine2()) {
            etAddressLine2.setText(address.getAddressLine2());
        }
        if (null != address.getAddressCity()) {
            etAddressCity.setText(address.getAddressCity());
        }
        if (null != address.getAddressState()) {
            etAddressState.setText(address.getAddressState());
        }
        if (null != address.getAddressZipCode()) {
            etAddressZipCode.setText(address.getAddressZipCode());
        }
        if (null != address.getAddressLandmark()) {
            etAddressLandmark.setText(address.getAddressLandmark());
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
        address.setAddressLine1(strAddressLine1);
        address.setAddressLine2(strAddressLine2);
        address.setAddressCity(strAddressCity);
        address.setAddressState(strAddressState);
        address.setAddressZipCode(strAddressZipCode);
        address.setAddressLandmark(strAddressLandmark);
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
