package com.mikeschen.www.fitnessapp;

import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.MapsInitializer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class MainActivityTest {
    private MainActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(MainActivity.class).create().get();
//        MapsInitializer.initialize(activity);
    }

    @Test
    public void validateTextViewContent() {
        TextView tipsTextView = (TextView) activity.findViewById(R.id.tipsTextView);
        assertTrue("Tips/Suggestions".equals(tipsTextView.getText().toString()));
    }
}
