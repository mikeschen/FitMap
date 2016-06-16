package com.mikeschen.www.fitnessapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.mikeschen.www.fitnessapp.Modules.DirectionFinder;
import com.mikeschen.www.fitnessapp.Modules.DirectionFinderListener;
import com.mikeschen.www.fitnessapp.Modules.Route;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapsActivity extends AppCompatActivity implements MapInterface.View {
    private boolean mPermissionDenied = false;
    private Context mContext;
    private MapActivityPresenter mMapActivityPresenter;
    @Bind(R.id.atOrigin) EditText atOrigin;
    @Bind(R.id.atDestination) EditText atDestination;
    @Bind(R.id.btnFindPath) Button btnFindPath;
    @Bind(R.id.tvDistance) TextView mTvDistance;
    @Bind(R.id.tvDuration) TextView mTvDuration;
    @Bind(R.id.tvCalorie) TextView mTvCalorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mContext = this;
        mMapActivityPresenter = new MapActivityPresenter(this, mContext, mapFragment);
        mMapActivityPresenter.loadMap();
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        String origin = atOrigin.getText().toString();
        String destination = atDestination.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(mContext, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(mContext, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        mMapActivityPresenter.makeRequest(origin, destination);
    }

    public void showMap() {
    }

    @Override
    public void showDistance(String distance) {
        mTvDistance.setText(distance);
    }

    @Override
    public void showDuration(String duration) {
        mTvDuration.setText(duration);
    }

    @Override
    public void showCalorieRoute(double calorie) {
        mTvCalorie.setText((int)(Math.round(calorie/16.1)) + "cal");
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

