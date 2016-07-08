package com.mikeschen.www.fitnessapp.main;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
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
import com.mikeschen.www.fitnessapp.models.Days;
import com.mikeschen.www.fitnessapp.utils.DatabaseHelper;
import com.mikeschen.www.fitnessapp.utils.StepCounterService;
import com.mikeschen.www.fitnessapp.utils.TimerService;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends BaseActivity implements
        TipInterface.View,
        View.OnClickListener {

    private String buttonDisplay;
    private TipPresenter mTipPresenter;

    int weight;
    int stride;

    int images[] = {R.drawable.stairwellmain, R.drawable.back, R.drawable.graffiti, R.drawable.hall, R.drawable.blur};

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
                    daysRecord.setCaloriesBurned(db.getDay(daysRecord.getId()).getCaloriesBurned());
                    Log.d("MainBurned", "" + db.getDay(daysRecord.getId()).getCaloriesBurned());
                    if(buttonDisplay.equals("Steps")) {
                        mMainButton.setText("Steps Taken: " + (int) steps);
                    } else {
                        setCaloriesText();
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

        try {
            heightWeightDB.createDatabase();
        } catch (IOException e) {
            throw new Error ("Unable to create Database");
        }


        try {
            heightWeightDB.openDatabase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        if(relativeLayout != null)
            relativeLayout.setBackgroundResource(images[getRandomNumber()]);

        mMessenger = new Messenger(new IncomingHandler());

        buttonDisplay = "Steps";
        mMainButton.setOnClickListener(this);
        mTipPresenter = new TipPresenter(this);
        mTipTextView.setOnClickListener(this);

        db = new DatabaseHelper(mContext);

        List<Days> daysList = db.getAllDaysRecords();

        // This creates a table on first use of app
        if (daysList.size() == 0) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM / dd / yyyy", Locale.getDefault());
            String finalDate = dateFormat.format(date);
            daysRecord = new Days(1, 0, 0, 0, finalDate);
            mEditor.putString(Constants.PREFERENCES_CURRENT_DATE, dateFormat.toString());
            daysRecord.setId(db.logDays(daysRecord));
            db.updateDays(daysRecord);
            db.closeDB();
        } else {
            daysRecord = daysList.get(daysList.size()-1);
        }


        mMainButton.setText("Steps Taken: " + daysRecord.getStepsTaken());

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

        startService(new Intent(MainActivity.this, TimerService.class));
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

    public void setCaloriesText() {
        if(daysRecord.getCaloriesConsumed() > 0) {
            mMainButton.setText("Calories Consumed: " + (int) (daysRecord.getCaloriesConsumed() - daysRecord.getCaloriesBurned()));
        } else {
            mMainButton.setText("Calories Burned: " + (int) daysRecord.getCaloriesBurned());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.mainButton):
                Days today = db.getDay(daysRecord.getId());
                float steps = today.getStepsTaken();

                if (buttonDisplay.equals("Calories")) {
                    buttonDisplay = "Steps";
                    mMainButton.setText("Steps Taken: " + (int) steps);
                } else if (buttonDisplay.equals("Miles")) {
                    buttonDisplay = "Calories";
                    setCaloriesText();
                } else if (buttonDisplay.equals("Steps")) {
                    buttonDisplay = "Miles";
                    mMainButton.setText("Approx. Mileage: " + (double) Math.round(steps/2000 * 100d) / 100d);
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
            case (R.id.tipTextView):
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
                break;
        }
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
    protected void onDestroy() {
        super.onDestroy();

        try {
            doUnbindService();
        } catch (Throwable t) {
            Log.e("MainActivity", "Failed to unbind from the service", t);
        }
    }
}


