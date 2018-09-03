package com.codecool.enterprise.overcomplicated.model;

public class Player {
    private static int userCount = 0;

    private String userName = "Anonymous";
    private int id = userCount++;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return id;
    }
}
