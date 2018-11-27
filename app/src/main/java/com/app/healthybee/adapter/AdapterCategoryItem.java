package com.app.healthybee.adapter;

import android.annotation.SuppressLint;
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

import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.R;
import com.app.healthybee.listeners.UpdateCart;

import com.app.healthybee.dboperation.DbHelper;
import com.app.healthybee.models.CategoryItem;
import com.app.healthybee.utils.Constant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.util.List;

import static com.app.healthybee.fragment.FragmentHome.mFlagDisplayList;


public class AdapterCategoryItem extends RecyclerView.Adapter<AdapterCategoryItem.MyViewHolder> {

    private Context mContext;
    private List<CategoryItem> categoryItemList;
    private CustomItemClickListener listener;
    private UpdateCart updateCart;
    private DbHelper dbHelper;

    public AdapterCategoryItem(Context mContext, List<CategoryItem> categoryItemList1, CustomItemClickListener listener1, UpdateCart updateCart) {
        this.mContext = mContext;
        this.categoryItemList = categoryItemList1;
        this.listener = listener1;
        this.updateCart = updateCart;


    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView tv_old_price;
        private TextView tv_new_price;
        private ImageView thumbnail;
        private TextView tvMinus, tvCount, tvPlus;

        private LinearLayout llAddRemove;
        private TextView tvAddItem;

        MyViewHolder(View view) {
            super(view);
            //  cardViewAddItem= (CardView) view.findViewById(R.id.cardViewAddItem);
            title = (TextView) view.findViewById(R.id.title);
            tv_old_price = (TextView) view.findViewById(R.id.tv_old_price);
            tv_new_price = (TextView) view.findViewById(R.id.tv_new_price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            tvMinus = (TextView) view.findViewById(R.id.tvMinus);
            tvCount = (TextView) view.findViewById(R.id.tvCount);
            tvPlus = (TextView) view.findViewById(R.id.tvPlus);
            llAddRemove = (LinearLayout) view.findViewById(R.id.llAddRemove);
            tvAddItem = (TextView) view.findViewById(R.id.tvAddItem);

        }
    }


    /*public AdapterCategoryItem(Context mContext, List<CategoryItem> albumList, CustomItemClickListener listener1) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.listener = listener1;
    }*/

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        // final MyViewHolder mViewHolder = new MyViewHolder(itemView);
        if (mFlagDisplayList) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.album_card, parent, false);
        } else {

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.album_card_grid, parent, false);
        }

//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onItemClick(v, mViewHolder.getPosition());
//            }
//        });
        return new MyViewHolder(itemView);
    }

    @SuppressLint({"SetTextI18n", "Range"})
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final CategoryItem categoryItem = categoryItemList.get(position);

        holder.title.setText(Html.fromHtml(categoryItem.getName()));
        holder.tv_old_price.setText(Html.fromHtml(mContext.getResources().getString(R.string.rs)+" "+categoryItem.getOld_price()));
        holder.tv_old_price.setPaintFlags(holder.tv_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_new_price.setText(Html.fromHtml(mContext.getResources().getString(R.string.rs)+" "+categoryItem.getPrice()+".00/Meal"));

//        Glide.with(mContext)
//                .load(categoryItem.getImage_url().replace(" ", "%20"))
//                .into(holder.thumbnail);

        Glide.with(mContext)
                .load(categoryItem.getImage_url().replace(" ", "%20"))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.load)
                        .fitCenter()
                        .error(R.drawable.ic_no_item))
                .into(holder.thumbnail);

//        Picasso.with(mContext)
//                .load(categoryItem.getImage_url().replace(" ", "%20"))
//                .placeholder(R.drawable.ic_thumbnail)
//                .into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, holder.getPosition());
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
                categoryItem.setCount(categoryItem.getCount() + 1);
                holder.tvCount.setText(Html.fromHtml(categoryItem.getCount() + ""));
                updateCart.OnAddItemToCart(categoryItemList.get(position), categoryItem.getCount() + 1,Constant.CARD_PLUS);
            }
        });
        holder.tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryItem.getCount() != 0) {
                    categoryItem.setCount(categoryItem.getCount() - 1);
                }
                if (!(categoryItem.getCount() == 0)) {
                    holder.tvCount.setText(Html.fromHtml(categoryItem.getCount() + ""));
                    updateCart.OnAddItemToCart(categoryItemList.get(position), categoryItem.getCount() - 1,Constant.CARD_MINUS);
                } else {
                    dbHelper=new DbHelper(mContext);
                    dbHelper.deleteCartRow(categoryItem.getName());
                    ((MainActivity) mContext).setCountText();
                    holder.tvAddItem.setVisibility(View.VISIBLE);
                    holder.llAddRemove.setVisibility(View.GONE);
                }
            }
        });
        holder.tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryItem.setCount(categoryItem.getCount() + 1);
                holder.tvAddItem.setVisibility(View.GONE);
                holder.llAddRemove.setVisibility(View.VISIBLE);
                holder.tvCount.setText(Html.fromHtml(categoryItem.getCount() + ""));
                updateCart.OnAddItemToCart(categoryItemList.get(position), categoryItem.getCount() + 1, Constant.CARD_PLUS);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }
}