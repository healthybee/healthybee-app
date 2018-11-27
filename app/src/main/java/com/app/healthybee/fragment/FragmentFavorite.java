package com.app.healthybee.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.healthybee.R;

public class FragmentFavorite extends Fragment {

    private View root_view, parent_view;
    private RecyclerView recyclerView;
    //private AdapterNews mAdapter;
    LinearLayout lyt_root;

    public FragmentFavorite() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_favorite, null);
//        parent_view = getActivity().findViewById(R.id.main_content);
//        lyt_root = root_view.findViewById(R.id.root_layout);
//
//        recyclerView = root_view.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setHasFixedSize(true);
//
//        if (Config.ENABLE_RTL_MODE) {
//            lyt_root.setRotationY(180);
//        }
//
//        //set data and list adapter
//        mAdapter = new AdapterNews(getActivity(), recyclerView, new ArrayList<News>());
//        recyclerView.setAdapter(mAdapter);
//
//        // on item list clicked
//        mAdapter.setOnItemClickListener(new AdapterNews.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, News obj, int position) {
//                ActivityNewsDetail.navigate((MainActivity) getActivity(), v.findViewById(R.id.image), obj);
//            }
//        });
        showNoItemView(true);
        return root_view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        assert ((AppCompatActivity)getActivity()) != null;
       // ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        assert ((AppCompatActivity)getActivity()) != null;
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

//    @Override
//    public void onResume() {
//        showNoItemView(true);
//        if(RealmController.with(this).getNewsSize() > 0){
//            displayData(RealmController.with(this).getNews());
//        } else {
//            showNoItemView(true);
//        }
//        super.onResume();
//    }

//    private void displayData(final List<News> posts) {
//        mAdapter.resetListData();
//        mAdapter.insertData(posts);
//        if (posts.size() == 0) {
//            showNoItemView(true);
//        }
//    }

    private void showNoItemView(boolean show) {
        View lyt_no_item = root_view.findViewById(R.id.lyt_no_item_later);
        ((TextView) root_view.findViewById(R.id.no_item_message)).setText(R.string.no_favorite_found);
        if (show) {
            recyclerView.setVisibility(View.GONE);
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lyt_no_item.setVisibility(View.GONE);
        }
    }
}
