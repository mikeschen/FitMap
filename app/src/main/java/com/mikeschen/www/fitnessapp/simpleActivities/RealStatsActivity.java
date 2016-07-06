package com.mikeschen.www.fitnessapp.simpleActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

//    make a Day object
        List<Days> allDays = db.getAllDaysRecords();
        Days yesterday = allDays.get(allDays.size()- 2);

        mDateTextView.setText("Date: " + yesterday.getDate());
        mIdTextView.setText(String.valueOf("ID: " + yesterday.getId()));

        mCaloriesTextView.setText("Cal Burned: " + yesterday.getCaloriesBurned());

        mStepsTextView.setText("Steps: " + yesterday.getStepsTaken());

        mCalsConsumedTextView.setText("Cals Consumed:" + yesterday.getCaloriesConsumed());

        yesterday.getCaloriesBurned();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.suggestionButton):
                openMapDialog();
                break;
        }
    }
    private void openMapDialog() {
        LayoutInflater inflater = LayoutInflater.from(RealStatsActivity.this);
        View subView = inflater.inflate(R.layout.fragment_search_map, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add An Address (i.e. home or work)");
        builder.setView(subView);

        final EditText subEditText = (EditText) subView.findViewById(R.id.searchMapEditText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    String destination = subEditText.getText().toString();
                    Intent intent = new Intent(RealStatsActivity.this, MapsActivity.class);
                    intent.putExtra("destination", destination);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            });
            builder.show();
    }
}


//pull stuff from database to show steps and calories burned from yesterday







