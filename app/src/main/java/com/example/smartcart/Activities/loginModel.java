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
import com.example.smartcart.data.DbUsersHandler;
import com.example.smartcart.data.FireStoreCallBack;
import com.example.smartcart.R;
import com.example.smartcart.UserViewModel;
import com.example.smartcart.data.UserDatabase;
import com.example.smartcart.modle.CurrentUser;
import com.google.firebase.FirebaseApp;

import java.util.List;
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

        sharedPreferences = getSharedPreferences("AppPrefs" , MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(sharedPreferences.getString("email" , null) != null){
            Intent intent = new Intent(loginModel.this, HomePageModel.class);
            startActivity(intent);
            finish();
        }
        setUpIds();
        setUpListeners();

        userDatabase = new UserDatabase();
        currentUser = CurrentUser.getInstance();
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
                    Number numberOfListsDoc = (Number) usersData.get("list number");
                    int numberOfLists = numberOfListsDoc != null ? numberOfListsDoc.intValue() : 0;

                    currentUser.setEmail(email);
                    currentUser.setUsername(username);
                    currentUser.setNumberOfLists(numberOfLists);

                    editor.putString("username", username);
                    editor.putString("email", email);
                    editor.putInt("list number", numberOfLists);
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