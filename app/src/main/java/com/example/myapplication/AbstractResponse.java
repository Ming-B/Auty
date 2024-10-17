package com.example.myapplication;


public abstract class AbstractResponse {
    String responseName;
    String message;

    public AbstractResponse(String responseName, String message){
        this.responseName = responseName;
        this.message = message;
    }

    public abstract void respond();

}
