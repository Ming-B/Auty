package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

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

        ArrayList<Workflow> workflows = getWorkflows();

        for (Workflow workflow: workflows){
            workflow.registerReceiver();
        }

    }

    private @NonNull ArrayList<Workflow> getWorkflows() {
        NotificationResponse batteryChargingResponse = new NotificationResponse(this, "batteryChargingResponse");
        NotificationResponse batteryLowResponse = new NotificationResponse(this, "batteryLowResponse");
        NotificationResponse wifiConnectedResponse = new NotificationResponse(this, "wifiConnectedResponse");
        NotificationResponse bluetoothConntectedResponse = new NotificationResponse(this, "bluetoothConnectedResponse");

        BatteryApplet batteryApplet = new BatteryApplet();
        BluetoothApplet bluetoothApplet = new BluetoothApplet();
        WifiApplet wifiApplet = new WifiApplet(this);

        WifiWorkflow wifiWorkflow = new WifiWorkflow(this, wifiApplet, wifiConnectedResponse);
        BatteryPluggedInWorkflow batteryPluggedInWorkflow = new BatteryPluggedInWorkflow(this, batteryApplet, batteryChargingResponse);
        BatteryLowWorkflow batteryLowWorkflow = new BatteryLowWorkflow(this, batteryApplet, batteryLowResponse);
        BluetoothConnectedWorkflow bluetoothConnectedWorkflow = new BluetoothConnectedWorkflow(this, bluetoothApplet, bluetoothConntectedResponse);

        ArrayList<Workflow> workflows = new ArrayList<>();
    

        workflows.add(wifiWorkflow);
        workflows.add(batteryPluggedInWorkflow);
        workflows.add(batteryLowWorkflow);
        workflows.add(bluetoothConnectedWorkflow);
        return workflows;
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

}