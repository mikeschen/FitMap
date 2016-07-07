package com.mikeschen.www.fitnessapp.maps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.Constants;
import com.mikeschen.www.fitnessapp.Meals.MealsActivity;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.main.MainActivity;
import com.mikeschen.www.fitnessapp.models.Route;
import com.mikeschen.www.fitnessapp.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapsActivity extends BaseActivity implements
        MapInterface.View,
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        View.OnClickListener {

    private boolean mPermissionDenied = false;
    private MapActivityPresenter mMapActivityPresenter;
    @Bind(R.id.atOrigin) EditText atOrigin;
    @Bind(R.id.atDestination) EditText atDestination;
    @Bind(R.id.btnFindPath) Button btnFindPath;
    @Bind(R.id.tvDistance) TextView mTvDistance;
    @Bind(R.id.tvDuration) TextView mTvDuration;
    @Bind(R.id.tvCalorie) TextView mTvCalorie;
    @Bind(R.id.homeButton) ImageButton mHomeButton;
    @Bind(R.id.workButton) ImageButton mWorkButton;
    @Bind(R.id.iconWalk) ImageView mIconWalk;

    private String mHomeAddress;
    private String mWorkAddress;
    private String destination;
    public ProgressDialog progressDialog;

    private Long calorie;
    public GoogleMap mMap;
    private UiSettings mUiSettings;
    GPSTracker gps;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    private ArrayList<String> distances;
    private ArrayList<Integer> durations;
    private ArrayList<Long> routeCalories;
    private boolean switcher = true;
    private Switch bikeSwitch;
    private boolean bikeSwitcher = false;

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

        btnFindPath.setOnClickListener(this);
        mHomeButton.setOnClickListener(this);
        mWorkButton.setOnClickListener(this);

        atOrigin.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        atDestination.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        bikeSwitch = (Switch) findViewById(R.id.bikeSwitch);

        bikeSwitch.setChecked(false);
        bikeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String destination = atDestination.getText().toString();
                if(isChecked){
                    bikeSwitcher = true;
                    if(!destination.isEmpty()) {
                        sendRequest();
                        setHideSoftKeyboard(atDestination);
                        LinearLayout pathFinder = (LinearLayout)findViewById(R.id.pathFinder);
                        pathFinder.setVisibility(LinearLayout.VISIBLE);
                        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim);
                        animation.setDuration(1000);
                        pathFinder.setAnimation(animation);
                        pathFinder.animate();
                        animation.start();
                    }
                } else {
                    bikeSwitcher = false;
                    if(!destination.isEmpty()) {
                        sendRequest();
                        setHideSoftKeyboard(atDestination);
                        LinearLayout pathFinder = (LinearLayout)findViewById(R.id.pathFinder);
                        pathFinder.setVisibility(LinearLayout.VISIBLE);
                        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim);
                        animation.setDuration(1000);
                        pathFinder.setAnimation(animation);
                        pathFinder.animate();
                        animation.start();
                    }
                }
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapActivityPresenter = new MapActivityPresenter(this);
        mapFragment.getMapAsync(this);
        destination = getIntent().getStringExtra("destination");
        atDestination.setText(destination, TextView.BufferType.EDITABLE);
        distances = new ArrayList<>();
        durations = new ArrayList<>();
        routeCalories = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btnFindPath):
                sendRequest();
                setHideSoftKeyboard(atDestination);
                LinearLayout pathFinder = (LinearLayout)findViewById(R.id.pathFinder);
                pathFinder.setVisibility(LinearLayout.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim);
                animation.setDuration(1000);
                pathFinder.setAnimation(animation);
                pathFinder.animate();
                animation.start();
                break;
            case (R.id.homeButton):
                mHomeAddress = mSharedPreferences.getString(Constants.PREFERENCES_HOME, null);
                atDestination.setText(mHomeAddress);
                String homeDestination = atDestination.getText().toString();
                if (homeDestination.isEmpty()) {
                    mWorkButton.setBackgroundResource(R.drawable.buttonred);
                    mHomeButton.setBackgroundResource(R.drawable.buttonred2);
                } else {
                    mHomeButton.setBackgroundResource(R.drawable.redbutton2down);
                    mWorkButton.setBackgroundResource(R.drawable.buttonred);
                }
                break;
            case (R.id.workButton):
                mWorkAddress = mSharedPreferences.getString(Constants.PREFERENCES_WORK, null);
                atDestination.setText(mWorkAddress);
                String workDestination = atDestination.getText().toString();
                if (workDestination.isEmpty()) {
                    mWorkButton.setBackgroundResource(R.drawable.buttonred);
                    mHomeButton.setBackgroundResource(R.drawable.buttonred2);
                } else {
                    mWorkButton.setBackgroundResource(R.drawable.redbuttondown);
                    mHomeButton.setBackgroundResource(R.drawable.buttonred2);
                }
                break;
        }
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
            Toast.makeText(mContext, "Please Enter A Destination Address!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = ProgressDialog.show(mContext, "Please wait...",
                "Finding Directions", true);
        mMapActivityPresenter.makeRequest(origin, destination, bikeSwitcher);
    }

    @Override
    public void displayDirections(List<Route> routes) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            double miles = Math.round((route.distance.value * 0.000621371) * 10d) / 10d;
            int minutes = Math.round(route.duration.value / 60);
            durations.add(minutes);
            distances.add(miles + " miles");

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            showDistance(miles + " miles");
            showDuration(minutes);
            calorie = Math.round(route.distance.value / 16.1);
            if(bikeSwitcher) {
                calorie = calorie/2;
            }
            showCalorieRoute(calorie);
            routeCalories.add(calorie);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.invisible))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
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
                    .color(Color.rgb(66, 133, 244))
                    .width(20)
                    .geodesic(true);
            if (switcher == true) {
                polylineOptions.color(Color.rgb(78, 160, 257));
                switcher = false;
            }

            for (int i = 0; i < route.points.size(); i++) {
                Log.d("points", route.points.get(i)+"");
                polylineOptions.add(route.points.get(i));
            }
            polylinePaths.add(mMap.addPolyline(polylineOptions));
            showDistance(distances.get(0));
            showDuration(durations.get(0));
            showCalorieRoute(routeCalories.get(0));
        }

        for (Polyline polyline : polylinePaths) {
            polyline.setClickable(true);
        }

        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick (Polyline clickedPolyline) {
                for (int i = 0; i < polylinePaths.size(); i++) {
                    if (polylinePaths.get(i).getId().equals(clickedPolyline.getId())) {
                        clickedPolyline.setColor(Color.rgb(78, 160, 257));
                        showDistance(distances.get(i));
                        showDuration(durations.get(i));
                        showCalorieRoute(routeCalories.get(i));
                    } else {
                        polylinePaths.get(i).setColor(Color.rgb(66, 133, 244));
                    }
                }
            }
        });
        switcher = true;
    }

    @Override
    public void clearMap() {
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
    public void showDistance(String distance) {
        progressDialog.dismiss();
        mTvDistance.setText(distance);
        if(bikeSwitcher) {
            mIconWalk.setImageResource(R.drawable.ic_directions_bike_black_24dp);
        } else {
            mIconWalk.setImageResource(R.drawable.ic_directions_walk_black_24dp);
        }
    }

    @Override
    public void showDuration(int duration) {
        int hours = duration / 60;
        int minutes = duration % 60;
        mTvDuration.setText(hours + " h " + minutes + " min");
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

    @Override
    public void closeDialog() {
        progressDialog.dismiss();
        Toast.makeText(this, "No Route Found", Toast.LENGTH_SHORT).show();
    }
}

