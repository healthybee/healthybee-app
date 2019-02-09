package com.app.healthybee.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.healthybee.R;
import com.app.healthybee.RoundedCornersTransformation;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.models.FavouriteModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class AdapterFavourite extends RecyclerView.Adapter<AdapterFavourite.ViewHolder> {
    private Context context;
    private ArrayList<FavouriteModel> list;
    private CustomItemClickListener customItemClickListener;

    public AdapterFavourite(Context context, ArrayList<FavouriteModel> list, CustomItemClickListener customItemClickListener1) {
        this.context = context;
        this.list = list;
        this.customItemClickListener = customItemClickListener1;
    }

    @NonNull
    @Override
    public AdapterFavourite.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        final View view = LayoutInflater.from(context).inflate(R.layout.raw_favourite, viewGroup, false);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                customItemClickListener.onItemClick(view, i);
//            }
//        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterFavourite.ViewHolder holder, int position) {
        FavouriteModel favouriteModel = list.get(position);
        holder.title.setText(Html.fromHtml(favouriteModel.getResult().get(0).getName()));
        holder.tv_old_price.setText(Html.fromHtml(context.getResources().getString(R.string.rs) + " " + favouriteModel.getResult().get(0).getOldPrice()));
        holder.tv_old_price.setPaintFlags(holder.tv_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_new_price.setText(Html.fromHtml(context.getResources().getString(R.string.rs) + " " + favouriteModel.getResult().get(0).getPrice() + ".00/Meal"));
        Glide.with(context).load(favouriteModel.getResult().get(0).getImageUrl().replace(" ", "%20"))
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(context, 10, 0)).error(R.drawable.ic_no_item))
                .into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView tv_old_price;
        TextView tv_new_price;
        ImageView thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            tv_old_price = itemView.findViewById(R.id.tv_old_price);
            tv_new_price = itemView.findViewById(R.id.tv_new_price);
            thumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }
}
