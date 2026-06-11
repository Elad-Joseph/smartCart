package com.example.smartcart.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.smartcart.R;
import com.example.smartcart.data.UserDatabase;
import com.example.smartcart.helpers.CurrentUser;
import com.example.smartcart.helpers.NotificationsHandler;
import com.google.android.material.button.MaterialButton;

public class ProfilePageModel extends BaseActivity {
    private UserDatabase userDatabase;
    private CurrentUser currentUser;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private Switch notificationsSwitch;

    private MaterialButton changeUsernameButton;
    private EditText changeUsernameEditText;



    private MaterialButton saveChangesButton;
    private boolean DoubleclickSaveChangesButton = false;

    private ImageButton ToHomePageButton;

    private NotificationsHandler notificationsHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        sharedPreferences = ProfilePageModel.this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();


        userDatabase = new UserDatabase();
        currentUser = CurrentUser.getInstance();

        setupDoubleBackExit();
        SetupIds();
        SetupListeners();


    }

    @Override
    protected void SetupIds() {
        changeUsernameButton = findViewById(R.id.editUsernameButton);
        changeUsernameEditText = findViewById(R.id.usernameProfilePage);
        changeUsernameEditText.setText(currentUser.getUsername());

        saveChangesButton = findViewById(R.id.saveChangesButton);

        notificationsSwitch = findViewById(R.id.userNotificationsSwitch);
        notificationsSwitch.setChecked(sharedPreferences.getBoolean("notifications_enabled", false));

        ToHomePageButton = findViewById(R.id.backButtonProfilePage);
    }

    @Override
    protected void SetupListeners() {
        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("notifications_enabled", isChecked);
            editor.apply();
            if(isChecked){
                NotificationsHandler.schedule(getApplicationContext());
            }
            else {
                NotificationsHandler.cancel(getApplicationContext());
            }
        });


        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changeUsernameEditText.isEnabled()){
                    changeUsernameEditText.setEnabled(false);
                }
                else{
                    changeUsernameEditText.setEnabled(true);
                }
            }
        });


        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DoubleclickSaveChangesButton){
                    saveChanges();
                }
                else{
                    DoubleclickSaveChangesButton = true;
                    Toast.makeText(ProfilePageModel.this, "press again to confirm", Toast.LENGTH_SHORT).show();
                    new Handler(Looper.getMainLooper()).postDelayed(() -> DoubleclickSaveChangesButton = false, 2000);
                }
            }
        });

        ToHomePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePageModel.this, HomePageModel.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveChanges(){
        currentUser.setUsername(changeUsernameEditText.getText().toString());

        userDatabase.updateUser();
        Intent intent = new Intent(ProfilePageModel.this, HomePageModel.class);
        startActivity(intent);
        finish();
    }
}
