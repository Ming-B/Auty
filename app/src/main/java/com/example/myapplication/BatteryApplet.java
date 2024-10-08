package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;


public class BatteryApplet {

    boolean isCharging;



    String appName;

    public BatteryApplet(String appName) {
        this.appName = appName;
    }

    public void createApp(String appName, Object config){
        return;

    }

    public void listFunctionality(){
        return;
    }

    public boolean status_call(){

        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;

            }

        };
        return isCharging;
    }




}
