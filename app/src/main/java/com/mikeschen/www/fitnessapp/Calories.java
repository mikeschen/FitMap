package com.mikeschen.www.fitnessapp;

public class Calories {

    long id;
    int caloriesBurned;
    int date;

    public Calories() {
    }

    public Calories(long id, int caloriesBurned, int date) {
        this.id = id;
        this.caloriesBurned = caloriesBurned;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}

