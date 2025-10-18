package com.example.smartcart;

import android.os.Bundle;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore database;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        userViewModel= new ViewModelProvider(this).get(UserViewModel.class);
//
//        userViewModel.getUser().observe(this , user -> {
//            if(user != null){
//                Log.d("ViewModel", "Email: " + user.getEmail());
//                Log.d("ViewModel", "Username: " + user.getUsername());
//            }
//        });
//
//
//        DbUsersHandler d = new DbUsersHandler();
//        d.readDocument("eladj", "123", new FireStoreCallBack() {
//            @Override
//            public void onCallBack(List<Map<String, Object>> list) {
//                if (!list.isEmpty()) {
//                    Map<String, Object> data = list.get(0);
//                    String email = (String) data.get("email");
//                    String username = (String) data.get("username");
//                    String password = (String) data.get("password");
//
//                    User user = new User(username, email, password);
//                    userViewModel.setUser(user);  // store in ViewModel
//                }
//            }
//        });

    }



}