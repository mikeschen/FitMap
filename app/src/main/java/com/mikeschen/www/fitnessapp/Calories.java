package com.mikeschen.www.fitnessapp;

public class Calories {

    long id;
    Integer calories;
    int date;

    public Calories() {
    }

    public Calories(long id, Integer calories, int date) {
        this.id = id;
        this.calories = calories;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}

