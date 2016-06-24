package com.mikeschen.www.fitnessapp.simpleActivities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }
}
