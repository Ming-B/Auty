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
import android.widget.EditText;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button button;
    Button logInButton;
    Button registerButton;
    Button deleteButton;

    String loggedInUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        UserModel db = new UserModel(this);

        button = findViewById(R.id.btnNotifications);

        logInButton = findViewById(R.id.button_login);
        registerButton = findViewById(R.id.button_register);
        deleteButton = findViewById(R.id.button_delete);

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

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity(db);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity(db);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteActivity(db);
            }
        });

        NotificationApplet notificationApplet = new NotificationApplet(this);
        BatteryApplet batteryApplet = new BatteryApplet(this, notificationApplet);

        AbstractTrigger<Float> batteryTrigger = new BatteryTriggerPluggedIn("batteryTrigger", 0.5f, batteryApplet);
        // response class should be here called batteryResponse
        AbstractResponse batteryResponse = new NotificationResponse("batteryResponse",
                "battery plugged in", notificationApplet);
        Workflow batteryWorkflow = new Workflow(batteryResponse, batteryTrigger);

    }

    public boolean DeleteActivity(UserModel userModel) {
        if (this.loggedInUser == null) {
            System.out.println("No logged in user to delete");
            return false;
        }

        userModel.deleteUser(new User(this.loggedInUser));
        System.out.println("Deleted logged in user");
        return true;
    }

    public boolean LoginActivity(UserModel userModel){

        if (this.loggedInUser != null) {
            System.out.println("User already logged in");
            return false;
        }

        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        // Optionally, handle empty fields
        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
        }

        User loggingUser = new User(username, password);

        if (userModel.logIn(loggingUser)) {
            this.loggedInUser = username;

            Toast.makeText(this, "User logged in", Toast.LENGTH_SHORT).show();

        }
        System.out.println(String.format("User is logged in %s", this.loggedInUser));
        return true;
    }

    public void RegisterActivity(UserModel userModel){
        EditText usernameField = findViewById(R.id.register_username);
        EditText passwordField = findViewById(R.id.register_password);
        EditText repeatPasswordField = findViewById(R.id.repeat_password);

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String repeatPassword = repeatPasswordField.getText().toString();

        System.out.println(String.format("Username: %s; Password: %s; RepeatPassword: %s", username, password, repeatPassword));

        // Optionally, handle empty fields
        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
        }
        if (repeatPassword.isEmpty()) {
            Toast.makeText(this, "Repeat Password cannot be empty", Toast.LENGTH_SHORT).show();
        }

        User registeringUser = null;
        try {
            registeringUser = new User(username, password, repeatPassword);
        } catch (Exception e) {
            System.out.println("Exception occured during the registration");
            throw new RuntimeException(e);
        }

        if (userModel.register(registeringUser)) {
            Toast.makeText(this, "User registered", Toast.LENGTH_SHORT).show();
        }

    }

    public void disable(View view) {
    }
}