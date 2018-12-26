package com.app.healthybee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.healthybee.listeners.UpdateCart;
import com.app.healthybee.models.CategoryItem;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.R;
import com.app.healthybee.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 22/9/18.
 */

public class AdapterCheckOut extends RecyclerView.Adapter<AdapterCheckOut.MyViewHolder> {

    private Context mContext;
    private List<CategoryItem> categoryItemList;
    private CustomItemClickListener listener;
    private UpdateCart updateCart;

    public AdapterCheckOut(Context context, ArrayList<CategoryItem> data, CustomItemClickListener tag, UpdateCart updateCart1) {
        this.mContext = context;
        this.categoryItemList = data;
        this.listener=tag;
        this.updateCart=updateCart1;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemName, tvItemPrice,tv_monthly_subs,tv_quantity;

        private ImageView tvMinus;
        private TextView  tvCount;
        private ImageView tvPlus;
        private LinearLayout llAddRemove;
        private TextView tvAddItem;

        MyViewHolder(View view) {
            super(view);
            //  cardViewAddItem= (CardView) view.findViewById(R.id.cardViewAddItem);
            tvItemName = (TextView) view.findViewById(R.id.tvItemName);
            tvItemPrice = (TextView) view.findViewById(R.id.tvItemPrice);
            tv_monthly_subs = (TextView) view.findViewById(R.id.tv_monthly_subs);
            tv_quantity = (TextView) view.findViewById(R.id.tv_quantity);


            tvMinus= (ImageView) view.findViewById(R.id.tvMinus);
            tvCount= (TextView) view.findViewById(R.id.tvCount);
            tvPlus= (ImageView) view.findViewById(R.id.tvPlus);

            llAddRemove = (LinearLayout) view.findViewById(R.id.llAddRemove);
            tvAddItem = (TextView) view.findViewById(R.id.tvAddItem);


        }
    }


//    public AdapterCheckOut(Context mContext, List<CategoryItem> categoryItemList1, CustomItemClickListener listener1) {
//        this.mContext = mContext;
//        this.categoryItemList = categoryItemList1;
//        this.listener=listener1;
//        dbHelper=new DbHelper(mContext);
//    }

    @NonNull
    @Override
    public AdapterCheckOut.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_check_out, parent, false);
        final AdapterCheckOut.MyViewHolder mViewHolder = new AdapterCheckOut.MyViewHolder(itemView);

//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onItemClick(v, mViewHolder.getPosition());
//            }
//        });
        return new AdapterCheckOut.MyViewHolder(itemView);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final AdapterCheckOut.MyViewHolder holder, final int position) {
        final CategoryItem categoryItem = categoryItemList.get(position);
        holder.tvItemName.setText(Html.fromHtml(categoryItem.getName()));
        holder.tvItemPrice.setText(Html.fromHtml(mContext.getResources().getString(R.string.rs)+" "+categoryItem.getPrice())+"/Meal");

        if (categoryItem.getCount()==0){
            holder.tvAddItem.setVisibility(View.VISIBLE);
            holder.llAddRemove.setVisibility(View.GONE);
        }else {
            holder.tvAddItem.setVisibility(View.GONE);
            holder.llAddRemove.setVisibility(View.VISIBLE);
        }
        holder.tvCount.setText(Html.fromHtml(categoryItem.getCount()+""));
        holder.tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryItem.setCount(categoryItem.getCount()+1);
                holder.tvCount.setText(Html.fromHtml(categoryItem.getCount()+""));
                updateCart.OnAddItemToCart(categoryItemList.get(position), categoryItem.getCount() + 1,Constant.CARD_PLUS);
            }
        });
        holder.tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryItem.getCount()!=0) {
                    categoryItem.setCount(categoryItem.getCount() - 1);
                }
                if (!(categoryItem.getCount()==0)) {
                    holder.tvCount.setText(Html.fromHtml(categoryItem.getCount()+""));
                    updateCart.OnAddItemToCart(categoryItemList.get(position), categoryItem.getCount() + 1,Constant.CARD_MINUS);
                }else {
                    holder.tvAddItem.setVisibility(View.VISIBLE);
                    holder.llAddRemove.setVisibility(View.GONE);
                    updateCart.OnAddItemToCart(categoryItemList.get(position), categoryItem.getCount() + 1,Constant.CARD_DELETE);
                }
            }
        });
        holder.tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryItem.setCount(categoryItem.getCount()+1);
                holder.tvAddItem.setVisibility(View.GONE);
                holder.llAddRemove.setVisibility(View.VISIBLE);
                holder.tvCount.setText(Html.fromHtml(categoryItem.getCount()+""));
                updateCart.OnAddItemToCart(categoryItemList.get(position), categoryItem.getCount() + 1,Constant.CARD_PLUS);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }
}