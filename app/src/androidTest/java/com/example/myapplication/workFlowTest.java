package com.example.myapplication;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

// test runs on emulator or device
@RunWith(AndroidJUnit4.class)  
public class workFlowTest {

    private Workflow workflow;
    private AbstractTrigger mockTrigger;
    private AbstractResponse mockResponse;

    @Before
    public void setUp() {
        // Set up context for the test 
        Context context = ApplicationProvider.getApplicationContext();

        // Create mock trigger and response
        mockTrigger = new AbstractTrigger("BatteryTrigger", 0.5f) {
            @Override
            public boolean handleService() {
                return true;
            }
        };

        mockResponse = new AbstractResponse() {
            @Override
            public void respond() {
                // Simulate response behavior
            }
        };

        // Initialize the Workflow object
        workflow = new Workflow(mockResponse, mockTrigger);
    }

    @Test
    public void testConstructor() {
        // Verify that the Workflow object is properly initialized
        assertNotNull("Workflow should not be null", workflow);
        assertNotNull("Trigger should be correctly initialized", mockTrigger);
        assertNotNull("Response should be correctly initialized", mockResponse);
    }

    @Test
    public void testHandle() {
        
        workflow.handle();

        String expectedTriggerName = "com.example.BatteryTrigger";
        String formattedTriggerName = String.format("com.example.%s", mockTrigger.getTriggerName());

        assertEquals("Trigger name should be formatted correctly", expectedTriggerName, formattedTriggerName);
    }

    @Test
    public void testOnStartAndOnStop() {
        // Since we're running an instrumented test, simulate calling onStart and onStop
        try {
            workflow.onStart();  
            workflow.onStop();   
        } catch (Exception e) {
            fail("onStart() or onStop() should not throw exceptions");
        }
    }
}
