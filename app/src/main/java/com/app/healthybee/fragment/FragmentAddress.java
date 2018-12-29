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
import android.widget.LinearLayout;
import android.widget.Toast;


import com.app.healthybee.R;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.adapter.AdapterAddress;
import com.app.healthybee.dboperation.DbHelper;
import com.app.healthybee.listeners.CustomAddClickListener;
import com.app.healthybee.models.Address;
import java.util.ArrayList;


public class FragmentAddress extends Fragment {

    private View root_view;
    private LinearLayout lyt_root;

    private RecyclerView itemsList;
    private ArrayList<Address> data ;

    private Address address;
    private Toolbar toolbar;
    private ImageView ivBack;
    private ImageView ivAddAddress;
    private DbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.activity_address, null);
        lyt_root = root_view.findViewById(R.id.root_layout);


        toolbar = root_view.findViewById(R.id.toolbarAddress);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        data = new ArrayList<>();
        address = new Address();

        dbHelper = new DbHelper(getActivity());

        itemsList = (RecyclerView) root_view.findViewById(R.id.recycler_view_address);
        itemsList.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        itemsList.setLayoutManager(mLinearLayoutManager);
        //let us add some items into the list
        // TODO: 17/11/18  get address list from table
        data.addAll(dbHelper.getAddressList());

        AdapterAddress adapter = new AdapterAddress(getActivity(), data, new CustomAddClickListener() {
            @Override
            public void onItemClick(View v, int position, String type) {
                if (type.equalsIgnoreCase("select")) {
                    Log.d("TAG", "clicked position:" + position);
                    MainActivity.address=data.get(position);
                    ((MainActivity) getActivity()).exitApp();
                }
                if (type.equalsIgnoreCase("edit")) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("addressObj", data.get(position));
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
                //finish();
            }
        });
        ivAddAddress = root_view.findViewById(R.id.ivAddAddress);
        ivAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putParcelable("addressObj",  address);
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

}
