package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

// TODO: remove this

public class StringListActivity extends AppCompatActivity {
    private EditText editText;
    private Button addButton;
    private ListView listView;
    private ArrayList<String> stringList;
    private ArrayAdapter<String> adapter;

    // Define a constant for the intent extra key
    public static final String EXTRA_STRING_ARRAY = "extra_string_array";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_list);

        // Initialize views
        editText = findViewById(R.id.editText);
        addButton = findViewById(R.id.addButton);
        listView = findViewById(R.id.listView);

        // Initialize string list
        stringList = new ArrayList<>();

        // Get string array from intent if available
        String[] receivedStrings = getIntent().getStringArrayExtra(EXTRA_STRING_ARRAY);
        if (receivedStrings != null) {
            Collections.addAll(stringList, receivedStrings);
        }

        // Initialize adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringList);
        listView.setAdapter(adapter);

        // Add button click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newString = editText.getText().toString().trim();
                if (!newString.isEmpty()) {
                    stringList.add(newString);
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                }
            }
        });

        // Add back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Return result to MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_STRING_ARRAY,
                    stringList.toArray(new String[0]));
            setResult(RESULT_OK, resultIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Return result when back button is pressed
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_STRING_ARRAY,
                stringList.toArray(new String[0]));
        setResult(RESULT_OK, resultIntent);
        finish();
        super.onBackPressed();
    }
}
