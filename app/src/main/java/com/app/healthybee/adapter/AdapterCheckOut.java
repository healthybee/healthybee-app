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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.healthybee.listeners.UpdateCart;
import com.app.healthybee.listeners.UpdateCart1;
import com.app.healthybee.models.Cart;
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
    private List<Cart> categoryItemList;
    private ArrayList<String> mSpinnerDataList;
    private CustomItemClickListener listener;
    private UpdateCart1 updateCart;

    public AdapterCheckOut(Context context, ArrayList<Cart> data,ArrayList<String> mSpinnerData, CustomItemClickListener tag, UpdateCart1 updateCart1) {
        this.mContext = context;
        this.categoryItemList = data;
        this.mSpinnerDataList=mSpinnerData;
        this.listener=tag;
        this.updateCart=updateCart1;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemName, tvItemPrice,tv_quantity;
        private ImageView tvMinus;
        private TextView  tvCount;
        private ImageView tvPlus;
        private LinearLayout llAddRemove;
        private TextView tvAddItem,tvWeakMonthPrice;
        private Spinner spWeakMonth;

        MyViewHolder(View view) {
            super(view);
            tvItemName =  view.findViewById(R.id.tvItemName);
            tvItemPrice =  view.findViewById(R.id.tvItemPrice);
            tv_quantity =  view.findViewById(R.id.tv_quantity);
            spWeakMonth =  view.findViewById(R.id.spWeakMonth);
            tvWeakMonthPrice=  view.findViewById(R.id.tvWeakMonthPrice);
            tvMinus=  view.findViewById(R.id.tvMinus);
            tvCount=  view.findViewById(R.id.tvCount);
            tvPlus=  view.findViewById(R.id.tvPlus);
            llAddRemove =  view.findViewById(R.id.llAddRemove);
            tvAddItem =  view.findViewById(R.id.tvAddItem);


        }
    }



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
        final Cart cart = categoryItemList.get(position);
        holder.tvItemName.setText(Html.fromHtml(cart.getResult().get(0).getName()));
        holder.tvItemPrice.setText(Html.fromHtml(mContext.getResources().getString(R.string.rs)+" "+cart.getResult().get(0).getPrice())+"/Meal");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mSpinnerDataList);
        holder.spWeakMonth.setAdapter(adapter);
        holder.spWeakMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(holder.itemView.getContext(), position+" : "+ holder.spWeakMonth.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
              // spinnerData.setSelectedData(position, binding.optionSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (cart.getQuantity()==0){
            holder.tvAddItem.setVisibility(View.VISIBLE);
            holder.llAddRemove.setVisibility(View.GONE);
        }else {
            holder.tvAddItem.setVisibility(View.GONE);
            holder.llAddRemove.setVisibility(View.VISIBLE);
        }
        holder.tvCount.setText(Html.fromHtml(cart.getQuantity()+""));
        holder.tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 31/1/19 web call
                cart.setQuantity(cart.getQuantity()+1);
                holder.tvCount.setText(Html.fromHtml(cart.getQuantity()+""));
                updateCart.OnAddItemToCart(position,categoryItemList.get(position), cart.getQuantity() + 1,Constant.CARD_PLUS);
            }
        });
        holder.tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.getQuantity()!=0) {
                    // TODO: 31/1/19 web call
                    cart.setQuantity(cart.getQuantity() - 1);
                }
                if (!(cart.getQuantity()==0)) {
                    holder.tvCount.setText(Html.fromHtml(cart.getQuantity()+""));
                    updateCart.OnAddItemToCart(position,categoryItemList.get(position), cart.getQuantity() + 1,Constant.CARD_MINUS);
                }else {
                    holder.tvAddItem.setVisibility(View.VISIBLE);
                    holder.llAddRemove.setVisibility(View.GONE);
                    updateCart.OnAddItemToCart(position,categoryItemList.get(position), cart.getQuantity() + 1,Constant.CARD_DELETE);
                }
            }
        });
        holder.tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 31/1/19 web call
                cart.setQuantity(cart.getQuantity()+1);
                holder.tvAddItem.setVisibility(View.GONE);
                holder.llAddRemove.setVisibility(View.VISIBLE);
                holder.tvCount.setText(Html.fromHtml(cart.getQuantity()+""));
                updateCart.OnAddItemToCart(position,categoryItemList.get(position), cart.getQuantity() + 1,Constant.CARD_PLUS);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }
}