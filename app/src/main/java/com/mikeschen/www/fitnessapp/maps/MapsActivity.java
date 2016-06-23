package com.mikeschen.www.fitnessapp.maps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.utils.PermissionUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapsActivity extends BaseActivity implements
        MapInterface.View,
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener {

    private boolean mPermissionDenied = false;
    private Context mContext;
    private MapActivityPresenter mMapActivityPresenter;
    @Bind(R.id.atOrigin) EditText atOrigin;
    @Bind(R.id.atDestination) EditText atDestination;
    @Bind(R.id.btnFindPath) Button btnFindPath;
    @Bind(R.id.tvDistance) TextView mTvDistance;
    @Bind(R.id.tvDuration) TextView mTvDuration;
    @Bind(R.id.tvCalorie) TextView mTvCalorie;
    private String destination;
    private ProgressDialog progressDialog;


    public GoogleMap mMap;
    private UiSettings mUiSettings;
    GPSTracker gps;

    public double myLocationLat;
    public double myLocationLong;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;



    private void setHideSoftKeyboard(EditText editText){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        atOrigin.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        atDestination.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mContext = this;
        mMapActivityPresenter = new MapActivityPresenter(this);
        mapFragment.getMapAsync(this);
        destination = getIntent().getStringExtra("destination");
        atDestination.setText(destination, TextView.BufferType.EDITABLE);
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
                setHideSoftKeyboard(atDestination);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setZoomControlsEnabled(true);

        mMap.setOnMyLocationButtonClickListener(this);

        enableMyLocation();

        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gps = new GPSTracker(mContext);
        if(gps.canGetLocation()) {
            Log.d("Current Lat", gps.getLatitude() + "");
            Log.d("Current Long", gps.getLongitude() + "");

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(gps.getLatitude(), gps.getLongitude()), 13));
            myLocationLat = gps.getLatitude();
            myLocationLong = gps.getLongitude();
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(gps.getLatitude(), gps.getLongitude()))
                    .zoom(16)
                    .build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
            gps.showSettingsAlert();
        }
    }

    @Override
    public void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission((FragmentActivity) mContext, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }



    private void sendRequest() {
        String origin = atOrigin.getText().toString();
        String destination = atDestination.getText().toString();
        gps = new GPSTracker(mContext);
        if (origin.isEmpty()) {
            origin = gps.getLatitude() + "," + gps.getLongitude();
        }
        if (destination.isEmpty()) {
            Toast.makeText(mContext, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = ProgressDialog.show(mContext, "Please wait...",
                "Finding Directions", true);
        mMapActivityPresenter.makeRequest(origin, destination);
    }

    @Override
    public void showDistance(String distance) {
        progressDialog.dismiss();
        mTvDistance.setText(distance);
    }

    @Override
    public void showDuration(String duration) {
        mTvDuration.setText(duration);
    }

    @Override
    public void showCalorieRoute(Long calorie) {
        mTvCalorie.setText(calorie + " cal");
    }

    @Override
    public void refresh() {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void updatePermissionStatus(boolean permissionStatus) {
        mPermissionDenied = permissionStatus;
    }
}

