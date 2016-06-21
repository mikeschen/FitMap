package com.mikeschen.www.fitnessapp.maps;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.utils.PermissionUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapsActivity extends BaseActivity implements MapInterface.View {
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
        mMapActivityPresenter = new MapActivityPresenter(this, mContext, mapFragment);
        mMapActivityPresenter.loadMap();
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

    private void sendRequest() {
        String origin = atOrigin.getText().toString();
        String destination = atDestination.getText().toString();
        if (origin.isEmpty()) {
            origin = mMapActivityPresenter.myLocationLat + "," + mMapActivityPresenter.myLocationLong;
        }
        if (destination.isEmpty()) {
            Toast.makeText(mContext, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        mMapActivityPresenter.makeRequest(origin, destination);
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
    public void showCalorieRoute(int calorie) {
        mTvCalorie.setText(calorie + "cal");
    }

    @Override
    public void refresh() {

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

