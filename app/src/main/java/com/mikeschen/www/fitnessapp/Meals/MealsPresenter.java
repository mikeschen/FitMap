package com.mikeschen.www.fitnessapp.Meals;

import android.content.Context;
import android.view.View;

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
        db.logCaloriesConsumed(calories);
    }
}

