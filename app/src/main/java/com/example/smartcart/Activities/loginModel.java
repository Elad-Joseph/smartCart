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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class loginModel extends  AppCompatActivity {

    private UserDatabase userDatabase;

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

        userDatabase.checkForExistingSession(new CallBack<Boolean>() {
            @Override
            public void onCallBack(Boolean value) {
                if (value) {
                    Intent intent = new Intent(loginModel.this, HomePageModel.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

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

        userDatabase.loginUser(email , password , new CallBack<>() {
            @Override
            public void onCallBack(Boolean isSuccess) {
                if (isSuccess) {
                    Intent intent = new Intent(loginModel.this, HomePageModel.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Email or password are wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}