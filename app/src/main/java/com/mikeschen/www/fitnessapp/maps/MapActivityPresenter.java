package com.mikeschen.www.fitnessapp.maps;

import com.mikeschen.www.fitnessapp.models.Route;
import com.mikeschen.www.fitnessapp.utils.DirectionFinder;
import com.mikeschen.www.fitnessapp.utils.DirectionFinderListener;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class MapActivityPresenter extends MapPresenter implements DirectionFinderListener {

    private MapInterface.View mMapView;

    public MapActivityPresenter(MapInterface.View mapView) {
        super(mapView);
        mMapView = mapView;
    }

    @Override
    public void makeRequest(String origin, String destination, boolean bikeSwitcher) {
        try {
            new DirectionFinder(this, origin, destination, bikeSwitcher).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {
        mMapView.clearMap();
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        mMapView.displayDirections(routes);
    }

    @Override
    public void onDirectionFinderFail() { mMapView.closeDialog(); }
}
