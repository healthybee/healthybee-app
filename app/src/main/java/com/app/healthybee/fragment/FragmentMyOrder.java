package com.app.healthybee.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.activities.ActivityAddress;
import com.app.healthybee.models.MyOrder;
import com.app.healthybee.adapter.AdapterMyOrder;
import com.app.healthybee.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMyOrder extends Fragment {

    RecyclerView itemsList;
    AdapterMyOrder adapter;
    ArrayList<MyOrder> data = new ArrayList<>();
    public FragmentMyOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my_order, container, false);
        itemsList = (RecyclerView)view.findViewById(R.id.recycler_view_my_order);
        itemsList.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        itemsList.setLayoutManager(mLinearLayoutManager);
        //let us add some items into the list
        data.add(
                new MyOrder(
                        "Veg Grill Sandwich with Cheese ",
                        "Rs.99.00/Meal",
                        "Your monthly subscription will expire on October 3, 2018",
                        "Address change",
                        "Pause for next day"
                ));

        data.add(
                new MyOrder(
                        "Veg Grill Toast with Cheese Item",
                        "Rs.99.00/Meal",
                        "Your monthly subscription will expire on October 3, 2018",
                        "Address change",
                        "Pause for next day"
                ));
        data.add(
                new MyOrder(
                        "Mayo Bombers with Jalapeno ",
                        "Rs.99.00/Meal",
                        "Your monthly subscription will expire on October 3, 2018",
                        "Address change",
                        "Pause for next day"
                ));


        adapter = new AdapterMyOrder(getActivity(), data, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("TAG", "clicked position:" + position);
                String postId = data.get(position).getItemName();
                Toast.makeText(getActivity(), postId, Toast.LENGTH_SHORT).show();

                Intent intent =new Intent(getActivity(),ActivityAddress.class);
                startActivity(intent);

//                FragmentManager fm =getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fm.beginTransaction().addToBackStack("null");
//                FragmentAddress f1 = new FragmentAddress();
//                fragmentTransaction.add(R.id.container, f1);
//                fragmentTransaction.commit();
            }
        });
        itemsList.setAdapter(adapter);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        assert ((AppCompatActivity)getActivity()) != null;
      //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        assert ((AppCompatActivity)getActivity()) != null;
      //  ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

}
