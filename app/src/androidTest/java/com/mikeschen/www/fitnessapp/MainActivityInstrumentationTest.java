package com.mikeschen.www.fitnessapp;

import android.support.test.rule.ActivityTestRule;

import com.mikeschen.www.fitnessapp.main.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Ramon on 6/13/16.
 */
public class MainActivityInstrumentationTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void validateTextView() {
        onView(withId(R.id.tipsTextView)).check(matches(withText("Tips/Suggestions")));
    }

    @Test
    public void doNotClickMainButton() {
        onView(withId(R.id.mainButton)).check(matches(withText("Calories Burned: 200")));
    }

    @Test
    public void clickMainButton() {
        onView(withId(R.id.mainButton)).perform(click())
                .check(matches(withText("Steps Taken: 75")));
    }
}
