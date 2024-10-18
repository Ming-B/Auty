package com.example.myapplication;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            System.out.println("Perms granted");


        }

        Applet batteryApplet = new BatteryApplet();
        Applet notificationApplet = new NotificationApplet(this);

        AbstractTrigger<Float> batteryTrigger = new BatteryTriggerPluggedIn("batteryTrigger", 0.5f, batteryApplet);
        // response class should be here called batteryResponse
        AbstractResponse batteryResponse = new NotificationResponse("batteryResponse", "battery plugged in", notificationApplet);
        Workflow batteryWorkflow = new Workflow(batteryResponse, batteryTrigger);

        setContentView(R.layout.activity_main);


    }

    public void disable(View view) {
    }
}