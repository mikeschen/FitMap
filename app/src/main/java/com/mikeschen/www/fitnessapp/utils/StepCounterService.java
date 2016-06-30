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
import android.util.Log;

import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.main.StepCounterInterface;
import com.mikeschen.www.fitnessapp.main.StepCounterPresenter;
import com.mikeschen.www.fitnessapp.models.Days;

import java.util.ArrayList;

public class StepCounterService extends Service implements SensorEventListener {

    private static boolean isRunning;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

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
    private float last_z;

    private ArrayList<Float> speedData;

    private int speedCounted;
    private float grossTotalSpeed;
    private float totalAverageSpeed;
    private int stepCount;

    private boolean checkSpeedDirection;

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
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
        for(int i = mClients.size()-1; i >= 0; i--) {
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

        mSharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", 0);
        mEditor = mSharedPreferences.edit();

        mSensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        lastUpdate = 0;
        last_x = 0;
        last_y = 0;
        last_z = 0;

        speedData = new ArrayList<>();

        speedCounted = mSharedPreferences.getInt("speedCounted", 1);
        grossTotalSpeed = mSharedPreferences.getFloat("grossTotalSpeed", 0);
        stepCount = mSharedPreferences.getInt("stepsTaken", 0);

        totalAverageSpeed = 1;

        checkSpeedDirection = true;
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
            if(System.currentTimeMillis()-lastUpdate > 20) {

                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                long curTime = System.currentTimeMillis();

                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 1000;
                if (speedData.size() < 20) {
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

                if (localAverageSpeed > 20) {
                    speedCounted++;
                    grossTotalSpeed = grossTotalSpeed + localAverageSpeed;
                    totalAverageSpeed = (grossTotalSpeed) / speedCounted;

                }

                if (totalAverageSpeed > 15 && speedCounted > 200) {

                    if (checkSpeedDirection) {
                        if (localAverageSpeed > totalAverageSpeed) {
                            checkSpeedDirection = false;
                            stepCount++;
                            Log.d("step taken", stepCount + "");
                            mEditor.putInt("stepsTaken", stepCount).commit();
                            mEditor.putFloat("grossTotalSpeed", grossTotalSpeed).commit();
                            mEditor.putInt("speedCounted", speedCounted).commit();
                            sendMessageToUI(stepCount);
                        }
                    } else {
                        if (localAverageSpeed < totalAverageSpeed) {
                            checkSpeedDirection = true;
                            stepCount++;
                            mEditor.putInt("stepsTaken", stepCount).commit();
                            mEditor.putFloat("grossTotalSpeed", grossTotalSpeed).commit();
                            mEditor.putInt("speedCounted", speedCounted).commit();
                            Log.d("step taken", mSharedPreferences.getInt("stepsTaken", 0)+"");
                            sendMessageToUI(stepCount);
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
    public void onDestroy() {
        super.onDestroy();
        stepCount = 0;
        Log.i("MyService", "Service Stopped.");
        isRunning = false;
        mSensorManager.unregisterListener(this);
    }
}
