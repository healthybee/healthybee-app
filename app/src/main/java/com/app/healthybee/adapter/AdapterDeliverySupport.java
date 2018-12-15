package com.app.healthybee.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.healthybee.R;
import com.app.healthybee.models.ModelDeliverySupport;

import java.util.ArrayList;

public class AdapterDeliverySupport extends RecyclerView.Adapter<AdapterDeliverySupport.ViewHolder> {
    Context context;
    ArrayList<ModelDeliverySupport> quesAnsList;
    private static int currentPosition = 0;

    public AdapterDeliverySupport(Context context, ArrayList<ModelDeliverySupport> quesAnsList) {
        this.context = context;
        this.quesAnsList = quesAnsList;
    }

    @NonNull
    @Override
    public AdapterDeliverySupport.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery_support, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterDeliverySupport.ViewHolder holder, final int position) {
        ModelDeliverySupport model = quesAnsList.get(position);
        holder.textViewQues.setText(model.getQuestion());
        holder.textViewAns.setText(model.getAnswer());

//        holder.linearLayout.setVisibility(View.GONE);

        //if the position is equals to the item position which is to be expanded
//        if (currentPosition == position) {
//            //creating an animation
//            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down_anim);
//
//            //toggling visibility
//            holder.linearLayout.setVisibility(View.VISIBLE);
//
//            //adding sliding effect
//            holder.linearLayout.startAnimation(slideDown);
//        }
        holder.textViewQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;
                if (holder.linearLayout.isShown()){
                    holder.textViewQues.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_down,0);
                    holder.linearLayout.setVisibility(View.GONE);
                }else {
                    holder.textViewQues.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.backarrow_black,0);
                    holder.linearLayout.setVisibility(View.VISIBLE);
                }

                //reloding the list
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        System.out.println("list size is \t"+quesAnsList.size());
        return quesAnsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQues, textViewAns;
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewQues = (TextView) itemView.findViewById(R.id.textViewQues);
            textViewAns = (TextView) itemView.findViewById(R.id.textViewAns);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}
