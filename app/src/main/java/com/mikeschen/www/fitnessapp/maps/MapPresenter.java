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

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikeschen.www.fitnessapp.utils.PermissionUtils;

public class MapPresenter implements
        MapInterface.Presenter,
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    public final MapInterface.View mMapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public Context mContext;
    public SupportMapFragment mMapFragment;
    public GoogleMap mMap;
    private Location mLastLocation;
    private Marker marker;
    private UiSettings mUiSettings;

    public double myLocationLat;
    public double myLocationLong;

    public MapPresenter(MapInterface.View mapView, Context context, SupportMapFragment mapFragment) {
        mMapView = mapView;
        mContext = context;
        mMapFragment = mapFragment;
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomGesturesEnabled(false);
        mUiSettings.setRotateGesturesEnabled(false);

        mMap.setOnMyLocationButtonClickListener(this);

        enableMyLocation();

        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));
            myLocationLat = location.getLatitude();
            myLocationLong = location.getLongitude();
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(16)
                    .build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission((FragmentActivity) mContext, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
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
            enableMyLocation();
            mMapView.refresh();
        } else {
            mMapView.updatePermissionStatus(true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        if (marker != null) {
            marker.remove();
        }

        double dLatitude = mLastLocation.getLatitude();
        double dLongitude = mLastLocation.getLongitude();
        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(dLatitude, dLongitude))
                .title("My Location").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 8));
    }

    @Override
    public void loadMap() {
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void makeRequest(String origin, String destination) {

    }
}
