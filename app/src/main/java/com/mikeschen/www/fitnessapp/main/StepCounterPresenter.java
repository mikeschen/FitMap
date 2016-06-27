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
import com.mikeschen.www.fitnessapp.utils.DatabaseHelper;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.models.Steps;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class StepCounterPresenter implements
        StepCounterInterface.Presenter {

    private StepCounterInterface.View mStepCounterView;
    private Context mContext;
//    private SensorManager mSensorManager;
//    private Sensor mAccelerometer;
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
    private Steps stepRecord;
    private Calories calorieRecord;

    private int fullDayInMillis = 86400000;



    public StepCounterPresenter(StepCounterInterface.View stepCounterInterface, Context context) {
        mStepCounterView = stepCounterInterface;
        mContext = context;

        totalAverageSpeed = 1;
        speedCounted = 1;
        grossTotalSpeed = 0;
        checkSpeedDirection = true;
        speedData = new ArrayList<>();
        caloriesBurned = 0;
        currentStepsTableId = 1;




        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("timer", "start?");
                long currentTime = System.currentTimeMillis() / 60000;
                Log.d("currentTime", currentTime + "");

                if (currentTime % (60000/1000) == 0) {
                    //TODO
                    //Do more thorough math with these numbers
                    //(60000/1000) == 0 is 1 hour, BUT ONLY FROM 1PM - 4PM
                    Log.d("tick", "tock");

                    mStepCounterView.buildNotification(stepRecord.getStepsTaken());


                    stepRecord = new Steps(currentStepsTableId, 0, 345);
                    calorieRecord = new Calories(currentStepsTableId, 0, 345);
                    long stepRecord_id = mStepCounterView.createNewDBRows(stepRecord, calorieRecord);
                    stepRecord.setId(stepRecord_id);
                    calorieRecord.setId(stepRecord_id);

                }
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, 1000); //CHANGE THIS NUMBER TO 1000 FOR DEBUGGING
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
                            stepRecord.setStepsTaken(stepRecord.getStepsTaken()+1);
                            loadSteps();
                        }
                    } else {
                        if (localAverageSpeed < totalAverageSpeed) {
                            checkSpeedDirection = true;
                            stepRecord.setStepsTaken(stepRecord.getStepsTaken()+1);
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
        stepRecord = new Steps(stepRecord.getId(), stepRecord.getStepsTaken(), 345);
        mStepCounterView.showSteps(stepRecord);
    }

    @Override
    public void loadCalories() {
        caloriesBurned = stepRecord.getStepsTaken() * 175/3500;
        calorieRecord.setCalories(caloriesBurned);
        mStepCounterView.showCalories(calorieRecord);
    }

    public void onPause() {
        long destroyTime = System.currentTimeMillis();
        int destroySteps = stepRecord.getStepsTaken();
        long destroyId = stepRecord.getId();
        Log.d("Destroy Time", destroyTime + "");
        Log.d("Destroy Steps", destroySteps + "");
        mStepCounterView.addToSharedPreferences(destroyTime, destroySteps, destroyId);
    }


    @Override
    public void checkDaysPassed(int lastKnownSteps, int lastKnownCalories, long lastKnownTime, long lastKnownId) {
        long currentTime = System.currentTimeMillis();
        long daysPassed;
        if (lastKnownTime > 0) {
            daysPassed = (currentTime / fullDayInMillis) - (lastKnownTime / fullDayInMillis);
        } else {
            daysPassed = 0;
        }
        Log.d("passed time", currentTime + "");
        Log.d("passed last known", lastKnownTime + "");
        Log.d("days Passed", daysPassed + "");

        if (daysPassed == 0) { //THIS IS FOR TURNING ON AND OFF WITHIN THE SAME DAY
            Log.d("database", "works");
            stepRecord = new Steps(lastKnownId, lastKnownSteps, 345);
            calorieRecord = new Calories(lastKnownId, lastKnownCalories, 345);
        } else {
            stepRecord = new Steps(lastKnownId, 0, 345);
            calorieRecord = new Calories(lastKnownId, 0, 345);
            if (daysPassed > 1) {
                for (int i = 0; i > daysPassed - 1; i++) { //FOR LOOP ADDS FIELDS FOR DAYS YOU MISSED
                    mStepCounterView.createNewDBRows(stepRecord, calorieRecord);
                }
            }
            long stepRecordId = mStepCounterView.createNewDBRows(stepRecord, calorieRecord); //FOR CURRENT DAY
            stepRecord.setId(stepRecordId);
            calorieRecord.setId(stepRecordId);
        }
    }
}


