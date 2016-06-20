package com.mikeschen.www.fitnessapp.maps;


public interface MapInterface {

    interface View {
        void updatePermissionStatus(boolean permissionStatus);
        void showDistance(String distance);
        void showDuration(String duration);
        void showCalorieRoute(int calorie);
        void refresh();
    }

    interface Presenter {
        void loadMap();
        void makeRequest(String origin, String destination);
    }
}
