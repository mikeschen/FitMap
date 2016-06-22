package com.mikeschen.www.fitnessapp.Meals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.mikeschen.www.fitnessapp.Calories;
import com.mikeschen.www.fitnessapp.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MealsPresenter implements
        MealsInterface.Presenter,
        View.OnClickListener {

    private MealsInterface.View mMealsView;
    private Context mContext;
    private int consumedCalories;
    private int totalCalories;
    DatabaseHelper db;
    private String mSearchString;
    private String mSearchType;
    private ProgressDialog mAuthProgressDialog;

    public ArrayList<Food> mFoods = new ArrayList<>();


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
        final NutritionixService nutritionixService = new NutritionixService();
        nutritionixService.searchUPC(mSearchString, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mFoods = nutritionixService.processResultsUpc(response);
                mMealsView.displayFood(mFoods);

//                MealsActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mFoods == null) {
//                            mAuthProgressDialog.dismiss();
//                            Toast.makeText(mContext, "Food Item Not Found", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(mContext, MealsSearchResultActivity.class);
//                            mContext.startActivity(intent);
//                        } else {
//                            mAuthProgressDialog.dismiss();
//                        }
//                    }
//                });

            }

        });
    }

    @Override
    public void onClick(View view) {

    }
}