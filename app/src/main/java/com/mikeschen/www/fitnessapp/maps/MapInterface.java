package com.mikeschen.www.fitnessapp.maps;


public interface MapInterface {

    interface View {
        void updatePermissionStatus(boolean permissionStatus);
        void showDistance(String distance);
        void showDuration(String duration);
        void showCalorieRoute(Long calorie);
        void refresh();
        void enableMyLocation();
    }

    interface Presenter {
        void loadMap();
        void makeRequest(String origin, String destination);
    }
}
