package com.app.healthybee.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.app.healthybee.adapter.AdapterItemsList;
import com.app.healthybee.R;
import com.app.healthybee.activities.MainActivity;
import com.app.healthybee.dboperation.DbHelper;
import com.app.healthybee.listeners.CustomItemClickListener;
import com.app.healthybee.listeners.UpdateCart;
import com.app.healthybee.models.CategoryItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentItemDetails extends Fragment {

    private RecyclerView itemsList;
    private AdapterItemsList adapter;
    private NestedScrollView sv;
    private ArrayList<CategoryItem> data = new ArrayList<>();
    private CategoryItem itemDetails = new CategoryItem();
    private ImageView my_image;
    private TextView tv_itemTitle;
    private TextView tvCalories;
    private TextView tvProtein;
    private TextView tvFat;
    private TextView tvCarbs;
    private ImageView IvBack;
    private DbHelper dbHelper;
    public FragmentItemDetails() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_item_details, container, false);
        sv = view.findViewById(R.id.sv);
        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.scrollTo(0, 0);
            }
        });

        dbHelper=new DbHelper(getActivity());
        my_image= (ImageView) view.findViewById(R.id.my_image);
        tvCalories= (TextView) view.findViewById(R.id.tvCalories);
        tv_itemTitle= (TextView)view. findViewById(R.id.tv_itemTitle);
        tvProtein= (TextView) view.findViewById(R.id.tvProtein);
        tvFat= (TextView) view.findViewById(R.id.tvFat);
        tvCarbs= (TextView) view.findViewById(R.id.tvCarbs);
        IvBack= (ImageView) view.findViewById(R.id.IvBack);

        itemsList = (RecyclerView) view.findViewById(R.id.recycler_view);
        itemsList.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        itemsList.setLayoutManager(mLinearLayoutManager);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            itemDetails = bundle.getParcelable("itemDetails");
            data.addAll(bundle.<CategoryItem>getParcelableArrayList("itemList"));
        }

        adapter = new AdapterItemsList(getActivity(), data, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("TAG", "clicked position:" + position);
                String postId = data.get(position).getName();
                Toast.makeText(getActivity(), postId, Toast.LENGTH_SHORT).show();

                // do what ever you want to do with it
            }
        }, new UpdateCart() {
            @Override
            public void OnAddItemToCart(CategoryItem categoryItem, int i1, int card_plus_minus) {
                Log.d("TAG", "add to cart" + categoryItem.getName());
                if (card_plus_minus==-1){
                    dbHelper.deleteCartRow(categoryItem.getName());
                    ((MainActivity) getActivity()).setCountText();
                }else {
                    dbHelper.insertUpdateCart(categoryItem);
                    ((MainActivity) getActivity()).setCountText();
                }
            }
        });

        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).exitApp();

            }
        });
        //  set item details
        tv_itemTitle.setText(Html.fromHtml(itemDetails.getAdd_on()));
        Glide.with(getActivity())
                .load(itemDetails.getImage_url().replace(" ", "%20"))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.load)
                        .fitCenter()
                        .error(R.drawable.ic_no_item))
                .into(my_image);

        itemsList.setAdapter(adapter);
        String nutrition=itemDetails.getNutrition().replace(" ", "").trim();
        nutrition=nutrition.replace("|", ":").trim();
        String[] separated = nutrition.split(":");
        String Calories=separated[0];
        tvCalories.setText(Html.fromHtml(Calories));
        String Protein=separated[1];
        tvProtein.setText(Html.fromHtml(Protein));
        String Fat=separated[2];
        tvFat.setText(Html.fromHtml(Fat));
        String Carbs=separated[3];
        tvCarbs.setText(Html.fromHtml(Carbs));
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        assert ((AppCompatActivity)getActivity()) != null;
//       ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        assert ((AppCompatActivity)getActivity()) != null;
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

}
