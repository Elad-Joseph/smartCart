package com.example.smartcart.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcart.R;
import com.example.smartcart.data.CallBack;
import com.example.smartcart.data.ListDatabase;
import com.example.smartcart.data.ProductDatabase;
import com.example.smartcart.data.UserDatabase;
import com.example.smartcart.helpers.CurrentUser;
import com.example.smartcart.helpers.ImportedShoppingLists;
import com.example.smartcart.helpers.Item;
import com.example.smartcart.helpers.Product;
import com.example.smartcart.helpers.RecycleViewAdapterAddItem;
import com.example.smartcart.helpers.RecycleViewAdapterListDistplay;
import com.example.smartcart.helpers.ShoppingList;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Map;

public class ListDisplayModel extends BaseActivity {
    private ListDatabase listDatabase;
    private UserDatabase userDatabase;
    private ProductDatabase productDatabase;

    private ImportedShoppingLists importedShoppingLists;
    private ShoppingList currentShoppingList;
    private RecycleViewAdapterListDistplay adapter;
    private Product SelectedProduct;

    private CurrentUser currentUser;

    private ImageButton scanItemsButton;
    private MaterialTextView listNameTextView;
    private ImageButton optionsButton;
    private ImageButton addItemButton;
    private ImageButton GoToHomePageButton;
    private RecyclerView ItemContainerRecyclerView;
    private MaterialTextView ListstatusTextView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_display);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        userDatabase = new UserDatabase();
        listDatabase = new ListDatabase();
        productDatabase = new ProductDatabase();
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


        productDatabase.getNProducts(20, new CallBack<ArrayList<Map<String, Object>>>() {
            @Override
            public void onCallBack(ArrayList<Map<String, Object>> value) {
                currentUser.setRecommendedProducts(value);
            }
        });


        SetUpRecyclerView();

        refreshItemList();

    }

    @Override
    protected void SetupIds(){
        scanItemsButton = findViewById(R.id.barcodeButton);
        listNameTextView = findViewById(R.id.listNameDisplay);
        optionsButton = findViewById(R.id.listOptionsButton);
        addItemButton = findViewById(R.id.addItemButton);
        GoToHomePageButton = findViewById(R.id.listDisplayToHomePage);
        ItemContainerRecyclerView = findViewById(R.id.itemRecyclerView);
        ListstatusTextView = findViewById(R.id.listStatusDisplay);
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
                userDatabase.updateUser();
                listDatabase.updateList(currentShoppingList);
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
                    Toast.makeText(ListDisplayModel.this,"List Deleted: "+ currentShoppingList.getName() , Toast.LENGTH_SHORT).show();
                    return true;
                }
//                else if(itemId == R.id.hideList){
//
//                }
                return false;
            }
        });

        popupMenu.show();
    }

    public void addNewItemToList(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = LayoutInflater.from(this).inflate(R.layout.popup_add_item, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();


        EditText itemAmountEditText = view.findViewById(R.id.add_item_amount);
        ImageButton addAmount = view.findViewById(R.id.add_item_addAmount);
        ImageButton decreaseAmount = view.findViewById(R.id.add_item_decreesAmount);
        EditText ItemNameEditText = view.findViewById(R.id.add_item_SetItemName);

        Button addItemButton = view.findViewById(R.id.buttonAddSelectedItem);

        SearchView searchView = view.findViewById(R.id.searchViewAddItem);

        RecyclerView addItemRecycleView = view.findViewById(R.id.recyclerViewItemsToAdd);

        RecycleViewAdapterAddItem recycleViewAdapterAddItem = new RecycleViewAdapterAddItem(currentUser.getRecommendedProducts());

        addItemRecycleView.setLayoutManager(new LinearLayoutManager(this));
        addItemRecycleView.setAdapter(recycleViewAdapterAddItem);

        recycleViewAdapterAddItem.setOnClickListener(product ->{
            SelectedProduct = product;
            ItemNameEditText.setText(SelectedProduct.getName());
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recycleViewAdapterAddItem.getFilter().filter(newText);
                return false;
            }
        });




        addAmount.setOnClickListener(v -> {
            int itemAmount = Integer.parseInt(itemAmountEditText.getText().toString())+1;
            itemAmountEditText.setText(String.valueOf(itemAmount));
        });


        decreaseAmount.setOnClickListener(v -> {
            int itemAmount = Integer.parseInt(itemAmountEditText.getText().toString());
            if (itemAmount > 1) {
                itemAmount--;
                itemAmountEditText.setText(String.valueOf(itemAmount));
            }
            else{
                Toast.makeText(view.getContext() , "cant be less then one" , Toast.LENGTH_SHORT).show();
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedProduct != null){
                    Item itemToAdd = new Item(SelectedProduct);
                    itemToAdd.setAmount(Integer.parseInt(itemAmountEditText.getText().toString()));
                    itemToAdd.setName(ItemNameEditText.getText().toString());
                    currentShoppingList.add(itemToAdd);
                    currentUser.setImportedListsToImportedShoppingLists();
                    userDatabase.updateUser();
                    listDatabase.updateList(currentShoppingList);
                    refreshItemList();
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(view.getContext() , "select a product" , Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    public void refreshItemList(){
        adapter.refreshDataSet();
        ListstatusTextView.setText("number of items:\n " + currentShoppingList.size());
    }
}
