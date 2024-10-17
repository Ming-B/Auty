package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class BatteryApplet extends  Applet{

    boolean isCharging;
    int batteryPercent;

    public BatteryApplet() {
        super("BatteryApp", "config");
    }

//    public void createApp(String appName, Object config){
//        return;
//    }

    public boolean status_call_plugged_in(){

        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
            }
        };
        return isCharging;
    }

    public boolean status_call_low_battery(){

        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                batteryPercent = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            }
        };
        return batteryPercent < 10 && batteryPercent != -1;
    }

}
