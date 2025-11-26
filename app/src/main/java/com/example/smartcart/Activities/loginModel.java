package com.example.smartcart.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcart.data.CallBack;
import com.example.smartcart.R;
import com.example.smartcart.data.UserDatabase;
import com.example.smartcart.modle.CurrentUser;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.Map;

public class loginModel extends  AppCompatActivity {

    private CurrentUser currentUser;
    private UserDatabase userDatabase;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Button LoginButton;
    private Button NewAcountButton;

    private String password;
    private String email;

    private EditText getEmail;
    private EditText getPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        FirebaseApp.initializeApp(this);

        userDatabase = new UserDatabase();
        currentUser = CurrentUser.getInstance();
        sharedPreferences = getSharedPreferences("AppPrefs" , MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(sharedPreferences.getString("email" , null) != null){
            String email = sharedPreferences.getString("email" , null);
            Toast.makeText(getApplicationContext(), "Welcome back " + sharedPreferences.getString("username" , null), Toast.LENGTH_SHORT).show();
            userDatabase.getUserByEmail(email , sharedPreferences.getString("password" , null) , new CallBack<Map<String , Object>>() {
                @Override
                public void onCallBack(Map<String, Object> usersData) {
                    if (usersData != null) {
                        String email = (String) usersData.get("email");
                        String username = (String) usersData.get("username");
                        String password = (String) usersData.get("password");
                        ArrayList<Map<String, Object>> importedLists = (ArrayList<Map<String, Object>>) usersData.get("importedLists");
                        currentUser.setEmail(email);
                        currentUser.setUsername(username);
                        currentUser.setPassword(password);
                        currentUser.setImportedLists(importedLists);
                    }
                }
                });
            Intent intent = new Intent(loginModel.this, HomePageModel.class);
            startActivity(intent);
            finish();
        }
        setUpIds();
        setUpListeners();


    }

    public void setUpIds(){
        LoginButton = findViewById(R.id.loginButton);
        NewAcountButton = findViewById(R.id.newAcount);

        getEmail = findViewById(R.id.getEmailLogin);
        getPassword = findViewById(R.id.getPasswordLogin);
    }

    public void setUpListeners(){

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = getPassword.getText().toString().trim();
                email = getEmail.getText().toString().trim();

                loginConfirmation(email , password);

            }
        });

        NewAcountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginModel.this, signInModel.class);
                startActivity(intent);
            }
        });
    }

    public void loginConfirmation(String email , String password){
        userDatabase.getUserByEmail(email, password, new CallBack<Map<String , Object>>() {
            @Override
            public void onCallBack(Map<String, Object> usersData) {
                if (usersData != null) {
                    String email = (String) usersData.get("email");
                    String username = (String) usersData.get("username");
                    String password = (String) usersData.get("password");
                    ArrayList<Map<String, Object>> importedLists = (ArrayList<Map<String, Object>>) usersData.get("importedLists");



                    currentUser.setEmail(email);
                    currentUser.setUsername(username);
                    currentUser.setPassword(password);
                    currentUser.setImportedLists(importedLists);

                    editor.putString("username", username);
                    editor.putString("email", email);
                    editor.putString("password" , password);
                    editor.apply();

                    Intent intent = new Intent(loginModel.this, HomePageModel.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "email or password are wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}