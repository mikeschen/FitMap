package com.mikeschen.www.fitnessapp.simpleActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.main.TipPresenter;
import com.mikeschen.www.fitnessapp.maps.MapsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RealStatsActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.suggestionButton) Button mSuggestionButton;
    @Bind(R.id.caloriesTextView) TextView mCaloriesTextView;
    @Bind(R.id.stepsTextView) TextView mStepsTextView;
    @Bind(R.id.dateTextView) TextView mDateTextView;

    private TipPresenter mTipPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_stats);
        ButterKnife.bind(this);

        mSuggestionButton.setOnClickListener(this);

    //make a Day object
//        List<Days> allDays = db.getAllDaysRecords();
//        Days yesterday = allDays.get(allDays.size()- 2);
//        if (allDays.isEmpty()) {
//            Toast.makeText(RealStatsActivity.this, "Data no available", Toast.LENGTH_SHORT).show();
//        } else {
//
//            mCaloriesTextView.setText(yesterday.getCaloriesBurned() + "" + "cals");
//
//            mStepsTextView.setText(yesterday.getStepsTaken() + "" + "steps");
//        }
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







