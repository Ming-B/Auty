package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
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
import com.example.myapplication.models.DatabaseInit;
import com.example.myapplication.models.NotificationModel;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button button;
    Button logInButton;
    Button registerButton;
    Button deleteButton;
    Button listNotificationsButton;


    public static  String loggedInUser = null;
    UserModel userModel;
    DatabaseInit databaseInit;
    WorkflowModel workflowModel;
    NotificationModel notificationModel;

    ArrayList<Workflow> workflows;
    private boolean isReturned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        logInButton = findViewById(R.id.button_login);
        registerButton = findViewById(R.id.button_register);
        deleteButton = findViewById(R.id.button_delete);
        listNotificationsButton = findViewById(R.id.button_notifications);

        databaseInit = new DatabaseInit(this);
        userModel = new UserModel(databaseInit);
        workflowModel = new WorkflowModel(databaseInit);
        notificationModel = new NotificationModel(databaseInit);

//        SQLiteDatabase database = databasthis.userModel.getWritableDatabase();
//        Log.d("AUTY", "User database created or opened: " + database.getPath());

//        this.workflowModel = new WorkflowModel(this);
//        SQLiteDatabase wDatabase = this.workflowModel.();
//        Log.d("AUTY", "Workflow database created or opened: " + wDatabase.getPath());

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

        listNotificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListNotifications(notificationModel);
            }
        });


//        for (Workflow workflow: workflows){
//            workflow.registerReceiver();
//            Log.d("AUTY", String.format("Registered: %s", workflow.getWorkflowName() ));
//        }
////
//        for (Workflow workflow: workflows){
//            WorkflowConfig workflowConfig = new WorkflowConfig(workflow.getWorkflowName(), Boolean.TRUE);
//            if (workflowModel.addWorkflow(workflowConfig, loggedInUser)) {
//                System.out.println("Succesfully added workflow to DB");
//            } else {
//                System.out.println("Failed to add workflow to DB");
//            }
//
//        }

//        WorkflowConfig obtainedWF = wDb.getWorkflow("batteryWorkflow");
//        System.out.printf("Obtained WF: %s\n", obtainedWF.toString() );


        // Find the button and set a click listener
        Button buttonNavigate = findViewById(R.id.button_navigate);
        buttonNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("AUTY", "onResume started");
                if (loggedInUser != null) {
                    workflows = createWorkflowList();

                    Log.d("AUTY","The user has logged in");

                    int userID = userModel.getUserID(loggedInUser);

                    Map<Workflow, Boolean> userwWorkflows = getWorkflows(userID, workflows);
                    HashMap<String, Boolean> presentedWorkflows = new HashMap<>();

                    for (Workflow workflow: workflows) {
                        if (!userwWorkflows.containsKey(workflow)) {
                            presentedWorkflows.put(workflow.getWorkflowName(), Boolean.FALSE);
                        } else {
                            presentedWorkflows.put(workflow.getWorkflowName(), Boolean.TRUE);
                        }
                    }

                    DynamicListActivity.start(MainActivity.this, presentedWorkflows);
                    //
//                    Log.d("AUTY", "Opening dynamic activity 1");
//                    Intent intent = new Intent(MainActivity.this, DynamicListActivity.class);
//                    Log.d("AUTY", "Opening dynamic activity 2");
//                    intent.putExtra("userHashMap", presentedWorkflows);
//                    Log.d("AUTY", "Opening dynamic activity 3");
//                    startActivity(intent);

                } else {
                    Log.d("AUTY", "The user is not logged in");
                }
            }
        });

    }



//    private void addWorkflows(int userID, Workflow workflow) {
//        WorkflowConfig workflowConfig = new WorkflowConfig(workflow.getWorkflowName(), true);
//        workflowModel.addWorkflow(workflowConfig, userID);
//
//        Log.d("AUTY", "Added " + workflow.getWorkflowName());
//    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isReturned) {
            isReturned = false;
            // This method is called when the activity is resumed, meaning the user has returned to it.
            Toast.makeText(this, "Returned to MainActivity", Toast.LENGTH_SHORT).show();
//
            if (loggedInUser!=null) {


                workflows = createWorkflowList();

                Log.d("AUTY", "The user has logged in");

                int userID = userModel.getUserID(loggedInUser);

                Map<Workflow, Boolean> userwWorkflows = getWorkflows(userID, workflows);
                for (Map.Entry<Workflow, Boolean> entry : userwWorkflows.entrySet()) {
                    Workflow workflow = entry.getKey();
                    Boolean isActive = entry.getValue();

                    if (isActive) {
                        Log.d("AUTY", workflow.getWorkflowName());
                        if (workflow.isActive()){
                            workflow.registerReceiver(workflowModel, userID);
//                            workflow.setActive(Boolean.TRUE);
                            Log.d("AUTY", "Registered a receiver");
                        }
                    }
                    //            else {
                    //                Log.d("AUTY", "Unregistered a receiver");
                    //
                    //                workflow.unregisterReceiver(workflowModel,userID);
                    //            }

                    // Process each workflow and its corresponding boolean value
                }
            }
        }

    }
    // Method to mark that the user has returned from another activity
    public void onReturnToMain() {
        isReturned = true;
    }
    private ArrayList<Workflow> createWorkflowList(){
        int userID = userModel.getUserID(loggedInUser);

        NotificationResponse batteryChargingResponse = new NotificationResponse(this, "batteryChargingResponse", userID,  notificationModel);
        NotificationResponse batteryLowResponse = new NotificationResponse(this, "batteryLowResponse", userID, notificationModel);
        NotificationResponse wifiConnectedResponse = new NotificationResponse(this, "wifiConnectedResponse", userID, notificationModel);
        NotificationResponse bluetoothConnectedResponse = new NotificationResponse(this, "bluetoothConnectedResponse",userID, notificationModel);

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

    public void ListNotifications(NotificationModel notificationModel) {
        int userID = userModel.getUserID(loggedInUser);
        List<String> notifications = notificationModel.getNotifications(userID);

        // TODO: THIS IS JUST FOR HUSEYN
        String[] notificationsArray = notifications.toArray(new String[0]);

        Intent intent = new Intent(MainActivity.this, StringListActivity.class);
        intent.putExtra(StringListActivity.EXTRA_STRING_ARRAY, notificationsArray);
        startActivityForResult(intent, 1001);
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