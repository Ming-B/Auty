package com.example.myapplication.triggers;

import android.content.BroadcastReceiver;

import com.example.myapplication.workflows.Workflow;

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
