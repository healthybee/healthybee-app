package com.app.healthybee.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.healthybee.activities.ActivitySearch;
import com.app.healthybee.testcode.FragmentRecent;
import com.app.healthybee.utils.Constant;
import com.app.healthybee.utils.MyCustomProgressDialog;
import com.app.healthybee.utils.NetworkConstants;
import com.app.healthybee.R;
import com.app.healthybee.utils.SharedPrefUtil;
import com.app.healthybee.utils.UrlConstants;
import com.app.healthybee.activities.Applications;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FragmentHome extends Fragment {

    private View rootView;
    private ImageView ivSearch;
    private TabLayout tabLayout;
    @SuppressLint("StaticFieldLeak")
    private static ViewPager viewPager;
    private SearchView searchView = null;
    private static ViewPagerAdapter adapter;
    private ArrayList<String> categoryList;
    private static int currentPosition = 0;
    private Toolbar toolbar;

    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, null);
        toolbar = rootView.findViewById(R.id.toolbarHome);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        categoryList = new ArrayList<>();
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

//        imageViewGrid = (ImageView) rootView.findViewById(R.id.imageViewGrid);
//        imageViewList = (ImageView) rootView.findViewById(R.id.imageViewList);
//        if (MainActivity.mFlagDisplayList) {
//            imageViewGrid.setImageResource(R.drawable.ic_gridview_disable);
//            imageViewList.setImageResource(R.drawable.ic_listview_enable);
//        } else {
//            imageViewList.setImageResource(R.drawable.ic_listview_disable);
//            imageViewGrid.setImageResource(R.drawable.ic_gridview_enable);
//        }
//        imageViewList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.mFlagDisplayList = true;
//                imageViewGrid.setImageResource(R.drawable.ic_gridview_disable);
//                imageViewList.setImageResource(R.drawable.ic_listview_enable);
//                refreshFragment();
//            }
//        });
//        imageViewGrid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.mFlagDisplayList = false;
//                imageViewList.setImageResource(R.drawable.ic_listview_disable);
//                imageViewGrid.setImageResource(R.drawable.ic_gridview_enable);
//                refreshFragment();
//
//            }
//        });
        tabLayout.setupWithViewPager(viewPager);
        // Attach the page change listener inside the activity
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
//                Toast.makeText(getActivity(),
//                        "Selected page position: " + position,
//                        Toast.LENGTH_SHORT).show();
                if (searchView != null && !searchView.isIconified()) {
                    //searchView.onActionViewExpanded();
                    searchView.setIconified(true);

                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                currentPosition = position;
                // Code goes here
//                Toast.makeText(getApplicationContext(),
//                        "onPageScrolled",
//                        Toast.LENGTH_SHORT).show();
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
//                Toast.makeText(getApplicationContext(),
//                        "onPageScrollStateChanged",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        ivSearch = rootView.findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivitySearch.class);
                startActivity(intent);
            }
        });
        getCategory();
        adapter = new ViewPagerAdapter(getFragmentManager());
        return rootView;
    }

    public static void refreshFragment() {
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPosition);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getFragmentManager());
        new UploadCategoryFragment().execute();
    }


    @SuppressLint("StaticFieldLeak")
    class UploadCategoryFragment extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  MyCustomProgressDialog.showDialog(getActivity(),
            //          getString(R.string.please_wait));
        }

        @Override
        protected String doInBackground(String... strings) {
            int size = categoryList.size();

            for (int i = 0; i < size; i++) {
                FragmentCategoryList fragmentCategoryList = new FragmentCategoryList();
                Bundle bundle = new Bundle();
                bundle.putString("category", categoryList.get(i));
                fragmentCategoryList.setArguments(bundle);
                adapter.addFrag(fragmentCategoryList, categoryList.get(i));

//                FragmentRecent fragmentRecent = new FragmentRecent();
//                Bundle bundle = new Bundle();
//                bundle.putString("category", categoryList.get(i));
//                fragmentRecent.setArguments(bundle);
//                adapter.addFrag(fragmentRecent, categoryList.get(i));

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            viewPager.setAdapter(adapter);
            //MyCustomProgressDialog.dismissDialog();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            // Causes adapter to reload all Fragments when
            // notifyDataSetChanged is called
            notifyDataSetChanged();
            return POSITION_NONE;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshFragment();
            }
        }, Constant.CARD_UPDATE_TIME_RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getCategory() {
        try {
            if (NetworkConstants.isConnectingToInternet(Objects.requireNonNull(getActivity()))) {
                MyCustomProgressDialog.showDialog(getActivity(), getString(R.string.please_wait));
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                        UrlConstants.getCategory,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                MyCustomProgressDialog.dismissDialog();
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        categoryList.add((String) response.get(i));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                setupViewPager();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                MyCustomProgressDialog.dismissDialog();
                            }
                        }
                ){
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type","application/json");
                        headers.put("Authorization", "Bearer " + SharedPrefUtil.getToken(getActivity()));
                        return headers;
                    }
                };
                Applications.getInstance().addToRequestQueue(jsonArrayRequest);
            } else {
                MyCustomProgressDialog.showAlertDialogMessage(getActivity(), getString(R.string.network_title), getString(R.string.network_message));
            }
        } catch (Exception ignored) {

        }
    }
}

