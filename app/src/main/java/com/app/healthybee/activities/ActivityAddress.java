package com.app.healthybee.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.healthybee.dboperation.DbHelper;
import com.app.healthybee.fragment.FragmentCheckOut;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.R;
import com.app.healthybee.adapter.AdapterAddress;
import com.app.healthybee.models.Address;

import java.util.ArrayList;

public class ActivityAddress extends AppCompatActivity {
    RecyclerView itemsList;
    AdapterAddress adapter;
    ArrayList<Address> data = new ArrayList<>();
    private Activity activity;

    private Address address;
    private Toolbar toolbar;
    private ImageView ivBack;
    private ImageView ivAddAddress;
    private DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        activity=ActivityAddress.this;

        toolbar = findViewById(R.id.toolbarAddress);
        setSupportActionBar(toolbar);
        address = new Address();
        dbHelper=new DbHelper(activity);

        itemsList = (RecyclerView)findViewById(R.id.recycler_view_address);
        itemsList.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(activity);
        itemsList.setLayoutManager(mLinearLayoutManager);
        //let us add some items into the list
        // TODO: 17/11/18  get addres list from table
        data.addAll(dbHelper.getAddressList());

        adapter = new AdapterAddress(activity, data, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("TAG", "clicked position:" + position);
                String postId = data.get(position).getAddressType();
                Toast.makeText(activity, postId, Toast.LENGTH_SHORT).show();


                finish();
                // do what ever you want to do with it
            }
        });
        itemsList.setAdapter(adapter);

        ivBack= findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivAddAddress= findViewById(R.id.ivAddAddress);
        ivAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(activity,ActivityEditAddress.class);
                intent.putExtra("addressObj",address);
                startActivityForResult(intent, 1);
                //startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data1) {
        super.onActivityResult(requestCode, resultCode, data1);
        if (requestCode==1){
            if (resultCode == Activity.RESULT_OK) {
                data.clear();
                data.addAll(dbHelper.getAddressList());
                adapter = new AdapterAddress(activity, data, new CustomItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Log.d("TAG", "clicked position:" + position);
                        String postId = data.get(position).getAddressType();
                        Toast.makeText(activity, postId, Toast.LENGTH_SHORT).show();

                        // do what ever you want to do with it
                    }
                });
                itemsList.setAdapter(adapter);
            }

        }
    }
}
