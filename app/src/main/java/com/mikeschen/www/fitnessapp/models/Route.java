package com.mikeschen.www.fitnessapp.models;


import com.google.android.gms.maps.model.LatLng;
import com.mikeschen.www.fitnessapp.models.Distance;
import com.mikeschen.www.fitnessapp.models.Duration;

import java.util.List;

public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
