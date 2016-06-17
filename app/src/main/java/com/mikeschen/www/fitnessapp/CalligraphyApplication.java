package com.mikeschen.www.fitnessapp;


import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class CalligraphyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        // initalize Calligraphy
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lobster_1.3.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}

