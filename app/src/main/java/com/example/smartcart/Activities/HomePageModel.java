package com.example.smartcart.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcart.R;
import com.example.smartcart.data.DbUsersHandler;
import com.example.smartcart.data.FireStoreListCallBack;
import com.example.smartcart.modle.CurrentUser;
import com.example.smartcart.modle.ImportedShoppingLists;
import com.example.smartcart.modle.ShoppingList;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;


public class HomePageModel extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private DbUsersHandler UserDatabase;
    ImportedShoppingLists importedShoppingLists;


    ImageButton optionsButton;
    TextView welcomeText;
    MaterialTextView NumberOfListTextview;
    MaterialButton addList;
    LinearLayout listsLayout;
    LinearLayout listContainer;

    private String email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        setUpIds();
        setUpListeners();

        String username = sharedPreferences.getString("username", null);
        email = sharedPreferences.getString("email", null);
        int numberOfLists = sharedPreferences.getInt("number list", 0);

        CurrentUser currentUser = CurrentUser.getInstance();
        currentUser.setEmail(email);
        currentUser.setUsername(username);
        currentUser.setNumberOfLists(numberOfLists);

        if (username != null) {
            welcomeText.setText(welcomeText.getText().toString() + username);
        }

        NumberOfListTextview.setText(NumberOfListTextview.getText().toString() + "\n" + numberOfLists);

        importedShoppingLists = ImportedShoppingLists.getInstance();
        UserDatabase = new DbUsersHandler();
        refreshLists();
    }


    public void setUpIds() {
        optionsButton = findViewById(R.id.optionsButton);
        welcomeText = findViewById(R.id.welcomeText);
        NumberOfListTextview = findViewById(R.id.NumberOfLists);
        addList = findViewById(R.id.addListButton);
        listsLayout = findViewById(R.id.listsContainer);
        listContainer = findViewById(R.id.listsContainer);
    }

    public void setUpListeners() {
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsMenu(v);
            }
        });

        addList.setOnClickListener(v -> showAddListPopup());

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
                } else if (item.getItemId() == R.id.sighOut) {
                    sharedPreferences.edit().clear().apply();
                    Intent intent = new Intent(HomePageModel.this, loginModel.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.toFriendsList) {
                    Intent intent = new Intent(HomePageModel.this, FriendsListModel.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        popupMenu.show();
    }

    private void showAddListPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom layout
        View view = LayoutInflater.from(this).inflate(R.layout.popup_add_list, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        EditText editTextListName = view.findViewById(R.id.editTextListName);
        Button buttonAddList = view.findViewById(R.id.buttonAddList);

        buttonAddList.setOnClickListener(v -> {
            String listName = editTextListName.getText().toString().trim();

            if (!listName.isEmpty()) {
                ShoppingList newList = new ShoppingList();
                newList = new ShoppingList(listName);
                UserDatabase.addNewListToUser(email, newList);
                listContainer.addView(newList.createRow(this));
                Toast.makeText(this, "List added: " + listName, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                editTextListName.setError("Enter a name");
            }
        });
    }

    private void refreshLists() {
        listsLayout.removeAllViews();
        UserDatabase.getListsFromUser(email, new FireStoreListCallBack() {
            @Override
            public void onCallBack(ArrayList<ShoppingList> lists) {
                for (ShoppingList list : lists) {
                    importedShoppingLists.addList(list);
                    listContainer.addView(list.createRow(HomePageModel.this));
                }
            }
        });
    }


}

