package com.app.healthybee.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.healthybee.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEditProfile extends Fragment {


    public FragmentEditProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_edit_profile, container, false);
    }

}