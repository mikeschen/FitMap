package com.mikeschen.www.fitnessapp.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.models.Days;
import com.mikeschen.www.fitnessapp.simpleActivities.RealStatsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {

    private NotificationCompat.Builder mBuilder;

    private static boolean isRunning;

    //All variables in Timer Service begin with 2
    public static final int MSG_REGISTER_CLIENT = 20;
    public static final int MSG_UNREGISTER_CLIENT = 21;

    private Timer timer;
    private TimerTask timerTask;

    DatabaseHelper db;


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

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        db = new DatabaseHelper(getApplicationContext());
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
    }


    public void checkMidnight(long currentTime) {
        TimeZone tz = TimeZone.getDefault();
        int offsetFromGMT = tz.getOffset(currentTime*60*1000);
        if(offsetFromGMT < 0) {
            offsetFromGMT = -offsetFromGMT;
        } else {
            offsetFromGMT = -offsetFromGMT + (24*60*60*1000);
        }
        offsetFromGMT = offsetFromGMT/1000/60;

        if (currentTime % (60 * 24) == offsetFromGMT) {

            //Finds most recent day in DB
            List<Days> allDays = db.getAllDaysRecords();
            Days today = allDays.get(allDays.size() -1);

            buildNotification(today);

            //Saves day's data to most recent day in DB
            db.updateDays(today);

            // Builds new, empty database row when notification fires and SHOULD create a new row with new ID
            long currentDaysTableId = today.getId() + 1;
            int currentDaysSteps;
            currentDaysSteps = 0;

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM / dd / yyyy", Locale.getDefault());
            String stringDate = dateFormat.format(date);

            Days newDay = new Days(currentDaysTableId, currentDaysSteps, 0, 0, stringDate);

            //This advances to the next key ID in the database and build a new table.
            newDay.setId(db.logDays(newDay));
            db.updateDays(newDay);
            db.deleteAllFoodRecords();

        }
    }

    public void buildNotification(Days today) {

        mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_accessibility_white_24dp)
                .setContentTitle("FitMap Says: ")
                .setContentText("You walked "  + today.getStepsTaken() + " steps today!");

        Intent resultIntent = new Intent(getApplicationContext(), RealStatsActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
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
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

}
