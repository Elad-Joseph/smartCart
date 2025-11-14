package com.example.smartcart.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcart.data.DbUsersHandler;
import com.example.smartcart.R;
import com.example.smartcart.data.UserDatabase;
import com.example.smartcart.modle.User;

public class signInModel extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    UserDatabase userDatabase;

    private Button ToLogin;
    private Button register;

    private EditText email;
    private EditText username;
    private EditText password;
    private EditText confirmPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        sharedPreferences = getSharedPreferences("AppPrefs" , MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userDatabase = new UserDatabase();
        setUpIds();
        setUpListeners();

    }

    public void setUpIds(){
        ToLogin = findViewById(R.id.goToLogin);
        register = findViewById(R.id.registerButton);

        email = findViewById(R.id.getEmailSignIn);
        username = findViewById(R.id.getUsernameSignIn);
        password = findViewById(R.id.getPasswordSignIn);
        confirmPassword = findViewById(R.id.getConfirmPasswordSignIn);
    }

    public void setUpListeners(){
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
                if (password.getText().toString().trim() != "" && username.getText().toString().trim() != "" && email.getText().toString().trim() != ""){
                    if (password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {

                        User user = new User(username.getText().toString().trim(), email.getText().toString().trim(), password.getText().toString().trim() , 0);
                        userDatabase.addUser(user);

                        editor.putString("username", username.getText().toString().trim());  // Save username
                        editor.putString("email", email.getText().toString().trim());
                        editor.putInt("list number" , 0);
                        editor.apply();



                        Intent intent = new Intent(signInModel.this, HomePageModel.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

}
