package com.mikeschen.www.fitnessapp;

/**
 * Created by Ramon on 6/14/16.
 */
public interface StepCounterInterface {

    interface View {
        void showSteps(int stepCount);
        void showCalories(int caloriesBurned);
    }

    interface Presenter {
        void loadSteps();
        void loadCalories();
    }

}
