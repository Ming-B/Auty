package com.example.myapplication.workflows;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myapplication.applets.BatteryApplet;
import com.example.myapplication.responses.AbstractResponse;
import com.example.myapplication.responses.NotificationResponse;
import com.example.myapplication.triggers.BatteryTrigger;

public class MockBatteryLowWorkflow extends Workflow {
    private Context context;
    private BatteryTrigger batteryTrigger;
    private NotificationResponse response;
    private BatteryApplet app;

    public static final String ACTION_BATTERY_CHANGED = "mock.intent.action.BATTERY_CHANGED";

    public MockBatteryLowWorkflow(Context context, BatteryApplet batteryApplet, AbstractResponse notificationApplet) {
        super("mockBatteryWorkflow", batteryApplet, notificationApplet);
        this.context = context;
        this.app = batteryApplet;
        this.response = (NotificationResponse) notificationApplet;
    }

    @Override
    public void registerReceiver() {
        this.batteryTrigger = new BatteryTrigger("mockBatteryTrigger", this);
        CustomIntentFilter filter = new CustomIntentFilter(ACTION_BATTERY_CHANGED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.registerReceiver(this.batteryTrigger, filter, Context.RECEIVER_EXPORTED);
        }
    }

    @Override
    public void handle(@Nullable Intent intent) {
        if (intent != null) {
            if (this.app.status_call_low_battery(intent)) {
                response.respond("Battery is low");
            }
        } else {
            response.respond("Missing intent in the handler");
        }
    }

    // Helper method to simulate battery events for testing
    public void simulateBatteryEvent(int level, boolean isCharging) {
        CustomIntent intent = new CustomIntent(ACTION_BATTERY_CHANGED);
        intent.putExtra("level", level);
        intent.putExtra("isCharging", isCharging);
        context.sendBroadcast(intent);
    }
}