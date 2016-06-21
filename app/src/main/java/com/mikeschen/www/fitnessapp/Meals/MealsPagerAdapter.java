package com.mikeschen.www.fitnessapp.Meals;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Ramon on 6/21/16.
 */
public class MealsPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Food> mFood;
    private Context mContext;

    public MealsPagerAdapter(Context context, FragmentManager fm, ArrayList<Food> foods) {
        super(fm);
        mFood = foods;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return GroceryDetailFragment.newInstance(mContext, mFood.get(position));
    }

    @Override
    public int getCount() {
        return mFood.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFood.get(position).getFoodName();
    }
}
