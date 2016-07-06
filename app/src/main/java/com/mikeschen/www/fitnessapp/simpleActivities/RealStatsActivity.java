package com.mikeschen.www.fitnessapp.simpleActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.R;
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
    @Bind(R.id.andTextView) TextView mAndTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_stats);
        ButterKnife.bind(this);

        mSuggestionButton.setOnClickListener(this);


        List<Days> allDays = db.getAllDaysRecords();
        if (allDays == null) {
            Toast.makeText(RealStatsActivity.this, "Data no available", Toast.LENGTH_SHORT).show();
        } else {

            Days yesterday = allDays.get(allDays.size() - 2);
//            mCaloriesTextView.setText(yesterday.getCaloriesBurned() + "" + "cals");
//            mStepsTextView.setText(yesterday.getStepsTaken() + "" + "steps");
////        }

            mCaloriesTextView.setText(String.valueOf(yesterday.getCaloriesBurned()) + "cals");
            mStepsTextView.setText(String.valueOf(yesterday.getCaloriesBurned()) + "steps");
        }
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







