package com.mikeschen.www.fitnessapp.models;

/**
 * Created by Ramon on 6/28/16.
 */
public class Days {

    long id;
    int stepsTaken;
    int caloriesBurned;
    int caloriesConsumed;
    String date;

    public Days (long id, Integer stepsTaken, int caloriesBurned, int caloriesConsumed, String date) {
        this.id = id;
        this.stepsTaken = stepsTaken;
        this.caloriesBurned = caloriesBurned;
        this.caloriesConsumed = caloriesConsumed;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStepsTaken() {
        return stepsTaken;
    }

    public void setStepsTaken(int stepsTaken) {
        this.stepsTaken = stepsTaken;
    }


    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public int getCaloriesConsumed() {
        return caloriesConsumed;
    }

    public void setCaloriesConsumed(int caloriesConsumed) {
        this.caloriesConsumed = caloriesConsumed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
