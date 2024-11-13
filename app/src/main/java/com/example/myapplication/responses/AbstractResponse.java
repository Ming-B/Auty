package com.example.myapplication.responses;


public abstract class AbstractResponse {
    String responseName;


    public AbstractResponse(String responseName){
        this.responseName = responseName;

    }

    public abstract void respond(String message);

}
