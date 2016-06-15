package com.mikeschen.www.fitnessapp;

/**
 * Created by Ramon on 6/10/16.
 */
public interface MapInterface {

    interface View {
        void updatePermissionStatus(boolean permissionStatus);
        void showMap();
        void showDistance(String distance);
    }

    interface Presenter {
        void loadMap();
        void makeRequest(String origin, String destination);
    }
}
