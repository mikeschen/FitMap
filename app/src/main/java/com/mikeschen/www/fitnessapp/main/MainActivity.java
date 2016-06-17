package com.mikeschen.www.fitnessapp.main;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.Constants;
import com.mikeschen.www.fitnessapp.DatabaseHelper;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.Steps;
import com.mikeschen.www.fitnessapp.maps.MapInterface;
import com.mikeschen.www.fitnessapp.maps.MapPresenter;
import com.mikeschen.www.fitnessapp.utils.PermissionUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements
        MainInterface.View,
        MapInterface.View,
        StepCounterInterface.View,
        View.OnClickListener {

    private boolean mPermissionDenied = false;
    private int caloriesBurned = 0;
    private String buttonDisplay;
    //    private ListView mDrawerList;
//    private DrawerLayout mDrawerLayout;
//    private ArrayAdapter<String> mAdapter;
//    private ActionBarDrawerToggle mDrawerToggle;
//    private String mActivityTitle;
    private Context mContext;
    private TipPresenter mTipPresenter;
    private MapPresenter mMapPresenter;
    private StepCounterPresenter mStepCounterPresenter;
    private Steps stepRecord;
    private Steps steps;
    private int currentSteps;
    DatabaseHelper db;

    private Timer timer;
    private TimerTask timerTask;

    private int currentStepsTableId;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Bind(R.id.mainButton)
    Button mMainButton;
    @Bind(R.id.tipTextView)
    TextView mTipTextView;
    @Bind(R.id.tipsTextView)
    TextView mTipsTextView;

    //THIS IS A TEST TEXT VIEW TO SEE IF I CAN RETRIEVE DATA FROM SQL DB
//    @Bind(R.id.testDBTextView1) TextView mTestDBTextView1;
//    @Bind(R.id.testDBTextView2) TextView mTestDBTextView2;
//    @Bind(R.id.testDBTextView3) TextView mTestDBTextView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        long lastKnownTime = mSharedPreferences.getLong(Constants.PREFERENCES_TIME_KEY, 0);
        int lastKnownSteps = mSharedPreferences.getInt(Constants.PREFERENCES_STEPS_KEY, 1);
        Log.d("Last known steps", lastKnownSteps + "");

        currentSteps = 0;
        currentStepsTableId = 1;

//        stepRecord = new Steps(currentStepsTableId, currentSteps, 345);

        Typeface myTypeFace = Typeface.createFromAsset(getAssets(), "fonts/PTN77F.ttf");
        mMainButton.setTypeface(myTypeFace);
        mTipsTextView.setTypeface(myTypeFace);
        mTipTextView.setTypeface(myTypeFace);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        buttonDisplay = "Calories";
        buttonDisplay = "Calories";
        mMainButton.setText("Calories Burned: " + caloriesBurned);
        mMainButton.setOnClickListener(this);
        mContext = this;

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();


//        mDrawerList = (ListView) findViewById(R.id.navList);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mActivityTitle = getTitle().toString();
        mTipPresenter = new TipPresenter(this, mContext);
        mMapPresenter = new MapPresenter(this, mContext, mapFragment);
        mStepCounterPresenter = new StepCounterPresenter(this, mContext);


//        addDrawerItems();
//        setupDrawer();
        mTipPresenter.loadTip();
        mMapPresenter.loadMap();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


//        db = new DatabaseHelper(getApplicationContext());

        mStepCounterPresenter.setStepCount(lastKnownSteps);//This sets text in Steps Taken Button on start
    }


    @Override
    public void showTip(String tip) {
        mTipTextView.setText(tip);
    }

    //Google Maps
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
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
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.mainButton):
                if (buttonDisplay.equals("Calories")) {
                    buttonDisplay = "Steps";
                    mStepCounterPresenter.loadSteps();
                } else if (buttonDisplay.equals("Steps")) {
                    buttonDisplay = "Calories";
                    mStepCounterPresenter.loadCalories();
                }
        }
    }

    @Override
    public void showMap() {
    }

    @Override
    public void showDistance(String distance) {
    }

    @Override
    public void showDuration(String duration) {
    }

    @Override
    public void showCalorieRoute(int calorie) {
    }

    @Override
    public void showSteps(int stepCount) {
//        stepRecord = new Steps(currentStepsTableId, stepCount+currentSteps, 345);
//        db.updateSteps(stepRecord);
//        mTestDBTextView.setText(String.valueOf(stepCount));

//        mTestDBTextView1.setText(String.valueOf(db.getSteps(1).getStepsTaken()));
//        mTestDBTextView2.setText(String.valueOf(db.getSteps(2).getStepsTaken()));
//        mTestDBTextView3.setText(String.valueOf(db.getSteps(3).getStepsTaken()));

        mMainButton.setText("Steps Taken: " + stepCount);
    }

    @Override
    public void showCalories(int caloriesBurned) {
        mMainButton.setText("Calories Burned: " + caloriesBurned);
    }

    @Override
    public void onPause() {
        long destroyTime = System.currentTimeMillis() / 1000;
        int destroySteps = mStepCounterPresenter.getStepCount();
//        int destroyId = mStepCounterPresenter.

        Log.d("Destroy Time", destroyTime + "");
        Log.d("Destroy Steps", destroySteps + "");
        addToSharedPreferences(destroyTime, destroySteps);
        Log.d("Shared Prefs", mSharedPreferences + "");
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void addToSharedPreferences(long time, int steps) {
        mEditor.putLong(Constants.PREFERENCES_TIME_KEY, time).apply();
        mEditor.putInt(Constants.PREFERENCES_STEPS_KEY, steps).apply();
    }

}


