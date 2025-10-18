package com.example.smartcart.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcart.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;


public class HomePageModel extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    ImageButton optionsButton;
    TextView welcomeText;
    MaterialTextView NumberOfListTextview;
    MaterialButton addList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        setUpIds();
        setUpListeners();

        String username = sharedPreferences.getString("username", null);
        String email = sharedPreferences.getString("email", null);
        int numberOfLists = sharedPreferences.getInt("number list" , 0);

        if (username != null) {
            welcomeText.setText(welcomeText.getText().toString()+username);
        }

        NumberOfListTextview.setText(NumberOfListTextview.getText().toString() + numberOfLists);

    }


    public void setUpIds(){
        optionsButton = findViewById(R.id.optionsButton);
        welcomeText = findViewById(R.id.welcomeText);
        NumberOfListTextview = findViewById(R.id.NumberOfLists);
        addList = findViewById(R.id.addListButton);
    }

    public void setUpListeners() {
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsMenu(v);
            }
        });

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageModel.this , BarcodeScannerModel.class);
                startActivity(intent);
            }
        });
    }

    private void showOptionsMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(HomePageModel.this, anchor);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.options_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.ToProfileSettings) {
                    Intent intent = new Intent(HomePageModel.this, ProfilePageModel.class);
                    startActivity(intent);
                    return true;
                }
                 else if (item.getItemId() == R.id.sighOut) {
                    sharedPreferences.edit().clear().apply();
                    Intent intent = new Intent(HomePageModel.this , loginModel.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        popupMenu.show();
    }
}

