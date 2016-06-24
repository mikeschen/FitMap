package com.mikeschen.www.fitnessapp.utils;


import com.mikeschen.www.fitnessapp.models.Route;

import java.util.List;

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
