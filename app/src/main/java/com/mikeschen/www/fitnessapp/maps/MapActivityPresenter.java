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

//    private GoogleMap mMap;
    private UiSettings mUiSettings;
//    private Context mContext;
    private MapInterface.View mMapView;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
//    private ProgressDialog progressDialog;
    private Long calorie;
    private int counter = 0;
    private ArrayList<String> distances;
    private ArrayList<String> durations;
    private ArrayList<Long> routeCalories;

    public MapActivityPresenter(MapInterface.View mapView) {
        super(mapView);
        mMapView = mapView;
        distances = new ArrayList<>();
        durations = new ArrayList<>();
        routeCalories = new ArrayList<>();
    }

//    @Override
//    public void onMapReady(GoogleMap map) {
//        super.onMapReady(map);
//        mMap = map;
//        mUiSettings = mMap.getUiSettings();
//        mUiSettings.setZoomGesturesEnabled(true);
//        mUiSettings.setRotateGesturesEnabled(true);
//        mMap.setOnMyLocationButtonClickListener(this);
//        mUiSettings.setZoomControlsEnabled(true);
//    }

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
//        progressDialog = ProgressDialog.show(mContext, "Please wait...",
//                "Finding Directions", true);

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

        distances.clear();
        durations.clear();
        routeCalories.clear();
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        counter = 0;
//        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            double miles = Math.round((route.distance.value * 0.000621371) * 10d) / 10d;
            Long minutes = Math.round(route.duration.value / 60.0);
            durations.add(minutes + " minutes");
            distances.add(miles + " miles");

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            mMapView.showDistance(miles + " miles");
            mMapView.showDuration(minutes + " minutes");
            calorie = Math.round(route.distance.value / 16.1);
            mMapView.showCalorieRoute(calorie);
            routeCalories.add(calorie);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(route.startAddress)
                    .position(route.startLocation)));
//            if (counter > 0) {
//                destinationMarkers.add(mMap.addMarker(new MarkerOptions()
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.invisible))
//                        .title(route.endAddress)
//                        .position(route.endLocation)));
//            } else {
//            Log.d("endLat", route.endLocation.latitude+"");
                destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title(route.endAddress)
                        .position(route.endLocation)));
//            }
//            Log.d("destination", destinationMarkers + "");
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
                    .color(Color.rgb(66, 133, 244))
                    .width(20)
                    .geodesic(true);

            for (int i = 0; i < route.points.size(); i++) {
                polylineOptions.add(route.points.get(i));
            }
            polylinePaths.add(mMap.addPolyline(polylineOptions));
            counter++;
        }
        for (Polyline polyline : polylinePaths) {
            polyline.setClickable(true);
        }
        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick (Polyline clickedPolyline) {
//                Log.d("PLYlineGETId!", clickedPolyline.getId() + "");
                for (int i = 0; i < polylinePaths.size(); i++) {
                    if (polylinePaths.get(i).getId().equals(clickedPolyline.getId())) {
                        clickedPolyline.setColor(Color.rgb(78, 160, 257));
                        mMapView.showDistance(distances.get(i));
                        mMapView.showDuration(durations.get(i));
                        mMapView.showCalorieRoute(routeCalories.get(i));
                    } else {
                        polylinePaths.get(i).setColor(Color.rgb(66, 133, 244));
                    }
                }
            }
        });
    }
}
