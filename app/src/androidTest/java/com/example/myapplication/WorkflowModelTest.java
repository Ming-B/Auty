package com.example.myapplication;

import android.content.Context;




import com.example.myapplication.applets.BatteryApplet;
import com.example.myapplication.applets.BluetoothApplet;
import com.example.myapplication.applets.WifiApplet;
import com.example.myapplication.models.DatabaseInit;
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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import androidx.test.core.app.ApplicationProvider;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorkflowModelTest {
    private WorkflowModel workflowModel;
    private DatabaseInit dbInit;
    private Context context;
    private int user_id;

    private int registerUser(UserModel userModel) {
        String username = "username";
        String password = "password";
        String repeatedPassword = "password";

        try {
            User registering_user = new User(username, password, repeatedPassword);
            userModel.register(registering_user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userModel.getUserID(username);
    }

    @Before
    public void createDB() {

        context = ApplicationProvider.getApplicationContext();

        dbInit = new DatabaseInit(context, ":memory:");
        workflowModel = new WorkflowModel(dbInit);

        UserModel userModel = new UserModel(dbInit);
        user_id = registerUser(userModel);
    }

    private ArrayList<Workflow> createWorkflowList() {
        NotificationResponse batteryChargingResponse = new NotificationResponse(context, "batteryChargingResponse");
        NotificationResponse batteryLowResponse = new NotificationResponse(context, "batteryLowResponse");
        NotificationResponse wifiConnectedResponse = new NotificationResponse(context, "wifiConnectedResponse");
        NotificationResponse bluetoothConnectedResponse = new NotificationResponse(context, "bluetoothConnectedResponse");

        BatteryApplet batteryApplet = new BatteryApplet();

        BatteryPluggedInWorkflow batteryPluggedInWorkflow = new BatteryPluggedInWorkflow(context, batteryApplet, batteryChargingResponse);
        BatteryLowWorkflow batteryLowWorkflow = new BatteryLowWorkflow(context, batteryApplet, batteryLowResponse);

        ArrayList<Workflow> workflows = new ArrayList<>();
        workflows.add(batteryPluggedInWorkflow);
        workflows.add(batteryLowWorkflow);

        return workflows;
    }

    @Test
    public void addWorkflowTest() throws Exception {
        ArrayList<Workflow> createdWorkflows = createWorkflowList();

        Workflow addedWorkflow = createdWorkflows.get(0);

        WorkflowConfig workflowConfig = new WorkflowConfig(addedWorkflow.getWorkflowName(), true);
        boolean first_status = workflowModel.addWorkflow(workflowConfig, user_id);
        boolean second_status = workflowModel.addWorkflow(workflowConfig, user_id);
        assertNotEquals(first_status, second_status);
    }

    @Test
    public void getWorkflowTest() throws Exception {
        ArrayList<Workflow> createdWorkflows = createWorkflowList();

        Workflow addedWorkflow = createdWorkflows.get(1);

        // try to obtain workflow before adding
        WorkflowConfig obtainedWorkflowConfig = workflowModel.getWorkflow(addedWorkflow.getWorkflowName());
        assertNotNull(obtainedWorkflowConfig);

        WorkflowConfig addedWorkflowConfig = new WorkflowConfig(addedWorkflow.getWorkflowName(), true);
        boolean status = workflowModel.addWorkflow(addedWorkflowConfig, user_id);
        obtainedWorkflowConfig = workflowModel.getWorkflow(addedWorkflow.getWorkflowName());

        // get workflow after adding
        assertNotNull(obtainedWorkflowConfig);
        assertEquals(addedWorkflowConfig.getWorkflowName(), obtainedWorkflowConfig.getWorkflowName());
        assertEquals(addedWorkflowConfig.getStatus(), obtainedWorkflowConfig.getStatus());
    }

    @Test
    public void getWorkflowByUserTest() throws Exception {
        ArrayList<Workflow> createdWorkflows = createWorkflowList();

        Workflow addedWorkflow = createdWorkflows.get(1);

        WorkflowConfig addedWorkflowConfig = new WorkflowConfig(addedWorkflow.getWorkflowName(), true);
        boolean status = workflowModel.addWorkflow(addedWorkflowConfig, user_id);
        Map<String, Boolean> obtainedWorkflowConfig = workflowModel.getWorkflowByUser(user_id);

        assertNotNull(obtainedWorkflowConfig);
        for (Map.Entry<String, Boolean> entry : obtainedWorkflowConfig.entrySet()) {
            String workflowName = entry.getKey();
            Boolean workflowStatus = entry.getValue();
            assertEquals(addedWorkflowConfig.getWorkflowName(), workflowName);
            assertEquals(addedWorkflowConfig.getStatus(), workflowStatus);
        }
    }

    @Test
    public void getWorkflowsTest() throws  Exception{
        ArrayList<Workflow> createdWorkflows = createWorkflowList();

        for( Workflow workflow : createdWorkflows) {
            WorkflowConfig addedWorkflowConfig = new WorkflowConfig(workflow.getWorkflowName(), true);
            boolean status = workflowModel.addWorkflow(addedWorkflowConfig, user_id);
        }

        ArrayList<WorkflowConfig> obtainedWorkflows = workflowModel.getWorkflows();

        assertNotNull(obtainedWorkflows);
        assertEquals(createdWorkflows.size(), obtainedWorkflows.size());
    }
}