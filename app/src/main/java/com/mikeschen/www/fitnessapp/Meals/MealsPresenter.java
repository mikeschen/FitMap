package com.mikeschen.www.fitnessapp.Meals;

import android.app.ProgressDialog;
import android.content.Context;

import com.mikeschen.www.fitnessapp.models.Calories;
import com.mikeschen.www.fitnessapp.utils.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MealsPresenter implements
        MealsInterface.Presenter {

    private MealsInterface.View mMealsView;
    private Context mContext;
    private int consumedCalories;
    private int totalCalories;
    private String mSearchType;
    private ProgressDialog mAuthProgressDialog;
    public ArrayList<Food> mFoods = new ArrayList<>();

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

//    @Override
//    public void loadCalories(long calories_id) {
//        Calories caloriesConsumed;
//        caloriesConsumed = new Calories(calories_id, calories.getCalories(), 345);
//        db.getCaloriesConsumed()
//    }

    @Override
    public void loadFoodItem() {

    }

    @Override
    public void searchFoods(String foodItem) {
        final NutritionixService nutritionixService = new NutritionixService();
        nutritionixService.searchFoods(foodItem, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mFoods = nutritionixService.processResults(response);
                mMealsView.displayFoodByItem(mFoods);
            }
        });
    }


    @Override
    public void searchUPC(String upc) {
        final NutritionixService nutritionixService = new NutritionixService();
        nutritionixService.searchUPC(upc, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mFoods = nutritionixService.processResultsUpc(response);
                mMealsView.displayFoodByUPC(mFoods);

            }

        });
    }

}


//create a presenter for searchUPC and take all code from BaseActivity
//MealsTracker activity that never changes and then implement fragment that handles the search data
//dialog with fields and btn save and closes
//list fragment and meals fragment; savedmeals fragment
//last green activity will be separate activity
//how to display the calories on the screen as well