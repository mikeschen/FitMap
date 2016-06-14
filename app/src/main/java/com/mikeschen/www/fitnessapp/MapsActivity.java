package com.mikeschen.www.fitnessapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.SupportMapFragment;

public class MapsActivity extends AppCompatActivity implements MapInterface.View {
    private boolean mPermissionDenied = false;
    private Context mContext;
    private MapActivityPresenter mMapActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mContext = this;
        mMapActivityPresenter = new MapActivityPresenter(this, mContext, mapFragment);
        mMapActivityPresenter.loadMap();
    }

    public void showMap() {
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

