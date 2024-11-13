package com.example.myapplication.workflows;


import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.myapplication.applets.Applet;
import com.example.myapplication.responses.AbstractResponse;

public abstract class Workflow {

    protected AbstractResponse response;
    protected Applet app;

    public Workflow(Applet app, AbstractResponse response) {
        this.app = app;
        this.response = response;

    } //this is fine

    public abstract void registerReceiver();
    public abstract void handle(@Nullable Intent intent);


}
