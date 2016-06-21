package com.mikeschen.www.fitnessapp.Meals;

/**
 * Created by alexnenchev on 6/21/16.
 */
public interface MealsInterface {

    interface View {
        void showFoodItem(String foodItem);
        void saveFoodItem(String foodItem);
        void showCalories(int calorie);
        void refresh();
    }

    interface Presenter {
        void loadFoodItem();
        void searchFoods(String foodItem);
        void searchUPC(String upc);
        //do we need Callback callback????
        //how to set the processUpcResults???
    }
}
