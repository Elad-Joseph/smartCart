package com.example.smartcart.Activities;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;


public abstract class BaseActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;

    protected void setupDoubleBackExit() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finishAffinity();
                    return;
                }
                doubleBackToExitPressedOnce = true;
                Toast.makeText(BaseActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        });
    }



    protected void SetupIds(){

    }

    protected void SetupListeners(){

    }
}