package com.example.myapplication;

public class BatteryTriggerPluggedIn extends AbstractTrigger<Float>{
    final BatteryApplet batteryApplet;
    @Override
    public boolean handleService() {
        return batteryApplet.status_call_plugged_in();
    }

    public BatteryTriggerPluggedIn(String triggerName, float condition, BatteryApplet batteryApplet){
        super(triggerName, condition);
        this.batteryApplet = batteryApplet;
    }

}
