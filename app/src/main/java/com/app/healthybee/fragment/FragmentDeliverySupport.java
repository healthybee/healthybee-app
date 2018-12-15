package com.app.healthybee.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.healthybee.R;
import com.app.healthybee.adapter.AdapterDeliverySupport;
import com.app.healthybee.models.ModelDeliverySupport;

import java.util.ArrayList;

public class FragmentDeliverySupport extends Fragment {
    RecyclerView recView;
    AdapterDeliverySupport adapterDeliverySupport;
    ArrayList<ModelDeliverySupport> list;
    LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_delivery_support, container, false);
        list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        recView = view.findViewById(R.id.recyclerView);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(layoutManager);

        for (int i = 0; i < 5; i++) {
            list.add(new ModelDeliverySupport("Question " + i, " Answer " + i));
        }

        adapterDeliverySupport = new AdapterDeliverySupport(getActivity(), list);
        recView.setAdapter(adapterDeliverySupport);
        adapterDeliverySupport.notifyDataSetChanged();
        return view;
    }

}
