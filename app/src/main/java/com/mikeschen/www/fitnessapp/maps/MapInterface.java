package com.mikeschen.www.fitnessapp.maps;


import com.mikeschen.www.fitnessapp.models.Route;

import java.util.List;

public interface MapInterface {

    interface View {
        void updatePermissionStatus(boolean permissionStatus);
        void showDistance(String distance);
        void showDuration(int duration);
        void showCalorieRoute(Long calorie);
        void refresh();
        void enableMyLocation();
        void displayDirections(List<Route> routes);
        void clearMap();
        void closeDialog();
    }

    interface Presenter {
        void loadMap();
        void makeRequest(String origin, String destination, boolean bikeSwitcher);
    }
}
