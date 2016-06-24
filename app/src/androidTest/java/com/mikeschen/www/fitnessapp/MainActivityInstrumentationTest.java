package com.mikeschen.www.fitnessapp;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.mikeschen.www.fitnessapp.main.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.regex.Pattern.matches;


/**
 * Created by Ramon on 6/13/16.
 */
//
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityInstrumentationTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

//    @Test
//    public void validateTextView() {
//        onView(withId(R.id.tipsTextView)).check(matches(withText("Tips/Suggestions")));
//    }
//
//    dummy data worked but not actually
//    @Test
//    public void doNotClickMainButton() {
//        onView(withId(R.id.mainButton)).check(matches(withText("Calories Burned: 0")));
//    }
//
//    @Test
//    public void clickMainButton() {
//        onView(withId(R.id.mainButton)).perform(click())
//                .check(matches(withText("Steps Taken: 0")));
//    }

//    ui test recorder in android studio if the new studio is implemented

//    @Test
//    public void doNotClickMainButton() {
//        onView(withId(R.id.mainButton)).check(matches(withText("Calories Burned: 0")));
//    }

//    @Test
//    public void clickMainButton() {
//        onView(withId(R.id.mainButton)).perform(click())
//                .check(matches(withText("Steps Taken: 0")));
//    }
}



