package com.example.myapplication.workflows;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myapplication.applets.Applet;
import com.example.myapplication.applets.BluetoothApplet;
import com.example.myapplication.models.WorkflowConfig;
import com.example.myapplication.models.WorkflowModel;
import com.example.myapplication.responses.AbstractResponse;
import com.example.myapplication.responses.NotificationResponse;
import com.example.myapplication.triggers.BluetoothTrigger;

public class BluetoothConnectedWorkflow extends Workflow {

    private BluetoothTrigger bluetoothTrigger;
    private NotificationResponse response;
    private BluetoothApplet app;

    private Context context;


    public BluetoothConnectedWorkflow(Context context, Applet applet, AbstractResponse response) {
        super("bluetoothConnectedWorkflow",applet, response);
        this.context = context;
        this.response = (NotificationResponse) response;
        this.app = (BluetoothApplet) applet;

//        this.registerReceiver();
    }

    @Override
    public void registerReceiver(WorkflowModel workflowModel, long user_id) {
        WorkflowConfig workflowConfig = new WorkflowConfig(this.workflowName, Boolean.TRUE);
        workflowModel.updateWorkflow(workflowConfig, user_id);
        Log.d("AUTY",String.format("Registered %s workflow", this.workflowName));

        this.bluetoothTrigger = new BluetoothTrigger(this);
//        BluetoothApplet.BluetoothStatusReceiver receiver = new BluetoothApplet.BluetoothStatusReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.context.registerReceiver(this.bluetoothTrigger, filter);
        Log.d("Applet", "Registering bluetooth status receiver");
    }

    @Override
    public void unregisterReceiver(WorkflowModel workflowModel, long user_id) {
        WorkflowConfig workflowConfig = new WorkflowConfig(this.workflowName, Boolean.FALSE);
        workflowModel.updateWorkflow(workflowConfig, user_id);
        Log.d("AUTY",String.format("Unregistered %s workflow", this.workflowName));
        context.unregisterReceiver(bluetoothTrigger);

    }
    @Override
    public void handle(@Nullable Intent intent) {
        if (intent != null) {
            if (this.app.isConnected(intent)) {
                this.response.respond( "Bluetooth device is connected");
            } else {
                this.response.respond("Bluetooth device is disconnected");
            }
        }
    }



}
