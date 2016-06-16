package com.mikeschen.www.fitnessapp.main;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;

public class StepCounterPresenter implements
        StepCounterInterface.Presenter,
        SensorEventListener {

    private StepCounterInterface.View mStepCounterView;
    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private int stepCount;

    private float totalAverageSpeed;
    private int speedCounted;
    private float grossTotalSpeed;
    private boolean checkSpeedDirection;

    private int caloriesBurned;

    private ArrayList<Float> speedData;

    public StepCounterPresenter(StepCounterInterface.View stepCounterInterface, Context context) {
        mStepCounterView = stepCounterInterface;
        mContext = context;

        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        stepCount = 0;
        totalAverageSpeed = 1;
        speedCounted = 1;
        grossTotalSpeed = 0;
        checkSpeedDirection = true;
        speedData = new ArrayList<>();
        caloriesBurned = 0;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
        mStepCounterView.showSteps(this.stepCount);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
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

                if(localAverageSpeed > 10) { // CHANGE THIS TO 20
                    speedCounted++;
                    grossTotalSpeed = grossTotalSpeed + speed;
                    totalAverageSpeed = (grossTotalSpeed) / speedCounted;
                }

                if(totalAverageSpeed > 15 && speedCounted > 200) {

                    if (checkSpeedDirection) {
                        if (localAverageSpeed > totalAverageSpeed) {
                            checkSpeedDirection = false;
                            setStepCount(getStepCount()+1);
                        }
                    } else {
                        if (localAverageSpeed < totalAverageSpeed) {
                            checkSpeedDirection = true;
                            setStepCount(getStepCount()+1);
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
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public void loadSteps() {
        Log.d("it", "works");
        mStepCounterView.showSteps(getStepCount());
    }

    @Override
    public void loadCalories() {
        caloriesBurned = getStepCount() * 175/3500;
        mStepCounterView.showCalories(caloriesBurned);
    }
}

