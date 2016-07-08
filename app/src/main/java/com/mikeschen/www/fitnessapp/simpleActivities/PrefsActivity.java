package com.mikeschen.www.fitnessapp.simpleActivities;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.Constants;
import com.mikeschen.www.fitnessapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PrefsActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.homeEditText) EditText mHomeEditText;
    @Bind(R.id.workEditText) EditText mWorkEditText;
    @Bind(R.id.heightFeetEditText) EditText mHeightFeetEditText;
    @Bind(R.id.heightInchesEditText) EditText mHeightInchesEditText;
    @Bind(R.id.weightEditText) EditText mWeightEditText;
    @Bind(R.id.homePrefsButton) Button mHomePrefsButton;
    @Bind(R.id.workPrefsButton) Button mWorkPrefsButton;
    @Bind(R.id.heightPrefsButton) Button mHeighPrefsButton;
    @Bind(R.id.weightPrefsButton) Button mWeightPrefsButton;

    private String home;
    private String work;
    private String feet;
    private String inches;
    private String weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);
        ButterKnife.bind(this);

        mHomePrefsButton.setOnClickListener(this);
        mWorkPrefsButton.setOnClickListener(this);
        mHeighPrefsButton.setOnClickListener(this);
        mWeightPrefsButton.setOnClickListener(this);

        home = mSharedPreferences.getString(Constants.PREFERENCES_HOME, null);
        mHomeEditText.setText(home);
        work = mSharedPreferences.getString(Constants.PREFERENCES_WORK, null);
        mWorkEditText.setText(work);
        feet = mSharedPreferences.getString(Constants.PREFERENCES_FEET, null);
        mHeightFeetEditText.setText(feet);
        inches = mSharedPreferences.getString(Constants.PREFERENCES_INCHES, null);
        mHeightInchesEditText.setText(inches);
        weight = mSharedPreferences.getString(Constants.PREFERENCES_WEIGHT, null);
        mWeightEditText.setText(weight);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.homePrefsButton):
                String home = mHomeEditText.getText().toString();
                if (home.isEmpty()) {
                    Toast.makeText(PrefsActivity.this, "Please Enter A Home Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                addHomeToSharedPreferences(home);
                Toast.makeText(PrefsActivity.this, "Home Location Saved!", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.workPrefsButton):
                String work = mWorkEditText.getText().toString();
                if (work.isEmpty()) {
                    Toast.makeText(PrefsActivity.this, "Please Enter A Work Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                addWorkToSharedPreferences(work);
                Toast.makeText(PrefsActivity.this, "Work Location Saved!", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.heightPrefsButton):

                String feet = mHeightFeetEditText.getText().toString();
                String inches = mHeightInchesEditText.getText().toString();

                if(feet.isEmpty() || inches.isEmpty() || feet.isEmpty() && inches.isEmpty()) {
                    Toast.makeText(PrefsActivity.this, "Please enter your height", Toast.LENGTH_SHORT).show();
                    return;
                }

                int intFeet = Integer.parseInt(feet);
                int intInches = Integer.parseInt(inches);
                if(intInches > 12) {
                    Toast.makeText(PrefsActivity.this, "Please enter 12 inches or less", Toast.LENGTH_SHORT).show();
                    return;
                }
                int height = (intFeet * 12) + intInches;

                addFeetToSharedPreferences(feet);
                addInchesToSharedPreferences(inches);
                addHeightToSharedPreferences(height);
                Toast.makeText(PrefsActivity.this, "Height Saved!", Toast.LENGTH_SHORT).show();
                break;

            case (R.id.weightPrefsButton):
                String weight = mWeightEditText.getText().toString();
                if(weight.isEmpty()) {
                    Toast.makeText(PrefsActivity.this, "Please enter your weight", Toast.LENGTH_SHORT).show();
                    return;
                }

                addWeightToSharedPreferences(weight);
                Toast.makeText(PrefsActivity.this, "Weight Saved!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void addHomeToSharedPreferences(String home) {
        mEditor.putString(Constants.PREFERENCES_HOME, home).apply();
    }

    private void addWorkToSharedPreferences(String work) {
        mEditor.putString(Constants.PREFERENCES_WORK, work).apply();
    }
    private void addHeightToSharedPreferences(int height) {
        mEditor.putInt(Constants.PREFERENCES_HEIGHT, height).apply();
    }
    private void addFeetToSharedPreferences(String feet) {
        mEditor.putString(Constants.PREFERENCES_FEET, feet).apply();
    }
    private void addInchesToSharedPreferences(String inches) {
        mEditor.putString(Constants.PREFERENCES_INCHES, inches).apply();
    }
    private void addWeightToSharedPreferences(String weight) {
        mEditor.putString(Constants.PREFERENCES_WEIGHT, weight).apply();
    }
}
