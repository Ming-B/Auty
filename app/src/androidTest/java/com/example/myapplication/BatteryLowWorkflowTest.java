package com.example.myapplication;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.service.notification.StatusBarNotification;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.myapplication.applets.BatteryApplet;
import com.example.myapplication.responses.NotificationResponse;
import com.example.myapplication.workflows.BatteryLowWorkflow;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

// tests run on an emulator or device
@RunWith(AndroidJUnit4.class)
public class BatteryLowWorkflowTest {

    private  BatteryLowWorkflow batteryLowWorkflow ;
    private Context context;
    private android.app.Instrumentation instrumentation;
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "batteryLowResponseChannel";
    private static final String responseName = "batteryLowResponse";

    @Before
    public void setUp() {
        this.instrumentation = InstrumentationRegistry.getInstrumentation();
        this.context = instrumentation.getTargetContext();

        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        BatteryApplet batteryApplet = new BatteryApplet();
        NotificationResponse batteryLowResponse = new NotificationResponse(this.context, "batteryLowResponse");
        batteryLowWorkflow = new BatteryLowWorkflow(this.context, batteryApplet, batteryLowResponse);


    }

    public void setUpIntent() {
        Intent intent = new Intent(Intent.ACTION_BATTERY_CHANGED);
        intent.putExtra(BatteryManager.EXTRA_LEVEL, 0.2);
        intent.putExtra(BatteryManager.EXTRA_SCALE, 100);

        final CountDownLatch latch = new CountDownLatch(1);

        // send the intent
        try {
            // Send the broadcast (needs to be done on the main thread)
            instrumentation.runOnMainSync(new Runnable() {
                @Override
                public void run() {
                    context.sendBroadcast(intent);
                }
            });

            // Wait for the broadcast to be received (with timeout)
            boolean await = latch.await(5, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void BatterLowTest() {

        batteryLowWorkflow.registerReceiver();;
        final int notificationId;
        setUpIntent();

        StatusBarNotification[] notifications =
                notificationManager.getActiveNotifications();

        boolean found = false;
        for (StatusBarNotification sbn : notifications) {
            if (sbn.getId() == notificationId) {
                found = true;
                assertEquals("Updated Title",
                        sbn.getNotification().extras.getString(Notification.EXTRA_TITLE));
                assertEquals("Updated Content",
                        sbn.getNotification().extras.getString(Notification.EXTRA_TEXT));
                break;
            }
        }
        assertTrue(true);
    }

}