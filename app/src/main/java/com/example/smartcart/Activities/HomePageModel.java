package com.example.smartcart.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;

import com.example.smartcart.R;
import com.example.smartcart.data.CallBack;
import com.example.smartcart.data.ListDatabase;
import com.example.smartcart.data.UserDatabase;
import com.example.smartcart.modle.CurrentUser;
import com.example.smartcart.modle.ImportedShoppingLists;
import com.example.smartcart.modle.ShoppingList;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;



public class HomePageModel extends BaseActivity {

    private UserDatabase userDatabase;
    private ListDatabase listDatabase;
    private CurrentUser currentUser;
    ImportedShoppingLists importedShoppingLists;


    private boolean doubleBackToExitPressedOnce;

    ImageButton optionsButton;
    TextView welcomeText;
    MaterialTextView NumberOfListTextview;
    MaterialButton addList;
    LinearLayout listsLayout;
    LinearLayout listContainer;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        setupDoubleBackExit();

        SetupIds();
        SetupListeners();

        currentUser = CurrentUser.getInstance();
        welcomeText.setText(welcomeText.getText().toString() + currentUser.getUsername());

        doubleBackToExitPressedOnce = false;
        doubleBackToExit();

        importedShoppingLists = ImportedShoppingLists.getInstance();
        userDatabase = new UserDatabase();
        listDatabase = new ListDatabase();
        refreshLists();
    }

    @Override
    protected void SetupIds() {
        optionsButton = findViewById(R.id.optionsButton);
        welcomeText = findViewById(R.id.welcomeText);
        NumberOfListTextview = findViewById(R.id.NumberOfLists);
        addList = findViewById(R.id.addListButton);
        listsLayout = findViewById(R.id.listsContainer);
        listContainer = findViewById(R.id.listsContainer);
    }

    @Override
    protected void SetupListeners() {
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
                    finish();
                    return true;
                }
                else if (item.getItemId() == R.id.sighOut) {
                    userDatabase.signOutUser();
                    Intent intent = new Intent(HomePageModel.this, loginModel.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else if (item.getItemId() == R.id.toFriendsList) {
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
                ShoppingList newList = new ShoppingList(listName);

                listDatabase.addList(newList);
                userDatabase.updateUser();

                listContainer.addView(newList.createRow(this));
                Toast.makeText(this, "List added: " + listName +" "+ currentUser.getPassword(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                editTextListName.setError("Enter a name");
            }
        });
    }

    public void doubleBackToExit(){
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    // If pressed twice, finish the activity/exit the app
                    finish();
                    return;
                }

                doubleBackToExitPressedOnce = true;
                Toast.makeText(HomePageModel.this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                // Reset the flag after 2 seconds
                new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        });
    }

    private void refreshLists() {
        listsLayout.removeAllViews();
        listDatabase.getAllListsForCurrentUser(new CallBack<String>() {
            @Override
            public void onCallBack(String value) {
                for (ShoppingList list : importedShoppingLists) {
                    if (list != null) {
                        listContainer.addView(list.createRow(HomePageModel.this));
                    }
                }
                NumberOfListTextview.setText(NumberOfListTextview.getText().toString() + "\n" + importedShoppingLists.size());
            }
        });
    }


}

