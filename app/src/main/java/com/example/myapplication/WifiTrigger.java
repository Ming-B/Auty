package com.example.myapplication;

import android.content.Context;
import android.content.Intent;

public class WifiTrigger extends AbstractTrigger {
    public WifiTrigger(WifiWorkflow wifiWorkflow) {
        super("wifiTrigger", wifiWorkflow);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.workflow.handle(null);
    }
}
