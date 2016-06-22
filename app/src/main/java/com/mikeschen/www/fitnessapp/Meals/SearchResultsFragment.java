package com.mikeschen.www.fitnessapp.Meals;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikeschen.www.fitnessapp.R;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SearchResultsFragment extends Fragment  {
    @Bind(R.id.foodItemRecyclerView) RecyclerView mFoodItemRecyclerView;
    private static Context mContext;



    public SearchResultsFragment() {
        // Required empty public constructor
    }

    public static SearchResultsFragment newInstance(Context context, String foodItem) {
        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        mContext = context;
        args.putParcelable("foodItem", Parcels.wrap(foodItem));
        searchResultsFragment.setArguments(args);

        return searchResultsFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_search_results, container, false);
        ButterKnife.bind(mContext, view);
        return view;
    }

}
