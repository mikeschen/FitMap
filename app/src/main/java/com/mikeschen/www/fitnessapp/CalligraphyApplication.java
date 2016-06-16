package com.mikeschen.www.fitnessapp;


import android.app.Application;
//import uk.co.chrisjenx.calligraphy.CalligraphyConfig;



public class CalligraphyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        // initalize Calligraphy
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/PTN77F.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}

