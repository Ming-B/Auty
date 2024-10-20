package com.example.myapplication;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)  // Instrumented test that runs on an emulator or device
public class MainActivityTest {

    @Test
    public void testMainActivityLaunch() {
        // Launch the MainActivity
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            // Verify that the activity is launched by checking if it's not null
            scenario.onActivity(activity -> {
                assertNotNull(activity);  // Ensure the activity is launched
            });
        }
    }

    @Test
    public void testUIElementsDisplayed() {
        // Check if the TextView with ID 'hello' is displayed
        onView(withId(R.id.hello))  // Using the correct ID from activity_main.xml
                .check(matches(isDisplayed()));

        // Check if the Button with ID 'button' is displayed
        onView(withId(R.id.button))  // Using the correct ID from activity_main.xml
                .check(matches(isDisplayed()));
    }
}
