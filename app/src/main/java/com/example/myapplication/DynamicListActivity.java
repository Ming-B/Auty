package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.models.DatabaseInit;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserModel;
import com.example.myapplication.models.WorkflowConfig;
import com.example.myapplication.models.WorkflowModel;
import com.example.myapplication.workflows.Workflow;

import java.util.HashMap;

public class DynamicListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the HashMap from the Intent
        Bundle extras = getIntent().getExtras();
        HashMap<String, Boolean> userHashMap = (HashMap<String, Boolean>) extras.getSerializable("userHashMap");

        // Create the layout
        ScrollView scrollView = new ScrollView(this);
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(mainLayout);

        // Add RadioGroup for each item in the HashMap
        if (userHashMap != null) {
            for (String key : userHashMap.keySet()) {
                // Create a LinearLayout for each entry
                LinearLayout entryLayout = new LinearLayout(this);
                entryLayout.setOrientation(LinearLayout.HORIZONTAL);

                // Create a TextView for the string label (instead of RadioButton)
                TextView labelTextView = new TextView(this);
                labelTextView.setText(key + ": ");
                labelTextView.setPadding(16, 0, 16, 0);  // Optional padding for spacing
                entryLayout.addView(labelTextView);



                // Create a CheckBox to represent the boolean value
                CheckBox checkBox = new CheckBox(this);
                checkBox.setChecked(userHashMap.get(key));  // Set checked based on the value in the HashMap
                entryLayout.addView(checkBox);

                // Set an OnCheckedChangeListener for the CheckBox
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    // Handle the checkbox click event here
                    userHashMap.put(key, isChecked);  // Update the HashMap with the new value

                    DatabaseInit dbInit =  new DatabaseInit(this);
                    WorkflowModel workflowModel = new WorkflowModel(dbInit);

                    UserModel userModel = new UserModel(dbInit);
                    long user_id = userModel.getUserID(MainActivity.loggedInUser);

                    workflowModel.updateWorkflow(new WorkflowConfig(key, isChecked), user_id);

                    // For demonstration, you can show a Toast or handle other logic
                    String message = isChecked ? key + " is now TRUE" : key + " is now FALSE";
                    Toast.makeText(DynamicListActivity.this, message, Toast.LENGTH_SHORT).show();
                });

                // Add the entry layout to the main layout
                mainLayout.addView(entryLayout, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
            }
        }
        // Add a "Back" button to return to the previous activity
        Button backButton = new Button(this);
        backButton.setText("Back");
        backButton.setOnClickListener(v -> {
            // Return to the previous activity
            MainActivity mainActivity = (MainActivity) getApplicationContext();
            mainActivity.onReturnToMain();

            finish();
        });

        // Add the Back button to the main layout
        mainLayout.addView(backButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));


        setContentView(scrollView);
    }

    public static void start(Context context, HashMap<String, Boolean> workflowMap) {
        Intent intent = new Intent(context, DynamicListActivity.class);
        intent.putExtra("userHashMap", workflowMap);
        context.startActivity(intent);
    }
}