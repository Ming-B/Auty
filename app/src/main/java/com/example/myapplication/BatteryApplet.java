package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class BatteryApplet extends Applet{

    boolean isCharging;
    int batteryPercent;
    private NotificationApplet notificationApplet;

    public BatteryApplet(Context context, NotificationApplet notificationApplet) {
        super("BatteryApp", "config");
        this.notificationApplet = notificationApplet;
        registerBatteryReceiver(context);
    }


    public boolean status_call_plugged_in(){


        return isCharging;
    }

    public boolean status_call_low_battery(){

        return batteryPercent < 10 && batteryPercent != -1;
    }

    public void updateBatteryStatus(boolean isCharging, int batteryPercent){
        this.isCharging = isCharging;
        this.batteryPercent = batteryPercent;

        if(isCharging){
            notificationApplet.sendNotification("Battery", "Charging", "Battery is charging");

        }
        else if(batteryPercent < 20){
            notificationApplet.sendNotification("Battery", "Low", "Battery is low");
        }
    }

    private void registerBatteryReceiver(Context context) {
        BatteryStatusReceiver receiver = new BatteryStatusReceiver(this);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(receiver, filter);
    }

}
