package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public abstract class Applet {

    String appName;

    public Applet(String appName, Object config){
        this.appName = appName;
    }

    public abstract ArrayList<String> listFunctionality();
}
