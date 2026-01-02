package com.example.smartcart.data;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class autenticationHandler {
    private FirebaseAuth mAuth;

    public autenticationHandler() {
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void login(String email, String password, final CallBack<FirebaseUser> callBack) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        callBack.onCallBack(user);
                    } else {
                        callBack.onCallBack(null);
                    }
                });
    }

    public void sighup(String email, String password, final CallBack<FirebaseUser> callBack) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        callBack.onCallBack(user);
                    } else {
                        callBack.onCallBack(null);
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
    }
}
