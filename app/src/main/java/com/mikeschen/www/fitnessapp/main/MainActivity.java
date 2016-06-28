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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.Constants;
import com.mikeschen.www.fitnessapp.Meals.MealsActivity;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.maps.MapsActivity;
import com.mikeschen.www.fitnessapp.models.Calories;
import com.mikeschen.www.fitnessapp.models.Steps;
import com.mikeschen.www.fitnessapp.utils.DatabaseHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends BaseActivity implements
        MainInterface.View,
        StepCounterInterface.View,
        View.OnClickListener,
        SensorEventListener {

    private int caloriesBurned = 0;
    private String buttonDisplay;
    private Context mContext;
    private TipPresenter mTipPresenter;
    private StepCounterPresenter mStepCounterPresenter;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private NotificationCompat.Builder mBuilder;
    DatabaseHelper db;
    Steps newSteps;
    Calories newCaloriesBurned;
    Calories newCaloriesConsumed;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    int images[] = {R.drawable.citymain, R.drawable.stairwalkmain, R.drawable.walk, R.drawable.girl};

    @Bind(R.id.mainButton) Button mMainButton;
    @Bind(R.id.tipTextView) TextView mTipTextView;
    @Bind(R.id.tipsTextView) TextView mTipsTextView;
    @Bind(R.id.mapsMainButton) Button mMapsMainButton;
    @Bind(R.id.mealsMainButton) Button mMealsMainButton;
    @Bind(R.id.mainlayout) RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if(relativeLayout != null)
            relativeLayout.setBackgroundResource(images[getRandomNumber()]);

        buttonDisplay = "Calories";
        mMainButton.setText("Calories Burned: " + caloriesBurned);
        mMainButton.setOnClickListener(this);
        mMapsMainButton.setOnClickListener(this);
        mMealsMainButton.setOnClickListener(this);
        mContext = this;

        mTipPresenter = new TipPresenter(this);
        mStepCounterPresenter = new StepCounterPresenter(this, mContext);

        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        db = new DatabaseHelper(mContext.getApplicationContext());

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();

        List<Steps> stepsList = db.getAllStepRecords();

        if (stepsList.size() == 0) {
            newSteps = new Steps(1, 0, 345);
            newCaloriesBurned = new Calories(1, 0 ,345);
            newCaloriesConsumed = new Calories(1, 0, 345);
            db.logSteps(newSteps);
            db.logCaloriesBurned(newCaloriesBurned);
            db.logCaloriesConsumed(newCaloriesConsumed);
            db.closeDB();
        }

        long lastKnownTime = mSharedPreferences.getLong(Constants.PREFERENCES_TIME_KEY, 0);
        int lastKnownSteps = mSharedPreferences.getInt(Constants.PREFERENCES_STEPS_KEY, 0);
        long lastKnownId = mSharedPreferences.getLong(Constants.PREFERENCES_STEPS_ID_KEY, 0);
        int lastKnownCalories = lastKnownSteps * 175/3500;

        mStepCounterPresenter.checkDaysPassed(lastKnownSteps, lastKnownCalories, lastKnownTime, lastKnownId);

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

    protected void onResume()
    {
        if(relativeLayout != null)
            relativeLayout.setBackgroundResource(images[getRandomNumber()]);
        super.onResume();
    }

    private int getRandomNumber() {
        return new Random().nextInt(4);
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
                break;
            case (R.id.mapsMainButton):
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                break;
            case (R.id.mealsMainButton):
                Intent intent2 = new Intent(MainActivity.this, MealsActivity.class);
                startActivity(intent2);
                break;
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
        db.closeDB();
    }

    @Override
    public void onPause() {
        mStepCounterPresenter.onPause();
        super.onPause();
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
    public long createNewDBRows(Steps stepRecord, Calories caloriesBurnedRecord, Calories caloriesConsumedRecord) {
        long stepRecord_id = db.logSteps(stepRecord);
        db.logCaloriesBurned(caloriesBurnedRecord);
        db.logCaloriesConsumed(caloriesConsumedRecord);
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


