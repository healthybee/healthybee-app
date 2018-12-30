package com.app.healthybee.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.healthybee.R;
import com.app.healthybee.models.ModelDeliverySupport;

import java.util.ArrayList;

public class AdapterDeliverySupport extends RecyclerView.Adapter<AdapterDeliverySupport.ViewHolder> {
    private Context context;
    private ArrayList<ModelDeliverySupport> quesAnsList;
    private int Cposition = -1;

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
    public void onBindViewHolder(final AdapterDeliverySupport.ViewHolder holder, final int position) {
        ModelDeliverySupport model = quesAnsList.get(position);
        holder.textViewQues.setText(model.getQuestion());
        holder.textViewAns.setText(model.getAnswer());

        final boolean isExpanded = position == Cposition;
        if (isExpanded) {
            holder.textViewQues.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0);
        } else {
            holder.textViewQues.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);

        }
        holder.linearLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


        holder.textViewQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cposition = isExpanded ? -1 : position;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
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
