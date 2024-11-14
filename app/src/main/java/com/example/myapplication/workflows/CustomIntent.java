package com.example.myapplication.workflows;

import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

// Custom Intent class to replace Android's Intent
public class CustomIntent extends Intent {
    private String action;
    private Map<String, Object> extras;

    public CustomIntent(String action) {
        this.action = action;
        this.extras = new HashMap<>();
    }

    public String getAction() {
        return action;
    }

    public void putExtra(String key, Object value) {
        extras.put(key, value);
    }

    public Object getExtra(String key) {
        return extras.get(key);
    }
}