package com.example.myapplication;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Workflow extends AppCompatActivity {
 //instantiate a broadcast receiver
 private BroadcastReceiver batteryReceiver; //should be in workflow

private AbstractTrigger trigger;
private AbstractResponse response;

    public Workflow(AbstractResponse response, AbstractTrigger trigger) {
        this.response = response;
        this.trigger = trigger;
    } //this is fine



    public void handle( ){

        String intentName = String.format("com.example.myapplication%s", trigger.getTriggerName());

        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intentName.equals(intent.getAction())) {
//
                    System.out.println("Intent is activated. Responding!");
                }

            }
        };

    }

    //custom intent for what the trigger what have
    //intent is the trigger

    // this on trigger
    @Override
    public void onStart(){
        super.onStart();
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, iFilter);
    }

    @Override
    public void onStop(){
        super.onStop();
        unregisterReceiver(batteryReceiver);
    }

}
