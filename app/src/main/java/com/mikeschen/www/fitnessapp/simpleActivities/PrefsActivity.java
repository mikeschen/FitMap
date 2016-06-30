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
    @Bind(R.id.homePrefsButton) Button mHomePrefsButton;
    @Bind(R.id.workPrefsButton) Button mWorkPrefsButton;

    private String home;
    private String work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);
        ButterKnife.bind(this);

        mHomePrefsButton.setOnClickListener(this);
        mWorkPrefsButton.setOnClickListener(this);

        home = mSharedPreferences.getString(Constants.PREFERENCES_HOME, null);
        mHomeEditText.setText(home);
        work = mSharedPreferences.getString(Constants.PREFERENCES_WORK, null);
        mWorkEditText.setText(work);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.homePrefsButton):
                String home = mHomeEditText.getText().toString();
                addHomeToSharedPreferences(home);
                Toast.makeText(PrefsActivity.this, "Home Location Saved!", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.workPrefsButton):
                String work = mWorkEditText.getText().toString();
                addWorkToSharedPreferences(work);
                Toast.makeText(PrefsActivity.this, "Work Location Saved!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void addHomeToSharedPreferences(String home) {
        mEditor.putString(Constants.PREFERENCES_HOME, home).apply();
    }

    private void addWorkToSharedPreferences(String work) {
        mEditor.putString(Constants.PREFERENCES_WORK, work).apply();
    }
}
