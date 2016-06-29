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
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mikeschen.www.fitnessapp.models.Calories;
import com.mikeschen.www.fitnessapp.Constants;
import com.mikeschen.www.fitnessapp.models.Days;
import com.mikeschen.www.fitnessapp.utils.DatabaseHelper;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.models.Steps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class StepCounterPresenter implements
        StepCounterInterface.Presenter {

    private StepCounterInterface.View mStepCounterView;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;

    private float totalAverageSpeed;
    private int speedCounted;
    private float grossTotalSpeed;
    private boolean checkSpeedDirection;

    private int caloriesBurned;

    private ArrayList<Float> speedData;

    private Timer timer;
    private TimerTask timerTask;

    private int currentStepsTableId;
    private int currentDaysTableId;
    private Days daysRecord;

    private int fullDayInMillis = 86400000;



    public StepCounterPresenter(StepCounterInterface.View stepCounterInterface) {
        mStepCounterView = stepCounterInterface;

        totalAverageSpeed = 1;
        speedCounted = 1;
        grossTotalSpeed = 0;
        checkSpeedDirection = true;
        speedData = new ArrayList<>();
        caloriesBurned = 0;
        currentStepsTableId = 1;

        currentDaysTableId = 1;

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis() / 60000;

                if (currentTime % (60 * 24) == 0) { // WHEN YOU CHANGE THIS, ALSO CHANGE IN DAYS PASSED METHOD
                    //TODO
                    //Do more thorough math with these numbers
                    Log.d("tick", "tock");

                    daysRecord = mStepCounterView.endOfDaySave();
                    mStepCounterView.buildNotification(daysRecord.getStepsTaken());

                    // Builds new, empty database row when notification fires
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM / dd / yyyy", Locale.getDefault());
                    daysRecord = new Days(currentDaysTableId, 0, 0, 0, dateFormat.toString());
                    long daysRecord_id = mStepCounterView.createNewDBRows(daysRecord);
                    daysRecord.setId(daysRecord_id);

                }
                checkMidnight(currentTime);
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, 60000); //CHANGE THIS NUMBER TO 1000 FOR DEBUGGING
    }

    @Override
    public void calculateSteps(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if(System.currentTimeMillis()-lastUpdate > 20) {

                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                long curTime = System.currentTimeMillis();

                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 1000;
                if(speedData.size() < 20) {
                    speedData.add(speed);
                } else {
                    speedData.remove(0);
                    speedData.add(speed);
                }
                float localGrossSpeed = 0;
                float localAverageSpeed;
                for(int i = 0; i < speedData.size(); i++)  {
                    localGrossSpeed += speedData.get(i);
                }
                localAverageSpeed = localGrossSpeed/speedData.size();

                if(localAverageSpeed > 20) {
                    speedCounted++;
                    grossTotalSpeed = grossTotalSpeed + speed;
                    totalAverageSpeed = (grossTotalSpeed) / speedCounted;
                }

                if(totalAverageSpeed > 15 && speedCounted > 200) {

                    if (checkSpeedDirection) {
                        if (localAverageSpeed > totalAverageSpeed) {
                            checkSpeedDirection = false;
                            daysRecord.setStepsTaken(daysRecord.getStepsTaken()+1);

                            caloriesBurned = daysRecord.getStepsTaken() * 175/3500;
                            daysRecord.setCaloriesBurned(caloriesBurned);

                            loadSteps();
                        }
                    } else {
                        if (localAverageSpeed < totalAverageSpeed) {
                            checkSpeedDirection = true;
                            daysRecord.setStepsTaken(daysRecord.getStepsTaken()+1);
                            loadSteps();
                        }
                    }
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }


    @Override
    public void loadSteps() {
        mStepCounterView.showSteps(daysRecord);
    }

//    @Override
//    public void loadCalories() {
//        caloriesBurned = stepRecord.getStepsTaken() * 175/3500;
//        caloriesBurnedRecord.setCalories(caloriesBurned);
//        mStepCounterView.showCalories(caloriesBurnedRecord);
//    }

    public void onPause() {
        long destroyTime = System.currentTimeMillis();
        int destroySteps = daysRecord.getStepsTaken();
        long destroyId = daysRecord.getId();
        Log.d("Destroy Time", destroyTime + "");
        Log.d("Destroy Steps", destroySteps + "");
        mStepCounterView.addToSharedPreferences(destroyTime, destroySteps, destroyId);
    }


    @Override
    public void checkDaysPassed(int lastKnownSteps, int lastKnownCalories, long lastKnownTime, long lastKnownId) {
        long currentTime = System.currentTimeMillis();
        long daysPassed;
        if (lastKnownTime > 0) {
            daysPassed = (currentTime / (1000 * 60 * 60 )) - (lastKnownTime / (1000 * 60 * 60 )); //THIS SHOULD SYNC WITH TIMER RUNNING EVERY HOUR
        } else {
            daysPassed = 0;
        }
        Log.d("passed time", currentTime + "");
        Log.d("passed last known", lastKnownTime + "");
        Log.d("days Passed", daysPassed + "");

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM / dd / yyyy", Locale.getDefault());

        if (daysPassed == 0) { //THIS IS FOR TURNING ON AND OFF WITHIN THE SAME DAY
            Log.d("database", "works");

            daysRecord = new Days(lastKnownId, lastKnownSteps, lastKnownCalories, 0, dateFormat.format(lastKnownTime));
        } else {
            long dateInMillis = lastKnownTime + fullDayInMillis;
            daysRecord = new Days(lastKnownId, 0, 0, 0, dateFormat.format(dateInMillis));
            if (daysPassed > 1) {
                for (int i = 0; i > daysPassed - 1; i++) { //FOR LOOP ADDS FIELDS FOR DAYS YOU MISSED
                    mStepCounterView.createNewDBRows(daysRecord);
                    dateInMillis += fullDayInMillis;
                    daysRecord.setDate(dateFormat.format(dateInMillis));
                }
            }
            long stepRecordId = mStepCounterView.createNewDBRows(daysRecord); //FOR CURRENT DAY
            daysRecord.setId(stepRecordId);
        }
    }

    public void checkMidnight(long currentTime) {
        if (currentTime % (60 * 24) == 0) { // WHEN YOU CHANGE THIS, ALSO CHANGE IN DAYS PASSED METHOD
            //TODO
            //Do more thorough math with these numbers
            Log.d("tick", "tock");

            daysRecord = mStepCounterView.endOfDaySave();
            mStepCounterView.buildNotification(daysRecord.getStepsTaken());

            // Builds new, empty database row when notification fires
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM / dd / yyyy", Locale.getDefault());
            daysRecord = new Days(currentDaysTableId, 0, 0, 0, dateFormat.toString());
//                    caloriesBurnedRecord = new Calories(currentStepsTableId, 0, 345);
//                    caloriesConsumedRecord = new Calories(currentStepsTableId, 0, 345);
            long daysRecord_id = mStepCounterView.createNewDBRows(daysRecord);
            daysRecord.setId(daysRecord_id);
//                    caloriesBurnedRecord.setId(stepRecord_id);
//                    caloriesConsumedRecord.setId(stepRecord_id);

        }
    }
}


