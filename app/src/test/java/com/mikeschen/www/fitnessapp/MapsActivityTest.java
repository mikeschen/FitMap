package com.mikeschen.www.fitnessapp;

import android.os.Build;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)

public class MapsActivityTest {
    private MapsActivity activity;

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(MapsActivity.class);
    }
}
