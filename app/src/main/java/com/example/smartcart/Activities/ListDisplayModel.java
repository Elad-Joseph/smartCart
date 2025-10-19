package com.example.smartcart.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcart.R;
import com.example.smartcart.modle.ImportedShoppingLists;
import com.example.smartcart.modle.ShoppingList;
import com.google.android.material.button.MaterialButton;

public class ListDisplayModel extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private ImportedShoppingLists importedShoppingLists;
    private ShoppingList currentShoppingList;

    MaterialButton scanItemsButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_display);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        String username = sharedPreferences.getString("username", null);
        String email = sharedPreferences.getString("email", null);
        int numberOfLists = sharedPreferences.getInt("number list" , 0);

        setUpIds();
        setUpListeners();

        Intent intent = getIntent();
        importedShoppingLists = ImportedShoppingLists.getInstance();
        currentShoppingList = importedShoppingLists.getListById(intent.getIntExtra("listId", -1));

    }

    public void setUpIds(){
        scanItemsButton = findViewById(R.id.barcodeButton);
    }

    public void setUpListeners(){
        scanItemsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Intent to navigate to the barcode scanning activity
                Intent intent = new Intent(ListDisplayModel.this, BarcodeScannerModel.class);
                startActivity(intent);
            }
        });

    }
}
