package com.example.smartcart.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.smartcart.data.CallBack;
import com.example.smartcart.R;
import com.example.smartcart.data.UserDatabase;
import com.google.firebase.FirebaseApp;


public class loginModel extends  BaseActivity {

    private UserDatabase userDatabase;

    private Button LoginButton;
    private Button NewAcountButton;

    private String password;
    private String email;

    private EditText getEmail;
    private EditText getPassword;

    @Override
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

        setupDoubleBackExit();

        SetupIds();
        SetupListeners();


    }

    @Override
    protected void SetupIds(){
        LoginButton = findViewById(R.id.loginButton);
        NewAcountButton = findViewById(R.id.newAcount);

        getEmail = findViewById(R.id.getEmailLogin);
        getPassword = findViewById(R.id.getPasswordLogin);
    }

    @Override
    protected void SetupListeners(){

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
                finish();
            }
        });
    }

    public void loginConfirmation(String email , String password){
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

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