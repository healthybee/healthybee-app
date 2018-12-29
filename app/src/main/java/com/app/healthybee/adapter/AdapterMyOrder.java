package com.app.healthybee.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.healthybee.listeners.CustomAddClickListener;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.models.MyOrder;
import com.app.healthybee.R;

import java.util.ArrayList;

/**
 * Created by root on 20/9/18.
 */

public class AdapterMyOrder extends RecyclerView.Adapter<AdapterMyOrder.ViewHolder> {
    ArrayList<MyOrder> data;

    Context mContext;
    CustomAddClickListener listener;

    @Override
    public AdapterMyOrder.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_my_order_new, parent, false);
        final AdapterMyOrder.ViewHolder mViewHolder = new AdapterMyOrder.ViewHolder(mView);
        /*mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, mViewHolder.getPosition());
            }
        });*/
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterMyOrder.ViewHolder holder, int position) {
        holder.ItemName.setText(Html.fromHtml(data.get(position).getItemName()));
        holder.ItemPrice.setText(Html.fromHtml(data.get(position).getItemPrice()));
        holder.ItemExpire.setText(Html.fromHtml(data.get(position).getItemDescription()));
//        holder.address.setText(Html.fromHtml(data.get(position).getAddress()));
//        holder.PauseForNextDay.setText(Html.fromHtml(data.get(position).getPauseForNextDay()));


        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, holder.getPosition(),"select");
                }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public AdapterMyOrder(Context mContext, ArrayList<MyOrder> data, CustomAddClickListener listener) {
        this.data = data;
        this.mContext = mContext;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ItemName;
        public TextView ItemPrice;
        public TextView ItemExpire;
        public TextView address;
        public TextView PauseForNextDay;


        ViewHolder(View v) {
            super(v);
            ItemName= (TextView) v.findViewById(R.id.tvItemName);
             ItemPrice= (TextView) v.findViewById(R.id.tvItemPrice);
            ItemExpire= (TextView) v.findViewById(R.id.tvItemExpire);
            address= (TextView) v.findViewById(R.id.tv_change_time);
             PauseForNextDay= (TextView) v.findViewById(R.id.tv_pause_for_next_day);

        }
    }
}