package com.mikeschen.www.fitnessapp.simpleActivities;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PrefsActivity extends BaseActivity {
    @Bind(R.id.homeEditText) EditText mHomeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);
        setTitle("Preferences");
        ButterKnife.bind(this);

        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("MYLABEL", "myStringToSave").commit();
    }
}
