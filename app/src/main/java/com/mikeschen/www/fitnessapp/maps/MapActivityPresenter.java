package com.mikeschen.www.fitnessapp.maps;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mikeschen.www.fitnessapp.utils.DirectionFinder;
import com.mikeschen.www.fitnessapp.utils.DirectionFinderListener;
import com.mikeschen.www.fitnessapp.models.Route;
import com.mikeschen.www.fitnessapp.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MapActivityPresenter extends MapPresenter implements DirectionFinderListener {

    private UiSettings mUiSettings;
    private MapInterface.View mMapView;

    public MapActivityPresenter(MapInterface.View mapView) {
        super(mapView);
        mMapView = mapView;
    }

    @Override
    public void makeRequest(String origin, String destination) {
        try {
            new DirectionFinder(this, origin, destination).execute();
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
}
