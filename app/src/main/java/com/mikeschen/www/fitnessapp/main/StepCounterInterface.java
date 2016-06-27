package com.mikeschen.www.fitnessapp.main;

import android.hardware.SensorEvent;

import com.mikeschen.www.fitnessapp.models.Calories;
import com.mikeschen.www.fitnessapp.models.Steps;

public interface StepCounterInterface {

    interface View {
        void showSteps(Steps steps);
        void showCalories(Calories calories);
        void buildNotification(int steps);
        long createNewDBRows(Steps stepRecord, Calories calorieRecord);
        void addToSharedPreferences(long time, int steps, long id);
    }

    interface Presenter {
        void loadSteps();
        void loadCalories();
        void calculateSteps(SensorEvent sensor);
        void checkDaysPassed(int lastKnownSteps, int lastKnownCalories, long lastKnownTime, long lastKnownId);
    }

}
