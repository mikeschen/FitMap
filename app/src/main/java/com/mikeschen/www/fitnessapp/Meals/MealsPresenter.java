package com.mikeschen.www.fitnessapp.Meals;

import android.content.Context;
import android.view.View;

import com.mikeschen.www.fitnessapp.Calories;
import com.mikeschen.www.fitnessapp.DatabaseHelper;


public class MealsPresenter implements
        MealsInterface.Presenter,
        View.OnClickListener {

    private MealsInterface.View mMealsView;
    private Context mContext;
    private int consumedCalories;
    private int totalCalories;
    DatabaseHelper db;

    public MealsPresenter(MealsInterface.View mealsView, Context context) {

        mMealsView = mealsView;
        mContext = context;
        db = new DatabaseHelper(mContext.getApplicationContext());
        consumedCalories = 0;
        totalCalories = 0;

    }

    @Override
    public void saveCalories(Integer calories) {
        Calories caloriesConsumed;
        caloriesConsumed = new Calories(1, calories, 345);
        db.logCaloriesConsumed(caloriesConsumed);
        db.close();
    }

    @Override
    public void loadFoodItem() {

    }

    @Override
    public void searchFoods(String foodItem) {

    }

    @Override
    public void searchUPC(String upc) {

    }

    @Override
    public void onClick(View view) {

    }
}

