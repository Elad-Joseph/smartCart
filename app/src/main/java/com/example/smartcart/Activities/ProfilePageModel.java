package com.example.smartcart.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcart.R;
import com.example.smartcart.modle.NotificationsHandler;

public class ProfilePageModel extends BaseActivity {
    

    private Switch notificationsSwitch;
    private NotificationsHandler notificationsHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        setupDoubleBackExit();
        SetupIds();


    }

    @Override
    protected void SetupIds() {
        notificationsSwitch = findViewById(R.id.userNotificationsSwitch);
    }

    @Override
    protected void SetupListeners() {
        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

            } else {

            }
        });
    }
}
