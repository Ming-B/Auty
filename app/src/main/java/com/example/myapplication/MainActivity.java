package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        Applet batteryApplet = new BatteryApplet();
        Applet notificationApplet = new NotificationApplet(this);

        AbstractTrigger<Float> batteryTrigger = new BatteryTriggerPluggedIn("batteryTrigger", 0.5f, batteryApplet);
        // response class should be here called batteryResponse
        AbstractResponse batteryResponse = new NotificationResponse("batteryResponse", "battery plugged in", notificationApplet);
        Workflow batteryWorkflow = new Workflow(batteryResponse, batteryTrigger);


        setContentView(R.layout.activity_main);

    }


}