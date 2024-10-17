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
        BatteryApplet batteryApplet = new BatteryApplet("BatteryApp");

        BatteryTriggerPluggedIn batteryTrigger = new BatteryTriggerPluggedIn("batteryTrigger", 0.5f, batteryApplet);
        // response class should be here called batteryResponse

        //Workflow batteryWorkflow = new Workflow(batteryResponse, batteryTrigger);


        setContentView(R.layout.activity_main);

    }


}