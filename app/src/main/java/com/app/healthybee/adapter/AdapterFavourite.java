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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.healthybee.R;
import com.app.healthybee.RoundedCornersTransformation;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.listeners.UpdateCartCategoryItem;
import com.app.healthybee.models.CategoryItem;
import com.app.healthybee.utils.Constant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class AdapterFavourite extends RecyclerView.Adapter<AdapterFavourite.ViewHolder> {
    private Context context;
    private ArrayList<CategoryItem> list;
    private CustomItemClickListener customItemClickListener;
    private UpdateCartCategoryItem cartCategoryItem;

    public AdapterFavourite(Context context, ArrayList<CategoryItem> list, CustomItemClickListener customItemClickListener,UpdateCartCategoryItem cartCategoryItem) {
        this.context = context;
        this.list = list;
        this.customItemClickListener = customItemClickListener;
        this.cartCategoryItem = cartCategoryItem;
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
    public void onBindViewHolder(@NonNull final AdapterFavourite.ViewHolder holder, final int position) {
        final CategoryItem categoryItem = list.get(position);
        holder.title.setText(Html.fromHtml(categoryItem.getName()));
        holder.tv_old_price.setText(Html.fromHtml(context.getResources().getString(R.string.rs) + " " + categoryItem.getOld_price()));
        holder.tv_old_price.setPaintFlags(holder.tv_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_new_price.setText(Html.fromHtml(context.getResources().getString(R.string.rs) + " " + categoryItem.getPrice() + ".00/Meal"));
        Glide.with(context).load(categoryItem.getImage_url().replace(" ", "%20"))
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(context, 10, 0)).error(R.drawable.ic_no_item))
                .into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
        if (categoryItem.getCount() == 0) {
            holder.tvAddItem.setVisibility(View.VISIBLE);
            holder.llAddRemove.setVisibility(View.GONE);
        } else {
            holder.tvAddItem.setVisibility(View.GONE);
            holder.llAddRemove.setVisibility(View.VISIBLE);
        }
        holder.tvCount.setText(Html.fromHtml(categoryItem.getCount() + ""));

        holder.tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartCategoryItem.OnAddItemToCart(list.get(position), categoryItem.getCount(), Constant.CARD_PLUS,position);
            }
        });
        holder.tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryItem.getCount()> 1) {
                    cartCategoryItem.OnAddItemToCart(list.get(position), categoryItem.getCount(), Constant.CARD_MINUS,position);
                }else {
                    cartCategoryItem.OnAddItemToCart(list.get(position), categoryItem.getCount(), Constant.CARD_DELETE,position);
                }
            }
        });
        holder.tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartCategoryItem.OnAddItemToCart(categoryItem, categoryItem.getCount() , Constant.CARD_PLUS,position);
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
        private TextView tvCount;
        private ImageView tvPlus, tvMinus;
        private LinearLayout llAddRemove;
        private TextView tvAddItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            tv_old_price = itemView.findViewById(R.id.tv_old_price);
            tv_new_price = itemView.findViewById(R.id.tv_new_price);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            tvMinus = itemView.findViewById(R.id.tvMinus);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvPlus = itemView.findViewById(R.id.tvPlus);
            llAddRemove = itemView.findViewById(R.id.llAddRemove);
            tvAddItem = itemView.findViewById(R.id.tvAddItem);

        }
    }
}
