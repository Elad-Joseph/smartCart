package com.example.smartcart.Activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcart.R;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;

public class FriendsListModel extends AppCompatActivity {

    SearchView friendsSearchView;
    ListView friendsListView;
    ArrayAdapter<String> Adapter;
    ArrayList<String> friendsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);

        friendsSearchView = findViewById(R.id.friendSearchView);
        friendsListView = findViewById(R.id.friendsListView);

        // Sample data for friends list
        friendsList = new ArrayList<>();
        friendsList.add("Alice");
        friendsList.add("Bob");
        friendsList.add("Charlie");
        friendsList.add("David");
        friendsList.add("Eve");

        Adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendsList);
        friendsListView.setAdapter(Adapter);

        friendsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Adapter.getFilter().filter(newText);
                return false;
            }
        });
    }


}
