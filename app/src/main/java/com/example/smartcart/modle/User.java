package com.example.smartcart.modle;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String Username;
    private String Email;
    private String Password;
    private int NumberOfLists;

    public User(String username , String email , String password , int numberOfLists){
        this.Username = username;
        this.Email = email;
        this.Password = password;
        this.NumberOfLists = numberOfLists;
    }

    public User(){

    }

    public Map<String , Object> exportUserToDB(){
        Map<String , Object> user = new HashMap<>();
        user.put("username" , this.Username);
        user.put("email" , this.Email);
        user.put("password" , this.Password);
        user.put("list number" , this.NumberOfLists);
        return user;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getUsername() {
        return Username;
    }

    public int getNumberOfLists(){return NumberOfLists;}

    public void setNumberOfLists(int numberOfLists){
        this.NumberOfLists = numberOfLists;
    }
}
