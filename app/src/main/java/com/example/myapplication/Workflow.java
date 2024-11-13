package com.example.myapplication;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class Workflow {
    //instantiate a broadcast receiver


    protected AbstractResponse response;
    protected Applet app;


    public Workflow(Applet app, AbstractResponse response) {
        this.app = app;
        this.response = response;

    } //this is fine

    public abstract void registerReceiver();
    public abstract void handle(@Nullable Intent intent);


}
