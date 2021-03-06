package com.app.healthybee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.healthybee.RoundedCornersTransformation;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.R;
import com.app.healthybee.listeners.UpdateCartCategoryItem;
import com.app.healthybee.models.CategoryItem;
import com.app.healthybee.utils.Constant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import static com.app.healthybee.activities.MainActivity.mFlagDisplayList;

public class AdapterCategoryItem extends RecyclerView.Adapter<AdapterCategoryItem.MyViewHolder> {

    private Context mContext;
    private List<CategoryItem> categoryItemList;
    private CustomItemClickListener listener;
    private UpdateCartCategoryItem updateCartCategoryItem;

    public AdapterCategoryItem(Context mContext, List<CategoryItem> categoryItemList1, CustomItemClickListener listener1, UpdateCartCategoryItem updateCartCategoryItem) {
        this.mContext = mContext;
        this.categoryItemList = categoryItemList1;
        this.listener = listener1;
        this.updateCartCategoryItem = updateCartCategoryItem;

    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView tv_old_price;
        private TextView tv_new_price;
        private ImageView thumbnail;
        private TextView tvCount;
        private ImageView tvPlus, tvMinus;

        private LinearLayout llAddRemove;
        private TextView tvAddItem;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            tv_old_price = view.findViewById(R.id.tv_old_price);
            tv_new_price = view.findViewById(R.id.tv_new_price);
            thumbnail = view.findViewById(R.id.thumbnail);
            tvMinus = view.findViewById(R.id.tvMinus);
            tvCount = view.findViewById(R.id.tvCount);
            tvPlus = view.findViewById(R.id.tvPlus);
            llAddRemove = view.findViewById(R.id.llAddRemove);
            tvAddItem = view.findViewById(R.id.tvAddItem);
        }
    }


    /*public AdapterCategoryItem(Context mContext, List<CategoryItem> albumList, CustomItemClickListener listener1) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.listener = listener1;
    }*/

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int position) {
        View itemView;

        // final MyViewHolder mViewHolder = new MyViewHolder(itemView);
        if (mFlagDisplayList) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.album_card, parent, false);
        } else {

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.album_card_grid, parent, false);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position);
            }
        });
        return new MyViewHolder(itemView);
    }

    @SuppressLint({"SetTextI18n", "Range"})
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final CategoryItem categoryItem = categoryItemList.get(position);

        holder.title.setText(Html.fromHtml(categoryItem.getName()));
        holder.tv_old_price.setText(Html.fromHtml(mContext.getResources().getString(R.string.rs) + " " + categoryItem.getOld_price()));
        holder.tv_old_price.setPaintFlags(holder.tv_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_new_price.setText(Html.fromHtml(mContext.getResources().getString(R.string.rs) + " " + categoryItem.getPrice() + ".00/Meal"));


        Glide.with(mContext)
                .load(categoryItem.getImage_url().replace(" ", "%20"))
                .apply(RequestOptions
                        .bitmapTransform(new RoundedCornersTransformation(mContext, 10, 0))
                        .error(R.drawable.ic_no_item))
                .into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, position);
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
                updateCartCategoryItem.OnAddItemToCart(categoryItemList.get(position), categoryItem.getCount(), Constant.CARD_PLUS, position);
            }
        });
        holder.tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryItem.getCount() > 1) {
                    updateCartCategoryItem.OnAddItemToCart(categoryItemList.get(position), categoryItem.getCount(), Constant.CARD_MINUS, position);
                } else {
                    updateCartCategoryItem.OnAddItemToCart(categoryItemList.get(position), categoryItem.getCount(), Constant.CARD_DELETE, position);
                }
            }
        });
        holder.tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCartCategoryItem.OnAddItemToCart(categoryItem, categoryItem.getCount(), Constant.CARD_PLUS, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}