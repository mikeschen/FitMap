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
    @Bind(R.id.calsConsumedTextView) TextView mCalsConsumedTextView;
    private Days yesterday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_stats);
        ButterKnife.bind(this);

        mSuggestionButton.setOnClickListener(this);

        List<Days> allDays = db.getAllDaysRecords();
        if (db.getAllDaysRecords().size() < 2) {
            Toast.makeText(RealStatsActivity.this, "No data available", Toast.LENGTH_SHORT).show();
        } else {
            yesterday = allDays.get(allDays.size() - 2);

            mDateTextView.setText("Date: " + yesterday.getDate());

            mCaloriesTextView.setText("Calories Burned: " + yesterday.getCaloriesBurned());

            mStepsTextView.setText("Steps Taken: " + yesterday.getStepsTaken());

            mCalsConsumedTextView.setText("Calories Consumed:" + yesterday.getCaloriesConsumed());
        }
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
        builder.setTitle("Enter An Address");
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







