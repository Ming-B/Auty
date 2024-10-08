package com.example.myapplication;

public class User {

    private String userName;
    private String password;

    public User(String username, String password) {
        this.userName = userName;
        this.password = password;
    }

    //function write later for database


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }





}
