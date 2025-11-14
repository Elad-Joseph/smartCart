package com.example.smartcart.modle;

import java.util.HashMap;
import java.util.Map;

public class CurrentUser {
    private static CurrentUser instance;
    private String username;
    private String email;
    private int numberOfLists;
    private int[] ListIds;

    private CurrentUser() {
        // Private constructor to prevent instantiation
    }

    public static synchronized CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public int getNumberOfLists() {
        return numberOfLists;
    }
    public void setNumberOfLists(int numberOfLists) {
        this.numberOfLists = numberOfLists;
    }

    public int[] getListIds() {
        return ListIds;
    }

    public void setListIds(int[] listIds) {
        ListIds = listIds;
        numberOfLists = (listIds != null) ? listIds.length : -1;
    }

    public void clear() {
        username = null;
        email = null;
        numberOfLists = 0;
        ListIds = null;
    }

    public void addItemToListIds(int newListId) {
        if (ListIds == null) {
            ListIds = new int[] { newListId };
        } else {
            int[] newListIds = new int[ListIds.length + 1];
            System.arraycopy(ListIds, 0, newListIds, 0, ListIds.length);
            newListIds[ListIds.length] = newListId;
            ListIds = newListIds;
        }
    }

    public Map<String , Object> exportCurrentUserToDB() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", this.username);
        userMap.put("email", this.email);
        userMap.put("numberOfLists", this.numberOfLists);
        return userMap;
    }


}
