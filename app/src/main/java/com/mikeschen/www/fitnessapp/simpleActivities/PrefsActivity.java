package com.mikeschen.www.fitnessapp.simpleActivities;

import android.content.Context;
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
    @Bind(R.id.heightEditText) EditText mHeightEditText;
    @Bind(R.id.weightEditText) EditText mWeightEditText;
    @Bind(R.id.homePrefsButton) Button mHomePrefsButton;
    @Bind(R.id.workPrefsButton) Button mWorkPrefsButton;
    @Bind(R.id.heightPrefsButton) Button mHeighPrefsButton;
    @Bind(R.id.weightPrefsButton) Button mWeightPrefsButton;

    private String home;
    private String work;
    private String height;
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
        height = mSharedPreferences.getString(Constants.PREFERENCES_HEIGHT, null);
        mHeightEditText.setText(height);
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
                String height = mHeightEditText.getText().toString();
                if(height.isEmpty()) {
                    Toast.makeText(PrefsActivity.this, "Please enter your height", Toast.LENGTH_SHORT).show();
                    return;
                }
                addHeightToSharedPreferences(height);
                Toast.makeText(PrefsActivity.this, "Height Saved!", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.weightPrefsButton):
                String weight = mHeightEditText.getText().toString();
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
    private void addHeightToSharedPreferences(String work) {
        mEditor.putString(Constants.PREFERENCES_HEIGHT, height).apply();
    }
    private void addWeightToSharedPreferences(String work) {
        mEditor.putString(Constants.PREFERENCES_WEIGHT, weight).apply();
    }
}
