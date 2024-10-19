package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btnNotifications);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.POST_NOTIFICATIONS) !=
                        PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions((MainActivity.this),
                            new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
                }
            }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // creation of applets
        NotificationApplet notificationApplet = new NotificationApplet(this);
        BatteryApplet batteryApplet = new BatteryApplet(this, notificationApplet);

        // initialization of trigger class
        AbstractTrigger<Float> batteryTrigger = new BatteryTriggerPluggedIn("batteryTrigger", 0.5f, batteryApplet);

        // initialization of response class
        AbstractResponse batteryResponse = new NotificationResponse("batteryResponse",
                "battery plugged in", notificationApplet);

        // initialization of batteryWorkflow
        Workflow batteryWorkflow = new Workflow(batteryResponse, batteryTrigger);

    }

//    public void makeNotification() {
//        String channelID = "CHANNEL_ID_NOTIFICATION";
//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(getApplicationContext(), channelID);
//        builder.setSmallIcon(R.drawable.ic_notifications)
//                .setContentTitle("Notification Title")
//                .setContentText("Some description")
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("data", "Some value to be passed");
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
//                0, intent, PendingIntent.FLAG_MUTABLE);
//
//        builder.setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
//            NotificationChannel notificationChannel =
//                    notificationManager.getNotificationChannel(channelID);
//            if(notificationChannel == null){
//                int importance = NotificationManager.IMPORTANCE_HIGH;
//                notificationChannel = new NotificationChannel(channelID,
//                        "Some description", importance);
//                notificationChannel.setLightColor(Color.GREEN);
//                notificationChannel.enableVibration(true);
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//
//
//        }
//
//        notificationManager.notify(0, builder.build());
//    }
//
//
//    public void disable(View view) {
//    }
}