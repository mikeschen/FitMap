package com.mikeschen.www.fitnessapp.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mikeschen.www.fitnessapp.main.StepCounterInterface;
import com.mikeschen.www.fitnessapp.models.Days;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ramon on 6/30/16.
 */
public class TimerService extends Service {

    private StepCounterInterface.View mStepCounterView;


    private static boolean isRunning;

    //All variables in Timer Service begin with 2
    public static final int MSG_REGISTER_CLIENT = 20;
    public static final int MSG_UNREGISTER_CLIENT = 21;
    public static final int MSG_SEND_NOTIFICATION = 22;

    private Timer timer;
    private TimerTask timerTask;

    private int currentStepsTableId;
    private int currentDaysTableId;
    private Days daysRecord;

    private



    final Messenger mMessenger = new Messenger(new IncomingHandler());
    ArrayList<Messenger> mClients = new ArrayList<>();

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

    private void sendMessageToUI() {
        for (int i = mClients.size() -1; i >= 0; i--) {
            try {
                mClients.get(i).send(Message.obtain(null, MSG_SEND_NOTIFICATION, 0));
            } catch (RemoteException e) {
                mClients.remove(i);
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;

        currentStepsTableId = 1;
        currentDaysTableId = 1;

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis() / 60000;
                checkMidnight(currentTime);
                Log.d("Current Time", currentTime + "");
        }
    };

    timer.scheduleAtFixedRate(timerTask, 0, 60000);
    Log.d("Timer", "Fires");
}




    public void checkMidnight(long currentTime) {
//        if (currentTime % (60 * 24) == 0) {
        if (currentTime % 3 == 0)
            Log.d("tick", "tock");
            sendMessageToUI();

//            daysRecord = mStepCounterView.endOfDaySave();
//            mStepCounterView.buildNotification(daysRecord.getStepsTaken());
//
//            // Builds new, empty database row when notification fires
//            SimpleDateFormat dateFormat = new SimpleDateFormat("MM / dd / yyyy", Locale.getDefault());
//            daysRecord = new Days(currentDaysTableId, 0, 0, 0, dateFormat.toString());
//            long daysRecord_id = mStepCounterView.createNewDBRows(daysRecord);
//            daysRecord.setId(daysRecord_id);

        }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

}
