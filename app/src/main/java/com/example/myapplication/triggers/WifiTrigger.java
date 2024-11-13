package com.example.myapplication.triggers;

import android.content.Context;
import android.content.Intent;

import com.example.myapplication.workflows.WifiWorkflow;

public class WifiTrigger extends AbstractTrigger {
    public WifiTrigger(WifiWorkflow wifiWorkflow) {
        super("wifiTrigger", wifiWorkflow);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.workflow.handle(null);
    }
}
