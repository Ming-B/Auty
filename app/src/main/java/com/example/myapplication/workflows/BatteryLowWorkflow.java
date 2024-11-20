package com.example.myapplication.workflows;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myapplication.applets.BatteryApplet;
import com.example.myapplication.models.WorkflowConfig;
import com.example.myapplication.models.WorkflowModel;
import com.example.myapplication.responses.AbstractResponse;
import com.example.myapplication.responses.NotificationResponse;
import com.example.myapplication.triggers.BatteryTrigger;

public class BatteryLowWorkflow extends  Workflow{

    private Context context;
    private BatteryTrigger batteryTrigger;
    private NotificationResponse response;
    private BatteryApplet app;

    public BatteryLowWorkflow(Context context, BatteryApplet batteryApplet, AbstractResponse notificationApplet) {
        super("batteryLowWorkflow",batteryApplet, notificationApplet);
        this.context = context;
        this.app = batteryApplet;
        this.response = (NotificationResponse) notificationApplet;

//        this.registerReceiver();
    }

    @Override
    public void registerReceiver(WorkflowModel workflowModel, long user_id) {
        WorkflowConfig workflowConfig = new WorkflowConfig(this.workflowName, Boolean.TRUE);
        workflowModel.updateWorkflow(workflowConfig, user_id);
        Log.d("AUTY",String.format("Registered %s workflow", this.workflowName));

        this.batteryTrigger = new BatteryTrigger("batteryLowTrigger", this);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(this.batteryTrigger, filter);
    }

    @Override
    public void unregisterReceiver(WorkflowModel workflowModel, long user_id) {
        WorkflowConfig workflowConfig = new WorkflowConfig(this.workflowName, Boolean.FALSE);
        workflowModel.updateWorkflow(workflowConfig, user_id);
        Log.d("AUTY",String.format("Unregistered %s workflow", this.workflowName));

        context.unregisterReceiver(batteryTrigger);

    }

    @Override
    public void handle(@Nullable Intent intent) {
        if (intent != null) {
            if (this.app.status_call_low_battery(intent)) {
                response.respond("Battery is low");
            }
        } else {
            response.respond("Missing intent in the handler");
        }
    }
}
