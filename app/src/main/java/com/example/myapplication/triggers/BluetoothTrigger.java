package com.example.myapplication.triggers;

import android.content.Context;
import android.content.Intent;

import com.example.myapplication.workflows.Workflow;

public class BluetoothTrigger extends AbstractTrigger {
    public BluetoothTrigger(Workflow workflow) {
        super("bluetoothTrigger", workflow);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.workflow.handle(intent);
    }
}
