package com.mikeschen.www.fitnessapp;

import android.widget.TextView;

import com.mikeschen.www.fitnessapp.main.MainActivity;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class MainActivityTest {
    private MainActivity activity;

    @Before
    public void setUp() {
//        MapsInitializer.initialize(activity);
    }

    @Test
    public void validateTextViewContent() {
        TextView tipsTextView = (TextView) activity.findViewById(R.id.tipsTextView);
        assertTrue("Tips/Suggestions".equals(tipsTextView.getText().toString()));
    }
}
