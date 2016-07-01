package com.mikeschen.www.fitnessapp.Meals;

import com.mikeschen.www.fitnessapp.models.Food;

import java.util.ArrayList;

public interface MealsInterface {

    interface View {
        void displayFoodByUPC(ArrayList<Food> foods);
        void displayFoodByItem(ArrayList<Food> foods);
    }

    interface Presenter {
        void searchFoods(String foodItem);
        void searchUPC(String scanResult);
    }
}
