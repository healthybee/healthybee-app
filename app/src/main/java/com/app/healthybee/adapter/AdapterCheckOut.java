package com.app.healthybee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.healthybee.listeners.PriceUpdaterListener;
import com.app.healthybee.listeners.UpdateCartCartModule;
import com.app.healthybee.models.CartModule;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.R;
import com.app.healthybee.utils.Constant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by root on 22/9/18.
 */

public class AdapterCheckOut extends RecyclerView.Adapter<AdapterCheckOut.MyViewHolder> {

    private Context mContext;
    private List<CartModule> categoryItemList;
    private ArrayList<String> mSpinnerDataList;
    private PriceUpdaterListener priceUpdaterListener;
    private UpdateCartCartModule updateCart;

    public AdapterCheckOut(Context context, ArrayList<CartModule> data, ArrayList<String> mSpinnerData, PriceUpdaterListener priceUpdaterListener, UpdateCartCartModule updateCartCartModule) {
        this.mContext = context;
        this.categoryItemList = data;
        this.mSpinnerDataList = mSpinnerData;
        this.priceUpdaterListener = priceUpdaterListener;
        this.updateCart = updateCartCartModule;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemName;
        private TextView tvItemPrice;
        private TextView tv_quantity;
        private ImageView tvMinus;
        private TextView tvCount;
        private ImageView tvPlus;
        private LinearLayout llAddRemove;
        private TextView tvAddItem;
        private TextView tvWeakMonthPrice;
        private Spinner spWeakMonth;

        MyViewHolder(View view) {
            super(view);
            tvItemName = view.findViewById(R.id.tvItemName);
            tvItemPrice = view.findViewById(R.id.tvItemPrice);
            tv_quantity = view.findViewById(R.id.tv_quantity);
            spWeakMonth = view.findViewById(R.id.spWeakMonth);
            tvWeakMonthPrice = view.findViewById(R.id.tvWeakMonthPrice);
            tvMinus = view.findViewById(R.id.tvMinus);
            tvCount = view.findViewById(R.id.tvCount);
            tvPlus = view.findViewById(R.id.tvPlus);
            llAddRemove = view.findViewById(R.id.llAddRemove);
            tvAddItem = view.findViewById(R.id.tvAddItem);


        }
    }


    @NonNull
    @Override
    public AdapterCheckOut.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull final AdapterCheckOut.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final CartModule cart = categoryItemList.get(position);
        holder.tvItemName.setText(Html.fromHtml(cart.getResult().get(0).getName()));
        holder.tvItemPrice.setText(Html.fromHtml(mContext.getResources().getString(R.string.rs) + " " + cart.getResult().get(0).getPrice()) + "/Meal");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mSpinnerDataList);
        holder.spWeakMonth.setAdapter(adapter);

        if (cart.getSubscriptionType().equals("M")){
            holder.spWeakMonth.setSelection(0);
        }else {
            holder.spWeakMonth.setSelection(1);
        }
        holder.spWeakMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position_spinner, long id) {
                if (position_spinner == 0)// for monthly subscription
                {
                    int price=cart.getResult().get(0).getPrice();
                    double itemTotalPrice=price * 30 * cart.getQuantity();
                    holder.tvWeakMonthPrice.setText(String.valueOf(itemTotalPrice));
                    categoryItemList.get(position).setSubscriptionType("M");
                }

                if (position_spinner == 1)// for weekly subscription
                {
                    int price=cart.getResult().get(0).getPrice();
                    double itemTotalPrice=price * 7 * cart.getQuantity();
                    holder.tvWeakMonthPrice.setText(String.valueOf(itemTotalPrice));
                    categoryItemList.get(position).setSubscriptionType("W");
                }
                //calculate total price and 12% DST
                double basicPrice = 0;
                int size=categoryItemList.size();
                for (int i=0;i<size;i++){
                    basicPrice=basicPrice+Double.valueOf((String) holder.tvWeakMonthPrice.getText());
                }
                priceUpdaterListener.onUpdatePrice(basicPrice);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (cart.getQuantity() == 0) {
            holder.tvAddItem.setVisibility(View.VISIBLE);
            holder.llAddRemove.setVisibility(View.GONE);
        } else {
            holder.tvAddItem.setVisibility(View.GONE);
            holder.llAddRemove.setVisibility(View.VISIBLE);
        }
        holder.tvCount.setText(Html.fromHtml(cart.getQuantity() + ""));
        holder.tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCart.OnAddItemToCart(position, categoryItemList.get(position), cart.getQuantity(), Constant.CARD_PLUS);
            }
        });
        holder.tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.getQuantity() > 1) {
                    updateCart.OnAddItemToCart(position, categoryItemList.get(position), cart.getQuantity(), Constant.CARD_MINUS);
                } else {
                    updateCart.OnAddItemToCart(position, categoryItemList.get(position), cart.getQuantity(), Constant.CARD_DELETE);
                }
            }
        });
        holder.tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCart.OnAddItemToCart(position, categoryItemList.get(position), cart.getQuantity(), Constant.CARD_PLUS);
            }
        });





    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }
}