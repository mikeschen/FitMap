package com.mikeschen.www.fitnessapp.main;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
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
import com.mikeschen.www.fitnessapp.MenuFragment;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.maps.MapsActivity;
import com.mikeschen.www.fitnessapp.models.Days;
import com.mikeschen.www.fitnessapp.utils.StepCounterService;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends BaseActivity implements
        MainInterface.View,
        StepCounterInterface.View,
        View.OnClickListener {

    private int caloriesBurned = 0;
    private String buttonDisplay;
    private TipPresenter mTipPresenter;
//    private StepCounterPresenter mStepCounterPresenter;
    private NotificationCompat.Builder mBuilder;
    int images[] = {R.drawable.alone, R.drawable.back, R.drawable.graffiti, R.drawable.hall, R.drawable.blur};
    Days daysRecord;

    Messenger mService = null;
    boolean mIsBound;
    Messenger mMessenger;
    @Bind(R.id.mainButton) Button mMainButton;
    @Bind(R.id.tipTextView) TextView mTipTextView;
    @Bind(R.id.tipsTextView) TextView mTipsTextView;
    @Bind(R.id.mainlayout) RelativeLayout relativeLayout;

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case StepCounterService.MSG_SET_STEP_COUNT_VALUE:
                    float steps = msg.arg1;
                    daysRecord.setStepsTaken(msg.arg1);
                    daysRecord.setCaloriesBurned(steps * 175/3500);
                    db.updateDays(daysRecord);
                    if(buttonDisplay.equals("Steps")) {
                        mMainButton.setText("Steps Taken: " + daysRecord.getStepsTaken());
                    } else {
                        mMainButton.setText("CaloriesBurned: " + (int) daysRecord.getCaloriesBurned());
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            try {
                Message msg = Message.obtain(null, StepCounterService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if(relativeLayout != null)
            relativeLayout.setBackgroundResource(images[getRandomNumber()]);

        mMessenger = new Messenger(new IncomingHandler());

        buttonDisplay = "Steps";
        mMainButton.setOnClickListener(this);

        mTipPresenter = new TipPresenter(this);
//        mStepCounterPresenter = new StepCounterPresenter(this);

        List<Days> daysList = db.getAllDaysRecords();


        // This creates a table on first use of app
        if (daysList.size() == 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM / dd / yyyy", Locale.getDefault());
            daysRecord = new Days(1, 0, 0, 0, dateFormat.toString());
            mEditor.putString(Constants.PREFERENCES_CURRENT_DATE, dateFormat.toString());
            daysRecord.setId(db.logDays(daysRecord));
            db.closeDB();
        } else {
            daysRecord = daysList.get(daysList.size()-1);
        }

        mMainButton.setText("Steps Taken: " + daysRecord.getStepsTaken());

        // Retrieves data when app is opened after crash/close and creates tables for each day app was not used
        long lastKnownTime = mSharedPreferences.getLong(Constants.PREFERENCES_LAST_KNOWN_TIME_KEY, 0);
        int lastKnownSteps = mSharedPreferences.getInt(Constants.PREFERENCES_LAST_KNOWN_STEPS_KEY, 0);
        long lastKnownId = mSharedPreferences.getLong(Constants.PREFERENCES_STEPS_ID_KEY, 0);
        int lastKnownCalories = lastKnownSteps * 175/3500;

        Log.d("lastKnownSteps", lastKnownSteps + "");

//        mStepCounterPresenter.checkDaysPassed(lastKnownSteps, lastKnownCalories, lastKnownTime, lastKnownId);

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

//        mStepCounterPresenter.loadSteps();//This sets text in Steps Taken Button on start

        startService(new Intent(MainActivity.this, StepCounterService.class));
        doBindService();
    }

    protected void onResume()
    {
        if(relativeLayout != null)
            relativeLayout.setBackgroundResource(images[getRandomNumber()]);
        super.onResume();
    }

    private int getRandomNumber() {
        return new Random().nextInt(5);
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
                    mMainButton.setText("Steps Taken: " + String.valueOf(db.getDay(daysRecord.getId()).getStepsTaken()));
                } else if (buttonDisplay.equals("Steps")) {
                    buttonDisplay = "Calories";
                    mMainButton.setText("Calories Burned: " + (int) db.getDay(daysRecord.getId()).getCaloriesBurned());
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
    public void showSteps(Days days) {
        mEditor.putInt(Constants.PREFERENCES_CURRENT_STEPS_KEY, days.getStepsTaken());
        mEditor.putFloat(Constants.PREFERENCES_CURRENT_CALORIES_BURNED_KEY, days.getCaloriesBurned());

        if (buttonDisplay.equals("Calories")) {
            mMainButton.setText("Calories Burned: " + days.getCaloriesBurned());
        } else if (buttonDisplay.equals("Steps")) {
            mMainButton.setText("Steps Taken: " + days.getStepsTaken());
        }
    }

    public void refresh() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void buildNotification(int steps) {
        mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_accessibility_white_24dp)
                .setContentTitle("My notification")
                .setContentText("You walked " + steps + " steps today!");

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM / dd / yyyy", Locale.getDefault());
        mEditor.putString(Constants.PREFERENCES_CURRENT_DATE, dateFormat.toString());
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

    void doBindService() {
        bindService(new Intent(this, StepCounterService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if(mIsBound) {
            if(mService != null) {
                try {
                    Message msg = Message.obtain(null, StepCounterService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch(RemoteException e) {
                    e.printStackTrace();
                }
            }
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    public void addToSharedPreferences(long time, int steps, long id) {
        mEditor.putLong(Constants.PREFERENCES_LAST_KNOWN_TIME_KEY, time).apply();
        mEditor.putInt(Constants.PREFERENCES_LAST_KNOWN_STEPS_KEY, steps).apply();
        mEditor.putLong(Constants.PREFERENCES_STEPS_ID_KEY, id).apply();
    }

    @Override
    public Days endOfDaySave() {
        int stepsTaken = mSharedPreferences.getInt(Constants.PREFERENCES_CURRENT_STEPS_KEY, 0);
        int caloriesBurned = mSharedPreferences.getInt(Constants.PREFERENCES_CURRENT_CALORIES_BURNED_KEY, 0);
        int caloriesConsumed = mSharedPreferences.getInt(Constants.PREFERENCES_CURRENT_CALORIES_CONSUMED_KEY, 0);
        String date = mSharedPreferences.getString(Constants.PREFERENCES_CURRENT_DATE, null);


        Days day = new Days(1, stepsTaken, caloriesBurned, caloriesConsumed, date);
        return day;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            doUnbindService();
        } catch (Throwable t) {
            Log.e("MainActivity", "Failed to unbind from the service", t);
        }
    }
}


