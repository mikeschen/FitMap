//package com.mikeschen.www.fitnessapp.Meals;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.util.Log;
//
//import com.mikeschen.www.fitnessapp.models.Calories;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.Response;
//
//
//public class MealsPresenter implements
//        MealsInterface.Presenter {
//
//    private MealsInterface.View mMealsView;
//    private int consumedCalories;
//    private int totalCalories;
//    private String mSearchType;
//    private ProgressDialog mAuthProgressDialog;
//    public ArrayList<Food> mFoods = new ArrayList<>();
//
//
//
//
//    public MealsPresenter(MealsInterface.View mealsView) {
//
//        mMealsView = mealsView;
//        consumedCalories = 0;
//        totalCalories = 0;
//    }
//
//    @Override
//    public void computeCalories(Integer calories, Calories calorieRecord) {
//
//    }
//
//    @Override
//    public void loadFoodItem() {
//
//    }
//
//    @Override
//    public void computeCalories(Integer calories, Calories calorieRecord) {
//
//        calorieRecord.setCalories(calorieRecord.getCalories() + calories);
//
////        Log.d("saveCalories", caloriesConsumed.getCalories() + "");
//        mMealsView.showCalories(calorieRecord);
//    }
//
//    @Override
//    public void loadCalories(Calories calories) {
//        mMealsView.showCalories(calories);
//
//    }
//
//    @Override
//    public void searchFoods(String foodItem) {
//        final NutritionixService nutritionixService = new NutritionixService();
//        nutritionixService.searchFoods(foodItem, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                mFoods = nutritionixService.processResults(response);
//                Log.d("search return", mFoods + "");
//                mMealsView.displayFoodByItem(mFoods);
//            }
//        });
//    }
//
//
//    @Override
//    public void searchUPC(String scanResult) {
//        final NutritionixService nutritionixService = new NutritionixService();
//        nutritionixService.searchUPC(scanResult, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                mFoods = nutritionixService.processResultsUpc(response);
//                mMealsView.displayFoodByUPC(mFoods);
//
//            }
//
//        });
//    }
//
//    @Override
//    public void scanUpc() {
//    }
//
//}
//
//


package com.mikeschen.www.fitnessapp.Meals;

import android.util.Log;

import com.mikeschen.www.fitnessapp.models.Food;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MealsPresenter implements MealsInterface.Presenter {

    private MealsInterface.View mMealsView;
    public ArrayList<Food> mFoods = new ArrayList<>();

    public MealsPresenter(MealsInterface.View mealsView) {
        mMealsView = mealsView;
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
