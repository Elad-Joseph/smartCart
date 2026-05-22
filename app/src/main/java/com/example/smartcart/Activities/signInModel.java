package com.example.smartcart.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcart.R;
import com.example.smartcart.data.CallBack;
import com.example.smartcart.data.UserDatabase;
import com.example.smartcart.modle.CurrentUser;
import com.example.smartcart.modle.User;

public class signInModel extends BaseActivity {

    private CurrentUser currentUser;

    private UserDatabase userDatabase;

    private Button ToLogin;
    private Button register;

    private EditText email;
    private EditText username;
    private EditText password;
    private EditText confirmPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        currentUser = CurrentUser.getInstance();

        userDatabase = new UserDatabase();

        SetupIds();
        SetupListeners();

    }

    @Override
    protected void SetupIds(){
        ToLogin = findViewById(R.id.goToLogin);
        register = findViewById(R.id.registerButton);

        email = findViewById(R.id.getEmailSignIn);
        username = findViewById(R.id.getUsernameSignIn);
        password = findViewById(R.id.getPasswordSignIn);
        confirmPassword = findViewById(R.id.getConfirmPasswordSignIn);
    }

    @Override
    protected void SetupListeners(){
        ToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signInModel.this, loginModel.class);
                startActivity(intent);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    public void registerNewUser() {
        String passwordText = password.getText().toString().trim();
        String confirmPasswordText = confirmPassword.getText().toString().trim();
        String usernameText = username.getText().toString().trim();
        String emailText = email.getText().toString().trim();

        if (passwordText.isEmpty() && usernameText.isEmpty()&& emailText.isEmpty()) {
            if (passwordText.equals(confirmPasswordText)) {

                currentUser.setUsername(usernameText);
                currentUser.setEmail(emailText);
                currentUser.setPassword(passwordText);

                userDatabase.createNewUser(new CallBack<Boolean>() {
                    @Override
                    public void onCallBack(Boolean value) {
                        if (value) {
                            Intent intent = new Intent(signInModel.this, HomePageModel.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Log.d("Firebase authentication" , "unable to register user" );
                        }
                    }
                });
            }
        }
    }
}