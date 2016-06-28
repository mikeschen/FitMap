package com.mikeschen.www.fitnessapp.main;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.Constants;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.maps.MapInterface;
import com.mikeschen.www.fitnessapp.maps.MapPresenter;
import com.mikeschen.www.fitnessapp.maps.MapsActivity;
import com.mikeschen.www.fitnessapp.models.Calories;
import com.mikeschen.www.fitnessapp.models.Steps;
import com.mikeschen.www.fitnessapp.utils.DatabaseHelper;
import com.mikeschen.www.fitnessapp.utils.PermissionUtils;

import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends BaseActivity implements
        MainInterface.View,
        StepCounterInterface.View,
        View.OnClickListener,
        SensorEventListener {

    private boolean mPermissionDenied = false;
    private int caloriesBurned = 0;
    private String buttonDisplay;

    private TipPresenter mTipPresenter;
    private StepCounterPresenter mStepCounterPresenter;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private NotificationCompat.Builder mBuilder;

    @Bind(R.id.mainButton) Button mMainButton;
    @Bind(R.id.tipTextView) TextView mTipTextView;
    @Bind(R.id.tipsTextView) TextView mTipsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        buttonDisplay = "Calories";
        buttonDisplay = "Calories";
        mMainButton.setText("Calories Burned: " + caloriesBurned);
        mMainButton.setOnClickListener(this);

        mTipPresenter = new TipPresenter(this);
        mStepCounterPresenter = new StepCounterPresenter(this);

        mSensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        long lastKnownTime = mSharedPreferences.getLong(Constants.PREFERENCES_TIME_KEY, 0);
        int lastKnownSteps = mSharedPreferences.getInt(Constants.PREFERENCES_STEPS_KEY, 0);
        long lastKnownId = mSharedPreferences.getLong(Constants.PREFERENCES_STEPS_ID_KEY, 0);
        int lastKnownCalories = lastKnownSteps * 175/3500;

        mStepCounterPresenter.checkDaysPassed(lastKnownSteps, lastKnownCalories, lastKnownTime, lastKnownId);
//        Log.d("Last known steps", lastKnownSteps + "");
//        Log.d("Last Known id", lastKnownId + "");


        String json;
        try {
            InputStream is = mContext.getAssets().open("tips.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            mTipPresenter.loadTip(json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        mStepCounterPresenter.loadSteps();//This sets text in Steps Taken Button on start
    }


    //Calligraphy

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide action item
                getSupportActionBar().setTitle("");
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getSupportActionBar().setTitle("FitnessApp");
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String destination) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("destination", destination);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }


    @Override
    public void showTip(String tip) {
        mTipTextView.setText(tip);
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
    public void showSteps(Steps steps) {
        db.updateSteps(steps);
        mMainButton.setText("Steps Taken: " + steps.getStepsTaken());
        db.closeDB();
    }

    @Override
    public void showCalories(Calories calories) {
        db.updateCaloriesBurned(calories);
        mMainButton.setText("Calories Burned: " + calories.getCalories());
    }

    @Override
    public void onPause() {
        mStepCounterPresenter.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void refresh() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mStepCounterPresenter.calculateSteps(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void buildNotification(int steps) {
        mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_accessibility_white_24dp)
                .setContentTitle("My notification")
                .setContentText("You walked " + steps + " steps today!");

        Intent resultIntent = new Intent(mContext, StatsActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public long createNewDBRows(Steps stepRecord, Calories calorieRecord) {
        long stepRecord_id = db.logSteps(stepRecord);
        db.logCaloriesBurned(calorieRecord);
        db.closeDB();
        return stepRecord_id;
    }

    @Override
    public void addToSharedPreferences(long time, int steps, long id) {
        mEditor.putLong(Constants.PREFERENCES_TIME_KEY, time).apply();
        mEditor.putInt(Constants.PREFERENCES_STEPS_KEY, steps).apply();
        mEditor.putLong(Constants.PREFERENCES_STEPS_ID_KEY, id).apply();
    }
}


