package com.example.myapplication.workflows;

import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;

// Custom Intent Filter
public class CustomIntentFilter extends IntentFilter {
    private List<String> actions;

    public CustomIntentFilter(String action) {
        this.actions = new ArrayList<>();
        this.actions.add(action);
    }

    public boolean matchesAction(String action) {
        return actions.contains(action);
    }
}