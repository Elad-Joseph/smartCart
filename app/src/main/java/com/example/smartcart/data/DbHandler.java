package com.example.smartcart.data;

import com.example.smartcart.modle.User;
import com.google.firebase.firestore.FirebaseFirestore;


public class DbHandler {
    private FirebaseFirestore database;


    public DbHandler(){
        database = FirebaseFirestore.getInstance();
    }

    public void EnterNewUser(User u){
        database.collection("users").add(u.exportUserToDB());
    }

}
