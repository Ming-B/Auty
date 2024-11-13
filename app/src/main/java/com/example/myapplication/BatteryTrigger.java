package com.example.myapplication;

import android.content.Context;
import android.content.Intent;


public class BatteryTrigger extends AbstractTrigger {

    public BatteryTrigger(String triggerName, Workflow workflow) {
        super(triggerName, workflow);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.workflow.handle(intent);
    }

}
