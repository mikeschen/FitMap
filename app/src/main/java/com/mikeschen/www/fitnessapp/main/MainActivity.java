package com.mikeschen.www.fitnessapp.main;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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

import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.Constants;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.maps.MapsActivity;
import com.mikeschen.www.fitnessapp.models.Days;
import com.mikeschen.www.fitnessapp.utils.DatabaseHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
    private Context mContext;
    private TipPresenter mTipPresenter;
    private StepCounterPresenter mStepCounterPresenter;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private NotificationCompat.Builder mBuilder;
    DatabaseHelper db;
    Days newDays;
//    Steps newSteps;
//    Calories newCaloriesBurned;
//    Calories newCaloriesConsumed;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Bind(R.id.mainButton) Button mMainButton;
    @Bind(R.id.tipTextView) TextView mTipTextView;
    @Bind(R.id.tipsTextView) TextView mTipsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        buttonDisplay = "Calories";
        mMainButton.setText("Calories Burned: " + caloriesBurned);
        mMainButton.setOnClickListener(this);
        mContext = this;

        mTipPresenter = new TipPresenter(this);
        mStepCounterPresenter = new StepCounterPresenter(this, mContext);

        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        db = new DatabaseHelper(mContext.getApplicationContext());

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();

        List<Days> daysList = db.getAllDaysRecords();


        // This creates a table on first use of app
        if (daysList.size() == 0) {
            newDays = new Days(1, 0, 0, 0, 0);
//            newSteps = new Steps(1, 0, 345);
//            newCaloriesBurned = new Calories(1, 0 ,345);
//            newCaloriesConsumed = new Calories(1, 0, 345);
            db.logDays(newDays);
//            db.logCaloriesBurned(newCaloriesBurned);
//            db.logCaloriesConsumed(newCaloriesConsumed);
            db.closeDB();
        }

        // Retrieves data when app is opened after crash/close and creates tables for each day app was not used
        long lastKnownTime = mSharedPreferences.getLong(Constants.PREFERENCES_LAST_KNOWN_TIME_KEY, 0);
        int lastKnownSteps = mSharedPreferences.getInt(Constants.PREFERENCES_LAST_KNOWN_STEPS_KEY, 0);
        long lastKnownId = mSharedPreferences.getLong(Constants.PREFERENCES_STEPS_ID_KEY, 0);
        int lastKnownCalories = lastKnownSteps * 175/3500;

        mStepCounterPresenter.checkDaysPassed(lastKnownSteps, lastKnownCalories, lastKnownTime, lastKnownId);

        //Calls tips
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
        searchView.setQueryHint("Enter Destination...");
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setTitle("");
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getSupportActionBar().setTitle("FitMap");
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
                    mStepCounterPresenter.loadSteps();
                }
        }
    }

    @Override
    public void showSteps(Days days) {
        mEditor.putInt(Constants.PREFERENCES_CURRENT_STEPS_KEY, days.getStepsTaken());
        mEditor.putInt(Constants.PREFERENCES_CURRENT_CALORIES_KEY, days.getCaloriesBurned());

        if (buttonDisplay.equals("Calories")) {
            mMainButton.setText("Calories Burned: " + days.getCaloriesBurned());
        } else if (buttonDisplay.equals("Steps")) {
            mMainButton.setText("Steps Taken: " + days.getStepsTaken());
        }
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
        Log.d("buildNotification", "Is it building?");

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
    public long createNewDBRows(Days dayRecord) {
        long stepRecord_id = db.logDays(dayRecord);

        db.closeDB();
        return stepRecord_id;
    }

    @Override
    public void addToSharedPreferences(long time, int steps, long id) {
        mEditor.putLong(Constants.PREFERENCES_LAST_KNOWN_TIME_KEY, time).apply();
        mEditor.putInt(Constants.PREFERENCES_LAST_KNOWN_STEPS_KEY, steps).apply();
        mEditor.putLong(Constants.PREFERENCES_STEPS_ID_KEY, id).apply();
    }
}


