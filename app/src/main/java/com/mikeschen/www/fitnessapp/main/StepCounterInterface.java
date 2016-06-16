package com.mikeschen.www.fitnessapp.main;

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
