package com.app.healthybee.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.listeners.CustomAddClickListener;
import com.app.healthybee.models.MyOrder;
import com.app.healthybee.adapter.AdapterMyOrder;
import com.app.healthybee.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMyOrder extends Fragment {
    private ImageView ivBack;
    private RecyclerView itemsList;
    private AdapterMyOrder adapter;
    private Toolbar toolbar;

    private ArrayList<MyOrder> data = new ArrayList<>();
    public FragmentMyOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my_order, container, false);
        toolbar = view.findViewById(R.id.toolbarMyOrder);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
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


        adapter = new AdapterMyOrder(getActivity(), data, new CustomAddClickListener() {
            @Override
            public void onItemClick(View v, int position,String type) {
                Log.d("TAG", "clicked position:" + position);
                String postId = data.get(position).getItemName();
                Toast.makeText(getActivity(), postId, Toast.LENGTH_SHORT).show();
                // TODO: 29/12/18  change timing
            }
        });
        itemsList.setAdapter(adapter);
        ivBack = view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).exitApp();
                //finish();
            }
        });
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

}
