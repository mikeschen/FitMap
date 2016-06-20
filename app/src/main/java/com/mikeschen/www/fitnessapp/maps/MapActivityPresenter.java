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
import com.mikeschen.www.fitnessapp.Modules.DirectionFinder;
import com.mikeschen.www.fitnessapp.Modules.DirectionFinderListener;
import com.mikeschen.www.fitnessapp.Modules.Route;
import com.mikeschen.www.fitnessapp.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MapActivityPresenter extends MapPresenter implements DirectionFinderListener {

    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private Context mContext;
    private MapInterface.View mMapView;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private int calorie;
    private double myLong;

    public MapActivityPresenter(MapInterface.View mapView, Context context, SupportMapFragment mapFragment) {
        super(mapView, context, mapFragment);
        mMapView = mapView;
        mContext = context;
        mMapFragment = mapFragment;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        super.onMapReady(map);
        mMap = map;
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mUiSettings.setZoomControlsEnabled(true);
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
        progressDialog = ProgressDialog.show(mContext, "Please wait...",
                "Finding Directions", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            mMapView.showDistance(route.distance.text);
            mMapView.showDuration(route.duration.text);
            calorie = ((int)(Math.round(route.distance.value/16.1)));
            mMapView.showCalorieRoute(calorie);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.startmarker))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.endmarker))
                    .title(route.endAddress)
                    .position(route.endLocation)));
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : originMarkers) {
                builder.include(marker.getPosition());
            }
            for (Marker marker : destinationMarkers) {
                builder.include(marker.getPosition());
            }

            LatLngBounds bounds = builder.build();

            int padding = 200; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            mMap.moveCamera(cu);

            PolylineOptions polylineOptions = new PolylineOptions()
                    .color(Color.rgb(66,133,244))
                    .width(20)
                    .geodesic(true);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}
