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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcart.R;
import com.example.smartcart.data.CallBack;
import com.example.smartcart.data.ListDatabase;
import com.example.smartcart.data.UserDatabase;
import com.example.smartcart.modle.CurrentUser;
import com.example.smartcart.modle.ImportedShoppingLists;
import com.example.smartcart.modle.Item;
import com.example.smartcart.modle.RecycleViewAdapterListDistplay;
import com.example.smartcart.modle.ShoppingList;
import com.example.smartcart.modle.recycleViewAdapterHomePage;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class ListDisplayModel extends BaseActivity {
    private ListDatabase listDatabase;
    private UserDatabase userDatabase;
    private ImportedShoppingLists importedShoppingLists;
    private ShoppingList currentShoppingList;
    private RecycleViewAdapterListDistplay adapter;

    private CurrentUser currentUser;

    ImageButton scanItemsButton;
    MaterialTextView listNameTextView;
    ImageButton optionsButton;
    ImageButton addItemButton;
    ImageButton GoToHomePageButton;
    private RecyclerView ItemContainerRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_display);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        userDatabase = new UserDatabase();
        listDatabase = new ListDatabase();
        currentUser = CurrentUser.getInstance();

        setupDoubleBackExit();
        SetupIds();
        SetupListeners();

        Intent intent = getIntent();
        String currentListId = intent.getStringExtra("CurrentListId");

        importedShoppingLists = ImportedShoppingLists.getInstance();
        currentShoppingList = importedShoppingLists.getListById(currentListId);

        if (currentShoppingList != null) {
            listNameTextView.setText(currentShoppingList.getName());
        }

//
        SetUpRecyclerView();

    }

    @Override
    protected void SetupIds(){
        scanItemsButton = findViewById(R.id.barcodeButton);
        listNameTextView = findViewById(R.id.listNameDisplay);
        optionsButton = findViewById(R.id.listOptionsButton);
        addItemButton = findViewById(R.id.addItemButton);
        GoToHomePageButton = findViewById(R.id.listDisplayToHomePage);
        ItemContainerRecyclerView = findViewById(R.id.itemRecyclerView);
    }

    @Override
    public void SetupListeners(){
        scanItemsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Intent to navigate to the barcode scanning activity
                Intent intent = new Intent(ListDisplayModel.this, BarcodeScannerModel.class);
                intent.putExtra("CurrentListId", currentShoppingList.getId());
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

        GoToHomePageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ListDisplayModel.this, HomePageModel.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void SetUpRecyclerView(){
        ItemContainerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. Set the Adapter
        adapter = new RecycleViewAdapterListDistplay(currentShoppingList);
        ItemContainerRecyclerView.setAdapter(adapter);
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
                    userDatabase.deleteListFromUser(currentShoppingList.getId());
                    listDatabase.deleteList(currentShoppingList.getId(), new CallBack() {
                        @Override
                        public void onCallBack(Object value) {
                            Log.d("ListDisplayModel", "List deleted from ListDatabase: " + currentShoppingList.getId());
                            Intent intent = new Intent(ListDisplayModel.this, HomePageModel.class);
                            startActivity(intent);
                            finish();
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

        ArrayAdapter<Item> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                itemsArray
        );



        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public void refreshItemList(){
        adapter.refreshDataSet();
    }
}
