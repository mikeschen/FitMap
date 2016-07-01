package com.mikeschen.www.fitnessapp.main;

public interface TipInterface {

    interface View {
        void showTip(String tip);
    }

    interface Presenter {
        void loadTip(String json);
    }
}
