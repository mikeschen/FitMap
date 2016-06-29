package com.mikeschen.www.fitnessapp.main;

import android.hardware.SensorEvent;

import com.mikeschen.www.fitnessapp.models.Calories;
import com.mikeschen.www.fitnessapp.models.Days;
import com.mikeschen.www.fitnessapp.models.Steps;

public interface StepCounterInterface {

    interface View {
        void showSteps(Days days);
//        void showCalories(Days days);
        void buildNotification(int steps);
        long createNewDBRows(Days dayRecord);
        void addToSharedPreferences(long time, int steps, long id);
        Days endOfDaySave();
    }

    interface Presenter {
        void loadSteps();
//        void loadCalories();
        void calculateSteps(SensorEvent sensor);
        void checkDaysPassed(int lastKnownSteps, int lastKnownCalories, long lastKnownTime, long lastKnownId);
    }

}
