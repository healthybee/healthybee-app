package com.app.healthybee.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.healthybee.listeners.CustomItemClickListener;

import com.app.healthybee.listeners.UpdateCartCategoryItem;
import com.app.healthybee.adapter.AdapterItemsList;
import com.app.healthybee.R;
import com.app.healthybee.dboperation.DbHelper;
import com.app.healthybee.models.CategoryItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityItemDetails extends AppCompatActivity {
    private RecyclerView itemsList;
    private AdapterItemsList adapter;
    private NestedScrollView sv;
    private ArrayList<CategoryItem> data = new ArrayList<>();
    private CategoryItem itemDetails = new CategoryItem();
    private Activity activity;
    private ImageView my_image;
    private TextView tv_itemTitle;
    private TextView tvCalories;
    private TextView tvProtein;
    private TextView tvFat;
    private TextView tvCarbs;
    private ImageView IvBack;
    private DbHelper dbHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        sv = findViewById(R.id.sv);
        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.scrollTo(0, 0);
            }
        });
        activity=ActivityItemDetails.this;
        dbHelper=new DbHelper(activity);
        my_image= (ImageView) findViewById(R.id.my_image);
        tvCalories= (TextView) findViewById(R.id.tvCalories);
        tv_itemTitle= (TextView) findViewById(R.id.tv_itemTitle);
        tvProtein= (TextView) findViewById(R.id.tvProtein);
        tvFat= (TextView) findViewById(R.id.tvFat);
        tvCarbs= (TextView) findViewById(R.id.tvCarbs);
        IvBack= (ImageView) findViewById(R.id.IvBack);

        itemsList = (RecyclerView) findViewById(R.id.recycler_view);
        itemsList.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        itemsList.setLayoutManager(mLinearLayoutManager);
        //let us add some items into the list
        Intent intent = getIntent();
        if (null != intent) {
            if (intent.hasExtra("itemDetails")) {
                itemDetails = intent.getParcelableExtra("itemDetails");
            }
            if (intent.hasExtra("itemList")) {
                data.addAll(intent.<CategoryItem>getParcelableArrayListExtra("itemList"));
            }

        }

        adapter = new AdapterItemsList(ActivityItemDetails.this, data, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("TAG", "clicked position:" + position);
                String postId = data.get(position).getName();
                Toast.makeText(ActivityItemDetails.this, postId, Toast.LENGTH_SHORT).show();

                // do what ever you want to do with it
            }
        }, new UpdateCartCategoryItem() {
            @Override
            public void OnAddItemToCart(CategoryItem categoryItem, int i1, int card_plus_minus,int position) {
                Log.d("TAG", "add to cart" + categoryItem.getName());
                if (card_plus_minus==-1){
                    dbHelper.deleteCartRow(categoryItem.getName());
                }else {
                  //  dbHelper.insertUpdateCart(categoryItem);
                }
            }
        });

        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //  set item details
        tv_itemTitle.setText(Html.fromHtml(itemDetails.getAdd_on()));
        Glide.with(activity)
                .load(itemDetails.getImage_url().replace(" ", "%20"))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.load)
                        .fitCenter()
                        .error(R.drawable.ic_no_item))
                .into(my_image);
       /* Picasso.with(activity)
                .load(itemDetails.getImage_url().replace(" ", "%20"))
                .placeholder(R.drawable.ic_thumbnail)
                .into(my_image);*/
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


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
