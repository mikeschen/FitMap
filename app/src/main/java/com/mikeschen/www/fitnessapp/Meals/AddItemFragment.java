package com.mikeschen.www.fitnessapp.Meals;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mikeschen.www.fitnessapp.R;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddItemFragment extends Fragment {
    private static Context mContext;
    @Bind(R.id.foodItemAddRecyclerView) RecyclerView mFoodItemAddRecyclerView;
    @Bind(R.id.foodInputEditText) EditText mFoodInputEditText;
    @Bind(R.id.calorieInputEditText) EditText mCalorieInputEditText;



    public AddItemFragment() {}


    public static AddItemFragment newInstance(Context context, String foodItem, String calories) {
        AddItemFragment addItemFragment = new AddItemFragment();
        Bundle args = new Bundle();
        mContext = context;
        args.putParcelable("foodItem", Parcels.wrap(foodItem));
        args.putParcelable("calories", Parcels.wrap(calories));
        addItemFragment.setArguments(args);
        return addItemFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item,container, false);
        ButterKnife.bind(mContext, view);
        return view;
    }
}


