package com.mikeschen.www.fitnessapp.simpleActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.main.TipPresenter;
import com.mikeschen.www.fitnessapp.maps.MapsActivity;
import com.mikeschen.www.fitnessapp.models.Days;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RealStatsActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.suggestionButton) Button mSuggestionButton;
    @Bind(R.id.caloriesTextView) TextView mCaloriesTextView;
    @Bind(R.id.stepsTextView) TextView mStepsTextView;
    @Bind(R.id.dateTextView) TextView mDateTextView;
    @Bind(R.id.idTextView) TextView mIdTextView;
    @Bind(R.id.calsConsumedTextView) TextView mCalsConsumedTextView;

    private TipPresenter mTipPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_stats);
        ButterKnife.bind(this);

        mSuggestionButton.setOnClickListener(this);

    //make a Day object
        List<Days> allDays = db.getAllDaysRecords();
        Days yesterday = allDays.get(allDays.size()- 2);

        mDateTextView.setText("Date: " + yesterday.getDate());
        mIdTextView.setText(String.valueOf("ID: " + yesterday.getId()));

        mCaloriesTextView.setText("Cal Burned: " + yesterday.getCaloriesBurned());

        mStepsTextView.setText("Steps: " + yesterday.getStepsTaken());

        mCalsConsumedTextView.setText("Cals Consumed:" + yesterday.getCaloriesConsumed());

//        yesterday.getCaloriesBurned();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.suggestionButton):
                Intent intent = new Intent(RealStatsActivity.this, MapsActivity.class);
                startActivity(intent);
                break;

        }
    }
}


//pull stuff from database to show steps and calories burned from yesterday







