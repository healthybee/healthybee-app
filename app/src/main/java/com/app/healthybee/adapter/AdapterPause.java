package com.app.healthybee.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.models.Pause;
import com.app.healthybee.R;

import java.util.ArrayList;

/**
 * Created by root on 21/9/18.
 */

public class AdapterPause extends RecyclerView.Adapter<AdapterPause.ViewHolder> {
    ArrayList<Pause> data;

    Context mContext;
    CustomItemClickListener listener;

    @Override
    public AdapterPause.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_pause, parent, false);
        final AdapterPause.ViewHolder mViewHolder = new AdapterPause.ViewHolder(mView);
        /*mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, mViewHolder.getPosition());
            }
        });*/
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterPause.ViewHolder holder, int position) {
        holder.ItemName.setText(Html.fromHtml(data.get(position).getOrderTitle()));
        if (data.get(position).isChecked()) {
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }



        holder.ItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, holder.getPosition());

            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public AdapterPause(Context mContext, ArrayList<Pause> data, CustomItemClickListener listener) {
        this.data = data;
        this.mContext = mContext;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ItemName;
        public CheckBox checkBox;



        ViewHolder(View v) {
            super(v);
            ItemName= (TextView) v.findViewById(R.id.tvPauseItem);
            checkBox= (CheckBox) v.findViewById(R.id.cbIsChacked);

        }
    }
}