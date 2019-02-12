package com.app.healthybee.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.R;
import com.app.healthybee.listeners.UpdateCartCategoryItem;
import com.app.healthybee.models.CategoryItem;
import com.app.healthybee.utils.Constant;

import java.util.ArrayList;


public class AdapterItemsList extends RecyclerView.Adapter<AdapterItemsList.ViewHolder> {
    ArrayList<CategoryItem> data;

    Context mContext;
    CustomItemClickListener listener;
    private UpdateCartCategoryItem updateCartCategoryItem;
    public AdapterItemsList(Context context, ArrayList<CategoryItem> data1, CustomItemClickListener listener1, UpdateCartCategoryItem updateCartCategoryItem) {
        this.data = data1;
        this.mContext = context;
        this.listener = listener1;
        this.updateCartCategoryItem = updateCartCategoryItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_suggestion_meel, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, mViewHolder.getPosition());
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CategoryItem itemListSingleItem = data.get(position);
        holder.itemTitle.setText(Html.fromHtml(itemListSingleItem.getName()));

        double strPrice= Double.parseDouble(itemListSingleItem.getPrice());
        holder.tv_price.setText(Html.fromHtml(String.format("%.2f",strPrice)+"/Meal"));

        if (itemListSingleItem.getCount()==0){
            holder.tvAddItem.setVisibility(View.VISIBLE);
            holder.llAddRemove.setVisibility(View.GONE);
        }else {
            holder.tvAddItem.setVisibility(View.GONE);
            holder.llAddRemove.setVisibility(View.VISIBLE);
        }
        holder.tvCount.setText(Html.fromHtml(itemListSingleItem.getCount()+""));
        holder.tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListSingleItem.setCount(itemListSingleItem.getCount()+1);
                holder.tvCount.setText(Html.fromHtml(itemListSingleItem.getCount()+""));
                updateCartCategoryItem.OnAddItemToCart(data.get(position), itemListSingleItem.getCount() + 1,Constant.CARD_PLUS,position);
            }
        });
        holder.tvMinus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (itemListSingleItem.getCount()!=0)
                    itemListSingleItem.setCount(itemListSingleItem.getCount() - 1);
                if (!(itemListSingleItem.getCount()==0)) {
                    holder.tvCount.setText(Html.fromHtml(itemListSingleItem.getCount()+""));
                    updateCartCategoryItem.OnAddItemToCart(data.get(position), itemListSingleItem.getCount() + 1,Constant.CARD_MINUS,position);
                }else {
                    holder.tvAddItem.setVisibility(View.VISIBLE);
                    holder.llAddRemove.setVisibility(View.GONE);
                    updateCartCategoryItem.OnAddItemToCart(data.get(position), itemListSingleItem.getCount() + 1,Constant.CARD_DELETE,position);
                }
            }
        });
        holder.tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListSingleItem.setCount(itemListSingleItem.getCount()+1);
                holder.tvAddItem.setVisibility(View.GONE);
                holder.llAddRemove.setVisibility(View.VISIBLE);
                holder.tvCount.setText(Html.fromHtml(itemListSingleItem.getCount()+""));
                updateCartCategoryItem.OnAddItemToCart(data.get(position), itemListSingleItem.getCount() + 1,Constant.CARD_PLUS,position);
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

  /*  public AdapterItemsList(Context mContext, ArrayList<CategoryItem> data, CustomItemClickListener listener) {
        this.data = data;
        this.mContext = mContext;
        this.listener = listener;
    }
*/
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTitle;
        public TextView tv_price;
        private ImageView tvMinus;
      private ImageView tvPlus;
      private TextView  tvCount;
        private LinearLayout llAddRemove;
        private TextView tvAddItem;

        ViewHolder(View v) {
            super(v);
            itemTitle = (TextView) v.findViewById(R.id.tv_title);
            tv_price = (TextView) v.findViewById(R.id.tv_price);
            tvMinus= (ImageView) v.findViewById(R.id.tvMinus);
            tvCount= (TextView) v.findViewById(R.id.tvCount);
            tvPlus= (ImageView) v.findViewById(R.id.tvPlus);
            llAddRemove = (LinearLayout) v.findViewById(R.id.llAddRemove);
            tvAddItem = (TextView) v.findViewById(R.id.tvAddItem);

        }
    }
}