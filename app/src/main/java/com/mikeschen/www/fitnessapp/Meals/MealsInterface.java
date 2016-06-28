package com.mikeschen.www.fitnessapp.Meals;

import com.mikeschen.www.fitnessapp.models.Calories;
import com.mikeschen.www.fitnessapp.models.Days;
import com.mikeschen.www.fitnessapp.models.Food;

import java.util.ArrayList;

public interface MealsInterface {

    interface View {
        void showFoodItem(String foodItem);
        void saveFoodItem(String foodItem);
        void showDays(Days days);
//        void showCalories(Calories calorieRecord);
        void refresh();
        void displayFoodByUPC(ArrayList<Food> foods);
        void displayFoodByItem(ArrayList<Food> foods);
    }

    interface Presenter {
        void computeCalories(Integer calories, Calories calorieRecord);
        void loadCalories(Calories calories);
        void loadFoodItem();
        void searchFoods(String foodItem);
        void searchUPC(String scanResult);
        void scanUpc();
    }
}
