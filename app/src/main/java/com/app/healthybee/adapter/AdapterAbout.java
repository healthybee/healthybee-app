package com.app.healthybee.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.healthybee.R;
import com.app.healthybee.activities.Applications;
import com.app.healthybee.fragment.FragmentProfile;

import java.util.List;

public class AdapterAbout extends RecyclerView.Adapter<AdapterAbout.UserViewHolder> {

    private List<FragmentProfile.Data> dataList;
    private Context context;
    Applications myApplication;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, FragmentProfile.Data obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterAbout(List<FragmentProfile.Data> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lsv_item_about, null);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        myApplication = Applications.getInstance();
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, final int position) {

        final FragmentProfile.Data data = dataList.get(position);

        holder.image.setImageResource(data.getImage());
        holder.title.setText(data.getTitle());
        holder.sub_title.setText(data.getSub_title());

        if (position == 0||position == 1) {
            holder.relativeLayout.setVisibility(View.GONE);
        }
        if (position == 2) {
            holder.relativeLayout.setVisibility(View.GONE);
            holder.sub_title.setTextColor(ContextCompat.getColor(context, R.color.colorOrange));
        }

        if (position == 3 || position == 4) {
            holder.sub_title.setVisibility(View.GONE);
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.color_dark));


//            if (myApplication.getIsLogin()) {
//                holder.relativeLayout.setVisibility(View.VISIBLE);
//            } else {
//                holder.relativeLayout.setVisibility(View.GONE);
//            }

        }

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, data, position);
                }
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, data, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView sub_title;
        RelativeLayout relativeLayout,lyt_parent;

        public UserViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            sub_title = itemView.findViewById(R.id.sub_title);
            relativeLayout = itemView.findViewById(R.id.relativeLayout_image);
            lyt_parent = itemView.findViewById(R.id.lyt_parent);

        }

    }

}