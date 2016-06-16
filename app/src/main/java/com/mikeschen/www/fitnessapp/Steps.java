package com.mikeschen.www.fitnessapp;

/**
 * Created by Ramon on 6/15/16.
 */
public class Steps {


    int id;
    int stepsTaken;
    int date;

    public Steps() {
    }

    public Steps(int id, int stepsTaken, int date) {
        this.id = id;
        this.stepsTaken = stepsTaken;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStepsTaken() {
        return stepsTaken;
    }

    public void setStepsTaken(int stepsTaken) {
        this.stepsTaken = stepsTaken;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}

