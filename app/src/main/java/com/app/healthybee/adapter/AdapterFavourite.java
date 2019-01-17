package com.app.healthybee.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.healthybee.R;
import com.app.healthybee.models.FavouriteModel;

import java.util.ArrayList;

public class AdapterFavourite extends RecyclerView.Adapter<AdapterFavourite.ViewHolder> {
    Context context;
    ArrayList<FavouriteModel> list;

    public AdapterFavourite(Context context, ArrayList<FavouriteModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterFavourite.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.raw_favourite, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFavourite.ViewHolder holder, int position) {
        FavouriteModel pos = list.get(position);
        holder.title.setText(Html.fromHtml(pos.getTitle()));
        holder.tv_old_price.setText(Html.fromHtml(context.getResources().getString(R.string.rs)+" "+pos.getOld_price()));
        holder.tv_old_price.setPaintFlags(holder.tv_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_new_price.setText(Html.fromHtml(context.getResources().getString(R.string.rs)+" "+pos.getNew_price()+".00/Meal"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, tv_old_price, tv_new_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.title);
            tv_old_price= itemView.findViewById(R.id.tv_old_price);
            tv_new_price= itemView.findViewById(R.id.tv_new_price);

        }
    }
}
