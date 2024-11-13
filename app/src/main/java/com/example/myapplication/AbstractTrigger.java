package com.example.myapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public abstract class AbstractTrigger extends BroadcastReceiver {
    protected String triggerName; //name of specific trigger
    protected Workflow workflow;

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public AbstractTrigger(String triggerName, Workflow workflow){
        this.triggerName = triggerName;
        this.workflow = workflow;
    }

}
