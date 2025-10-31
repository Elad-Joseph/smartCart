package com.example.smartcart.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcart.R;
import com.example.smartcart.data.CallBack;
import com.example.smartcart.data.DbUsersHandler;
import com.example.smartcart.modle.CurrentUser;
import com.example.smartcart.modle.ImportedShoppingLists;
import com.example.smartcart.modle.Item;
import com.example.smartcart.modle.ShoppingList;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class ListDisplayModel extends AppCompatActivity {
    DbUsersHandler database;
    SharedPreferences sharedPreferences;
    private ImportedShoppingLists importedShoppingLists;
    private ShoppingList currentShoppingList;

    ImageButton scanItemsButton;
    MaterialTextView listNameTextView;
    ImageButton optionsButton;
    ImageButton addItemButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_display);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        database = new DbUsersHandler();

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        String username = sharedPreferences.getString("username", null);
        String email = sharedPreferences.getString("email", null);
        int numberOfLists = sharedPreferences.getInt("number list" , 0);

        setUpIds();
        setUpListeners();

        Intent intent = getIntent();
        importedShoppingLists = ImportedShoppingLists.getInstance();
        currentShoppingList = importedShoppingLists.getListById(intent.getIntExtra("ListId", -1));
        if (currentShoppingList != null) {
            listNameTextView.setText(currentShoppingList.getName());
        }

    }

    public void setUpIds(){
        scanItemsButton = findViewById(R.id.barcodeButton);
        listNameTextView = findViewById(R.id.listNameDisplay);
        optionsButton = findViewById(R.id.listOptionsButton);
        addItemButton = findViewById(R.id.addItemButton);
    }

    public void setUpListeners(){
        scanItemsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Intent to navigate to the barcode scanning activity
                Intent intent = new Intent(ListDisplayModel.this, BarcodeScannerModel.class);
                startActivity(intent);
            }
        });

        optionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupOptionsMenu();
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addNewItemToList();
            }
        });

    }

    public void popupOptionsMenu(){
        PopupMenu popupMenu = new PopupMenu(this, optionsButton);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.list_options, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.deleteList) {
                    CurrentUser currentUser = CurrentUser.getInstance();
                    DbUsersHandler dbUsersHandler = new DbUsersHandler();
                    dbUsersHandler.removeListFromUser(currentUser.getEmail(), currentShoppingList.getId(), new CallBack() {
                        @Override
                        public void onCallBack() {
                            Intent intent = new Intent(ListDisplayModel.this, HomePageModel.class);
                            startActivity(intent);
                        }
                    });
                    Toast.makeText(ListDisplayModel.this,"List Deleted:"+ currentUser.getEmail() + " " + currentShoppingList.getId() , Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }

    public void addNewItemToList(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom layout
        View view = LayoutInflater.from(this).inflate(R.layout.popup_add_item, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        Spinner itemSelectorSpinner = view.findViewById(R.id.spinnerItemSelector);
        Button addItemButton = view.findViewById(R.id.buttonAddSelectedItem);

        Item item;
        Item[] itemsArray = {
                new Item ("Milk"),
                new Item ("Eggs"),
                new Item ("Bread"),
                new Item ("Butter"),
                new Item ("Cheese"),
                new Item ("Apples"),
                new Item ("Bananas"),
                new Item ("Chicken"),
                new Item ("Rice"),
        };

        itemSelectorSpinner.setAdapter(new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsArray));

        itemSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedItem = itemSelectorSpinner.getSelectedItem().toString();
                Item item = new Item(selectedItem);
                currentShoppingList.addItem(item);
                database.addItemToSelectedList(currentShoppingList.getId(), item, new CallBack() {
                    @Override
                    public void onCallBack() {
                        dialog.dismiss();
                    }
                });
            }
        });


    }
}
