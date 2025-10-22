package com.example.smartcart.modle;

public class CurrentUser {
    private static CurrentUser instance;
    private String username;
    private String email;
    private int numberOfLists;

    private CurrentUser() {
        // Private constructor to prevent instantiation
    }

    public static synchronized CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public int getNumberOfLists() {
        return numberOfLists;
    }
    public void setNumberOfLists(int numberOfLists) {
        this.numberOfLists = numberOfLists;
    }
}
