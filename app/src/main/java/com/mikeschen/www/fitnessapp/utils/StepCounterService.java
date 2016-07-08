package com.mikeschen.www.fitnessapp.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mikeschen.www.fitnessapp.Constants;
import com.mikeschen.www.fitnessapp.models.Days;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StepCounterService extends Service implements SensorEventListener {

    private static boolean isRunning;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    DatabaseHelper db;
    HeightWeightDatabaseHelper heightWeightDB;
    Days day;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    //All variables in StepCounterService begin with 1
    public static final int MSG_REGISTER_CLIENT = 10;
    public static final int MSG_UNREGISTER_CLIENT = 11;
    public static final int MSG_SET_STEP_COUNT_VALUE = 12;

    final Messenger mMessenger = new Messenger(new IncomingHandler());
    ArrayList<Messenger> mClients = new ArrayList<>();

    private long lastUpdate;
    private float last_x;
    private float last_y;
    private long lastKnownId;

    private ArrayList<Float> speedData;

    private int speedCounted;
    private float grossTotalSpeed;
    private float totalAverageSpeed;
    private int stepCount;

    private boolean checkSpeedDirection;

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private void sendMessageToUI(int stepCount) {
        for (int i = mClients.size() - 1; i >= 0; i--) {
            try {
                mClients.get(i).send(Message.obtain(null, MSG_SET_STEP_COUNT_VALUE, stepCount, 0));
            } catch (RemoteException e) {
                mClients.remove(i);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Steps", "On Create");
        isRunning = true;

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        mSensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        lastUpdate = 0;
        last_x = 0;
        last_y = 0;

        speedData = new ArrayList<>();

        speedCounted = mSharedPreferences.getInt("speedCounted", 1);
        grossTotalSpeed = mSharedPreferences.getFloat("grossTotalSpeed", 0);

        totalAverageSpeed = 1;

        checkSpeedDirection = true;

        db = new DatabaseHelper(this);
        heightWeightDB = new HeightWeightDatabaseHelper(this);
        List<Days> daysList = db.getAllDaysRecords();
        day = daysList.get(daysList.size() - 1);
        stepCount = day.getStepsTaken();
        lastKnownId = day.getId();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    public static boolean isRunning() {
        return isRunning;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (System.currentTimeMillis() - lastUpdate > 20) {

                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];

                long curTime = System.currentTimeMillis();

                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y - last_x - last_y) / diffTime * 1000;
                if (speedData.size() < 30) {
                    speedData.add(speed);
                } else {
                    speedData.remove(0);
                    speedData.add(speed);
                }
                float localGrossSpeed = 0;
                float localAverageSpeed;
                for (int i = 0; i < speedData.size(); i++) {
                    localGrossSpeed += speedData.get(i);
                }
                localAverageSpeed = localGrossSpeed / speedData.size();

                if (localAverageSpeed > 30 && localAverageSpeed < 300) {
                    speedCounted++;
                    grossTotalSpeed = grossTotalSpeed + localAverageSpeed;
                    totalAverageSpeed = (grossTotalSpeed) / speedCounted;
                }

                if (totalAverageSpeed > 15 && speedCounted > 200) {

                    if (checkSpeedDirection) {
                        if (localAverageSpeed > totalAverageSpeed) {
                            checkSpeedDirection = false;
                            List<Days> daysList = db.getAllDaysRecords();
                            day = daysList.get(daysList.size() - 1);
                            if (day.getId() != lastKnownId) {
                                lastKnownId = day.getId();
                                stepCount = 0;
                            }
                            stepCount++;
                            mEditor.putFloat("grossTotalSpeed", grossTotalSpeed).commit();
                            mEditor.putInt("speedCounted", speedCounted).commit();
                            sendMessageToUI(stepCount);
                            day.setStepsTaken(stepCount);

                            //Gathers height and weight and pulls corresponding cals burned data from DB
                            int stride = strideCalculator();
                            int weight = weightCalculator();

                            if (weight > 0 && stride > 0) {
                                float cals = heightWeightDB.getCals(weight, stride);
                                float newCalsBurned = cals / 1000;
                                day.setCaloriesBurned(stepCount * newCalsBurned);
                            } else {
                                day.setCaloriesBurned(stepCount * 175 / 3500);
                                db.updateDays(day);
                            }
                        }
                    } else {
                        if (localAverageSpeed < totalAverageSpeed) {
                            checkSpeedDirection = true;
                            List<Days> daysList = db.getAllDaysRecords();
                            day = daysList.get(daysList.size() - 1);
                            if (day.getId() != lastKnownId) {
                                lastKnownId = day.getId();
                                stepCount = 0;
                            }

                            stepCount++;
                            mEditor.putFloat("grossTotalSpeed", grossTotalSpeed).commit();
                            mEditor.putInt("speedCounted", speedCounted).commit();
                            sendMessageToUI(stepCount);
                            day.setStepsTaken(stepCount);

                            //Gathers height and weight and pulls corresponding cals burned data from DB
                            int stride = strideCalculator();
                            int weight = weightCalculator();

                            if (weight > 0 && stride > 0) {
                                float cals = heightWeightDB.getCals(weight, stride);
                                float newCalsBurned = cals / 1000;
                                day.setCaloriesBurned(stepCount * newCalsBurned);
                            } else {
                                day.setCaloriesBurned(stepCount * 175 / 3500);
                                db.updateDays(day);
                                db.closeDB();
                            }
                        }
                    }
                }

                last_x = x;
                last_y = y;
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stepCount = 0;
        Log.i("MyService", "Service Stopped.");
        isRunning = false;
        mSensorManager.unregisterListener(this);
    }

    //Takes user pref height and uses it to determine which table to pull data from
    public int strideCalculator() {
        int height = mSharedPreferences.getInt(Constants.PREFERENCES_HEIGHT, 0);
        int stride = 2000;

        double inchPerStride = (height * .413);
        double feetPerStride = (inchPerStride / 12);
        Double dStepsPerMile = (5280 / feetPerStride);
        int stepsPerMile = dStepsPerMile.intValue();

        if (stepsPerMile > 2301) {
            stride = 2400;
        } else if (stepsPerMile < 2300 && stepsPerMile >= 2101) {
            stride = 2200;
        } else if (stepsPerMile < 2101 && stepsPerMile >= 2000) {
            stride = 2000;
        }
        return stride;
    }

    //Takes user pref weight and uses it to determine which table to pull data from
    public int weightCalculator() {
        String strWeight = mSharedPreferences.getString(Constants.PREFERENCES_WEIGHT, "0");
        int weight = Integer.parseInt(strWeight);

        if (weight >= 300 && weight >= 280) {
            weight = 300;

        } else if (weight >= 260) {
            weight = 275;
        } else if (weight <= 259 && weight >= 230) {
            weight = 250;
        } else if (weight <= 229 && weight >= 210) {
            weight = 220;
        } else if (weight <= 209 && weight >= 190) {
            weight = 200;
        } else if (weight <= 189 && weight >= 170) {
            weight = 180;
        } else if (weight <= 164 && weight >= 150) {
            weight = 160;
        } else if (weight <= 149 && weight >= 130) {
            weight = 140;
        } else if (weight <= 129 && weight >= 110) {
            weight = 120;
        } else if (weight < 110) {
            weight = 100;
        }
        return weight;
    }
}
