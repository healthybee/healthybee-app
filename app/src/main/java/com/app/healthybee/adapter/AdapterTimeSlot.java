package com.app.healthybee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.R;

import com.app.healthybee.models.TimeSlot;

import java.util.List;

public class AdapterTimeSlot extends RecyclerView.Adapter<AdapterTimeSlot.MyViewHolder> {

    private Context mContext;
    private List<TimeSlot> timeSlots;
    private CustomItemClickListener listener;


    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTimeSlot;


        MyViewHolder(View view) {
            super(view);

            tvTimeSlot = (TextView) view.findViewById(R.id.tvTimeSlot);


        }
    }


    public AdapterTimeSlot(Context mContext, List<TimeSlot> timeSlots, CustomItemClickListener listener1) {
        this.mContext = mContext;
        this.timeSlots = timeSlots;
        this.listener = listener1;

    }

    @NonNull
    @Override
    public AdapterTimeSlot.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_time_slot, parent, false);
        final AdapterCheckOut.MyViewHolder mViewHolder = new AdapterCheckOut.MyViewHolder(itemView);

        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, mViewHolder.getPosition());
            }
        });*/
        return new AdapterTimeSlot.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final AdapterTimeSlot.MyViewHolder holder, final int position) {
        final TimeSlot timeSlot = timeSlots.get(position);
        if (timeSlot.isSelected()){
            holder.tvTimeSlot.setBackgroundResource(R.drawable.button_background_orange);
            holder.tvTimeSlot.setText(Html.fromHtml(timeSlot.getTimeSlot()));
            holder.tvTimeSlot.setTextColor(Color.parseColor("#FFFFFF"));
        }else {
            holder.tvTimeSlot.setBackgroundResource(R.drawable.button_background_white);
            holder.tvTimeSlot.setText(Html.fromHtml(timeSlot.getTimeSlot()));
            holder.tvTimeSlot.setTextColor(Color.parseColor("#797979"));
        }



        holder.tvTimeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }
}