package com.mikeschen.www.fitnessapp;

/**
 * Created by Ramon on 6/15/16.
 */
public class Calories {

    int id;
    int caloriesBurned;
    int date;

    public Calories() {
    }

    public Calories(int id, int caloriesBurned, int date) {
        this.id = id;
        this.caloriesBurned = caloriesBurned;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public int getint() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}

