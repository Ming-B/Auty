package com.example.myapplication;

import android.content.Intent;
import android.os.BatteryManager;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)  // This is an instrumented test that runs on an emulator or device
public class BatteryAppletTest {

    private BatteryApplet batteryApplet;

    @Before
    public void setUp() {
        // Initialize the BatteryApplet object
        batteryApplet = new BatteryApplet("BatteryApp");
    }

    @Test
    public void testStatusCallPluggedIn_Charging() {
        // Simulate the intent for charging status
        Intent batteryIntent = new Intent(Intent.ACTION_BATTERY_CHANGED);
        batteryIntent.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);

        // Broadcast the intent and test if the status is correctly identified as charging
        ApplicationProvider.getApplicationContext().sendBroadcast(batteryIntent);
        assertTrue("The device should be charging", batteryApplet.status_call_plugged_in());
    }

    @Test
    public void testStatusCallPluggedIn_NotCharging() {
        // Simulate the intent for not charging status
        Intent batteryIntent = new Intent(Intent.ACTION_BATTERY_CHANGED);
        batteryIntent.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_NOT_CHARGING);

        // Broadcast the intent and test if the status is correctly identified as not charging
        ApplicationProvider.getApplicationContext().sendBroadcast(batteryIntent);
        assertFalse("The device should not be charging", batteryApplet.status_call_plugged_in());
    }

    @Test
    public void testStatusCallLowBattery_LowBattery() {
        // Simulate the intent for low battery status
        Intent batteryIntent = new Intent(Intent.ACTION_BATTERY_CHANGED);
        batteryIntent.putExtra(BatteryManager.EXTRA_LEVEL, 5);  // Set battery percentage to 5%

        // Broadcast the intent and test if the battery is identified as low
        ApplicationProvider.getApplicationContext().sendBroadcast(batteryIntent);
        assertTrue("The battery should be low", batteryApplet.status_call_low_battery());
    }

    @Test
    public void testStatusCallLowBattery_NotLowBattery() {
        // Simulate the intent for sufficient battery status
        Intent batteryIntent = new Intent(Intent.ACTION_BATTERY_CHANGED);
        batteryIntent.putExtra(BatteryManager.EXTRA_LEVEL, 50);  // Set battery percentage to 50%

        // Broadcast the intent and test if the battery is identified as sufficient
        ApplicationProvider.getApplicationContext().sendBroadcast(batteryIntent);
        assertFalse("The battery should not be low", batteryApplet.status_call_low_battery());
    }
}
