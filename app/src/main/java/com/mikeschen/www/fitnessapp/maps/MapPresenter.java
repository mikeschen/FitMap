package com.mikeschen.www.fitnessapp.maps;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikeschen.www.fitnessapp.utils.PermissionUtils;

import java.util.List;

public class MapPresenter implements
        MapInterface.Presenter,
        GoogleMap.OnMyLocationButtonClickListener,
        LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    public final MapInterface.View mMapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public MapPresenter(MapInterface.View mapView) {
        mMapView = mapView;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            mMapView.enableMyLocation();
            mMapView.refresh();
        } else {
            mMapView.updatePermissionStatus(true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void loadMap() {
    }

    @Override
    public void makeRequest(String origin, String destination) {

    }
}
