package com.mikeschen.www.fitnessapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.Bind;


public class AboutActivity extends AppCompatActivity {
    @Bind(R.id.aboutTextView) TextView mAboutTextView;
    @Bind(R.id.benefitsTextView) TextView mBenefitsTextView;
    @Bind(R.id.creationTextView) TextView mCreationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //add background picture on the about page- use learn how to program -DONE
        //create two strings to getText/setText to display the creation of the app and the benefits of passively working out
        //look for a different picture to be set as a background
        //try to write JUnit tests at list for the About page



    }
}
