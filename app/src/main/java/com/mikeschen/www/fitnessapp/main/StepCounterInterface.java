package com.mikeschen.www.fitnessapp.main;

import android.hardware.SensorEvent;

import com.mikeschen.www.fitnessapp.models.Calories;
import com.mikeschen.www.fitnessapp.models.Days;
import com.mikeschen.www.fitnessapp.models.Steps;

public interface StepCounterInterface {

    interface View {
        void showSteps(Days days);
        long createNewDBRows(Days dayRecord);
        void addToSharedPreferences(long time, int steps, long id);
        Days endOfDaySave();
    }
}
