package com.app.healthybee.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.healthybee.listeners.CustomAddClickListener;
import com.app.healthybee.R;
import com.app.healthybee.models.Address;
import com.app.healthybee.models.AddressModule;

import java.util.ArrayList;

/**
 * Created by root on 20/9/18.
 */

public class AdapterAddress extends RecyclerView.Adapter<AdapterAddress.ViewHolder> {
    ArrayList<AddressModule> data;

    Context mContext;
    CustomAddClickListener listener;

    @Override
    public AdapterAddress.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_address, parent, false);
        final AdapterAddress.ViewHolder mViewHolder = new AdapterAddress.ViewHolder(mView);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, mViewHolder.getPosition(), "select");
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterAddress.ViewHolder holder, final int position) {
        AddressModule address = data.get(position);
        holder.tvAddressType.setText(Html.fromHtml(address.getAddressType()));
        holder.tvFullAddress.setText(Html.fromHtml(address.getLine1() + ", " + address.getLine2()+ ", " + address.getAddressType() + ", " + address.getState()));
        holder.ivEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, position, "edit");
            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public AdapterAddress(Context mContext, ArrayList<AddressModule> data, CustomAddClickListener listener) {
        this.data = data;
        this.mContext = mContext;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAddressType;
        public TextView tvFullAddress;
        public ImageView ivEditAddress;

        ViewHolder(View v) {
            super(v);
            tvAddressType = (TextView) v.findViewById(R.id.tvAddressType);
            tvFullAddress = (TextView) v.findViewById(R.id.tvFullAddress);
            ivEditAddress = (ImageView) v.findViewById(R.id.ivEditAddress);

        }
    }
}