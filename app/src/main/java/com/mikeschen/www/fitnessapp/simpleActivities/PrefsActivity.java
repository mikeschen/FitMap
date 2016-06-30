package com.mikeschen.www.fitnessapp.simpleActivities;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PrefsActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.homeEditText) EditText mHomeEditText;
    @Bind(R.id.workEditText) EditText mWorkEditText;
    @Bind(R.id.prefsButton) Button mPrefsButton;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);
        ButterKnife.bind(this);

        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("MYLABEL", "myStringToSave").commit();
        mPrefsButton.setOnClickListener(this);
        mContext = this;
    }

    @Override
    public void onClick(View v) {

    }
}
