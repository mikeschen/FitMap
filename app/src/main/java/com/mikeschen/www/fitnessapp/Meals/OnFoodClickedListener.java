package com.mikeschen.www.fitnessapp.Meals;

import com.mikeschen.www.fitnessapp.models.Food;

import java.util.ArrayList;

/**
 * Created by Ramon on 6/29/16.
 */
public interface OnFoodClickedListener {

    void onFoodClicked(int position, ArrayList<Food> mFoods);
}
