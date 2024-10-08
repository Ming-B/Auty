package com.example.myapplication;

public class BatteryTrigger extends AbstractTrigger<Float>{
    private BatteryApplet batteryApplet;
    @Override
    public boolean handleService() {
        return batteryApplet.status_call();
    }

    public BatteryTrigger(String triggerName, float condition, BatteryApplet batteryApplet){
        super(triggerName, condition);
        this.batteryApplet = batteryApplet;
    }

}
