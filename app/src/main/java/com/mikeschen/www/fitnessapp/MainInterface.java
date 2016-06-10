package com.mikeschen.www.fitnessapp;

/**
 * Created by Ramon on 6/9/16.
 */
public interface MainInterface {

    interface View {
        void showTip(String tip);
    }

    interface Presenter {
        void loadTip();
    }
}
