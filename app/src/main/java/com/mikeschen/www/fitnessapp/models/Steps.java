package com.mikeschen.www.fitnessapp.models;

public class Steps {


    long id;
    int stepsTaken;
    int date;

    public Steps(long id, int stepsTaken, int date) {
        this.id = id;
        this.stepsTaken = stepsTaken;
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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}

