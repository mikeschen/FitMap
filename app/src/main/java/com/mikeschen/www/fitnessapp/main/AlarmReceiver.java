package com.mikeschen.www.fitnessapp.main;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.models.Days;
import com.mikeschen.www.fitnessapp.simpleActivities.RealStatsActivity;
import com.mikeschen.www.fitnessapp.utils.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Matt on 7/8/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private Context mContext;
    DatabaseHelper db;
    private NotificationCompat.Builder mBuilder;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("firing", "alarm");
        mContext = context;
//        Intent timerService = new Intent(context, TimerService.class);
//        context.startService(timerService);
        db = new DatabaseHelper(mContext);
        long currentTime = System.currentTimeMillis() / 60000;
        checkMidnight(currentTime);
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
        Log.d("currentTime", currentTime%(60*24)+"");
        Log.d("offsetTime", offsetFromGMT+"");

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

        mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_accessibility_white_24dp)
                .setContentTitle("FitMap Says: ")
                .setContentText("You walked "  + today.getStepsTaken() + " steps today!");

        Intent resultIntent = new Intent(mContext, RealStatsActivity.class);

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
}
