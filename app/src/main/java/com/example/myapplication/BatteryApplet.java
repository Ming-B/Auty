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

public class BatteryApplet extends  Applet{

    boolean isCharging;
    int batteryPercent;
    private NotificationApplet notificationApplet;

    public BatteryApplet(Context context, NotificationApplet notificationApplet) {
        super("BatteryApp", "config");
        this.notificationApplet = notificationApplet;
        registerBatteryReceiver(context);
    }


    public boolean status_call_plugged_in(){

//        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
//                isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
//            }
//        };
        if(isCharging) {
            notificationApplet.sendNotification("Battery", "Charging", "Battery is charging");

        }
        return true;
    }

    public boolean status_call_low_battery(){

//        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                batteryPercent = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
//            }
//        };
        if (batteryPercent < 20 && batteryPercent != -1) {
            notificationApplet.sendNotification("Battery", "Low", "Battery is low");

        }
        return true;
    }

    public void updateBatteryStatus(boolean isCharging, int batteryPercent){
        this.isCharging = isCharging;
        this.batteryPercent = batteryPercent;

        status_call_plugged_in();
        status_call_low_battery();

//        if(status_call_plugged_in()){
//            notificationApplet.sendNotification("Battery", "Charging", "Battery is charging");
//
//        }
//        else if(status_call_low_battery()){
//            notificationApplet.sendNotification("Battery", "Low", "Battery is low");
//        }
    }

    private void registerBatteryReceiver(Context context) {
        BatteryStatusReceiver receiver = new BatteryStatusReceiver(this);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(receiver, filter);
    }

}
