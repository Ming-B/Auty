package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Toast;

import com.example.myapplication.applets.BatteryApplet;
import com.example.myapplication.applets.BluetoothApplet;
import com.example.myapplication.applets.WifiApplet;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserModel;
import com.example.myapplication.models.WorkflowConfig;
import com.example.myapplication.models.WorkflowModel;
import com.example.myapplication.responses.NotificationResponse;
import com.example.myapplication.workflows.BatteryLowWorkflow;
import com.example.myapplication.workflows.BatteryPluggedInWorkflow;
import com.example.myapplication.workflows.BluetoothConnectedWorkflow;
import com.example.myapplication.workflows.WifiWorkflow;
import com.example.myapplication.workflows.Workflow;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button button;
    Button logInButton;
    Button registerButton;
    Button deleteButton;

    String loggedInUser = null;
    UserModel userModel;
    WorkflowModel workflowModel;
    ArrayList<Workflow> workflows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        logInButton = findViewById(R.id.button_login);
        registerButton = findViewById(R.id.button_register);
        deleteButton = findViewById(R.id.button_delete);


        this.userModel = new UserModel(this);
        SQLiteDatabase database = this.userModel.getWritableDatabase();
        Log.d("AUTY", "User database created or opened: " + database.getPath());

        this.workflowModel = new WorkflowModel(this);
        SQLiteDatabase wDatabase = this.workflowModel.getWritableDatabase();
        Log.d("AUTY", "Workflow database created or opened: " + wDatabase.getPath());

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity(userModel);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity(userModel);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteActivity(userModel);
            }
        });

        workflows = createWorkflowList();
//
//        if (wDb.addWorkflow(workflowConfig)) {
//            System.out.println("Succesfully added workflow to DB");
//        } else {
//            System.out.println("Failed to add workflow to DB");
//        }
//
//        WorkflowConfig obtainedWF = wDb.getWorkflow("batteryWorkflow");
//        System.out.printf("Obtained WF: %s\n", obtainedWF.toString() );
        onStart();
    }

    protected void onStart() {
        super.onStart();

        Log.d("AUTY", "onResume started");
        if (loggedInUser != null) {
            Log.d("AUTY","The user has logged in");

            int userID = userModel.getUserID(loggedInUser);
            addWorkflows(userID, workflows.get(0));
            addWorkflows(userID, workflows.get(1));
            addWorkflows(userID, workflows.get(2));

            Map<Workflow, Boolean> userwWorkflows = getWorkflows(userID, workflows);


            for (Map.Entry<Workflow, Boolean> entry : userwWorkflows.entrySet()) {
                Workflow workflow = entry.getKey();
                Boolean status = entry.getValue();

                if (status) {
                    workflow.registerReceiver();
                    Log.d("AUTY", String.format("Registered: %s", workflow.getWorkflowName() ));
                }
            }

        } else {
            Log.d("AUTY", "The user is not logged in");
        }
    }

    private void addWorkflows(int userID, Workflow workflow) {
        WorkflowConfig workflowConfig = new WorkflowConfig(workflow.getWorkflowName(), true);
        workflowModel.addWorkflow(workflowConfig, userID);

        Log.d("AUTY", "Added " + workflow.getWorkflowName());
    }

    private ArrayList<Workflow> createWorkflowList(){
        NotificationResponse batteryChargingResponse = new NotificationResponse(this, "batteryChargingResponse");
        NotificationResponse batteryLowResponse = new NotificationResponse(this, "batteryLowResponse");
        NotificationResponse wifiConnectedResponse = new NotificationResponse(this, "wifiConnectedResponse");
        NotificationResponse bluetoothConnectedResponse = new NotificationResponse(this, "bluetoothConnectedResponse");

        BatteryApplet batteryApplet = new BatteryApplet();
        BluetoothApplet bluetoothApplet = new BluetoothApplet(this);
        WifiApplet wifiApplet = new WifiApplet(this);

        WifiWorkflow wifiWorkflow = new WifiWorkflow(this, wifiApplet, wifiConnectedResponse);
        BatteryPluggedInWorkflow batteryPluggedInWorkflow = new BatteryPluggedInWorkflow(this, batteryApplet, batteryChargingResponse);
        BatteryLowWorkflow batteryLowWorkflow = new BatteryLowWorkflow(this, batteryApplet, batteryLowResponse  );
        BluetoothConnectedWorkflow bluetoothConnectedWorkflow = new BluetoothConnectedWorkflow(this, bluetoothApplet, bluetoothConnectedResponse);

        ArrayList<Workflow> workflows = new ArrayList<>();
        workflows.add(wifiWorkflow);
        workflows.add(batteryPluggedInWorkflow);
        workflows.add(batteryLowWorkflow);
        workflows.add(bluetoothConnectedWorkflow);

        return workflows;
    }

    private @NonNull Map<Workflow, Boolean> getWorkflows(int userID, ArrayList<Workflow> workflows) {

        Map<String, Boolean> workflowConfigMappings = workflowModel.getWorkflowByUser(userID);
        Map<Workflow, Boolean> workflowMapping = new HashMap<>();

        for (Map.Entry<String, Boolean> entry : workflowConfigMappings.entrySet()) {
            String workflowName = entry.getKey();
            Boolean status = entry.getValue();
            Log.d("AUTY","WorkflowName: " + workflowName + ", Status: " + status);

            for (Workflow workflow: workflows) {
                if (Objects.equals(workflow.getWorkflowName(), workflowName)) {
                    workflowMapping.put(workflow, status);
                }
            }
        }
        return workflowMapping;

//        ArrayList<Workflow> activeWorkflows = new ArrayList<>();
//
//        for (WorkflowConfig workflowConfig: workflowConfigs) {
//            if (workflowConfig.getStatus()) {
//                for (Workflow workflow: workflows) {
//                    if (Objects.equals(workflow.getWorkflowName(), workflowConfig.getWorkflowName())) {
//                        activeWorkflows.add(workflow);
//                    }
//                }
//            }
//        }
//
//
//        return activeWorkflows;
    }

    public boolean DeleteActivity(UserModel userModel) {
        if (this.loggedInUser == null) {
            Log.d("AUTY","No logged in user to delete");
            return false;
        }

        userModel.deleteUser(new User(this.loggedInUser));
        Log.d("AUTY","Deleted logged in user");
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
        Log.d("AUTY",String.format("User is logged in %s", this.loggedInUser));
        return true;
    }

    public void RegisterActivity(UserModel userModel){
        EditText usernameField = findViewById(R.id.register_username);
        EditText passwordField = findViewById(R.id.register_password);
        EditText repeatPasswordField = findViewById(R.id.repeat_password);

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String repeatPassword = repeatPasswordField.getText().toString();

        Log.d("AUTY",String.format("Username: %s; Password: %s; RepeatPassword: %s", username, password, repeatPassword));

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