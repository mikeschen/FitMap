package com.mikeschen.www.fitnessapp.main;


import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.maps.MapInterface;
import com.mikeschen.www.fitnessapp.maps.MapPresenter;
import com.mikeschen.www.fitnessapp.maps.MapsActivity;
import com.mikeschen.www.fitnessapp.utils.PermissionUtils;

import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends BaseActivity implements
        MainInterface.View,
        StepCounterInterface.View,
        View.OnClickListener {

    private boolean mPermissionDenied = false;
    private int caloriesBurned = 0;
    private String buttonDisplay;

    private Context mContext;
    private TipPresenter mTipPresenter;
    private StepCounterPresenter mStepCounterPresenter;

    @Bind(R.id.mainButton) Button mMainButton;
    @Bind(R.id.tipTextView) TextView mTipTextView;
    @Bind(R.id.tipsTextView) TextView mTipsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        buttonDisplay = "Calories";
        buttonDisplay = "Calories";
        mMainButton.setText("Calories Burned: " + caloriesBurned);
        mMainButton.setOnClickListener(this);
        mContext = this;

        mTipPresenter = new TipPresenter(this);
        mStepCounterPresenter = new StepCounterPresenter(this, mContext);

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

        mStepCounterPresenter.loadSteps();//This sets text in Steps Taken Button on start
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

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide action item
                getSupportActionBar().setTitle("");
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getSupportActionBar().setTitle("FitMap");
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
                    mStepCounterPresenter.loadSteps();
                } else if (buttonDisplay.equals("Steps")) {
                    buttonDisplay = "Calories";
                    mStepCounterPresenter.loadCalories();
                }
        }
    }

    @Override
    public void showSteps(int stepCount) {
        mMainButton.setText("Steps Taken: " + stepCount);
    }

    @Override
    public void showCalories(int caloriesBurned) {
        mMainButton.setText("Calories Burned: " + caloriesBurned);
    }

    @Override
    public void onPause() {
        mStepCounterPresenter.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void refresh() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}


