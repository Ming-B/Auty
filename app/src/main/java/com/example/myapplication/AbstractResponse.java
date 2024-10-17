package com.example.myapplication;

public abstract class AbstractResponse {
    String responseName;
    Applet app;
    String message;

    public AbstractResponse(String responseName, String message, Applet responseApp){
        this.responseName = responseName;
        this.message = message;
        this.app = responseApp;
    }



}
