package com.example.smartcart.modle;

import com.example.smartcart.data.UserDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentUser {
    private static CurrentUser instance;
    private String username;
    private String email;
    private String password;
    private String id;
    private String FriendsIds;
    private ArrayList<Map<String , Object>> ImportedLists;
    private UserDatabase userDatabase;

    private CurrentUser() {
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

    public void clear() {
        username = null;
        email = null;
        password = null;
        id = null;
        FriendsIds = null;
        ImportedLists = null;
    }

    public void addListToImportedLists(Map<String , Object> newList){
        if(ImportedLists == null){
            ImportedLists = new ArrayList<>();
        }
        ImportedLists.add(newList);
    }

    public void addListToImportedLists(ShoppingList newList){
        Map<String , Object> listMap = newList.exportList();
        if(ImportedLists == null){
            ImportedLists = new ArrayList<>();
        }
        ImportedLists.add(listMap);
        userDatabase = new UserDatabase();
        userDatabase.updateUser();
    }

    public void removeListFromImportedLists(String idToRemove){
        if(ImportedLists != null){
            ImportedLists.removeIf(listMap -> listMap.get("id").toString().equals(idToRemove));
        }
    }

    public Map<String , Object> exportCurrentUserToDB() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", this.username);
        userMap.put("email", this.email);
        userMap.put("password", this.password);
        userMap.put("id", this.id);
        userMap.put("friends ids", this.FriendsIds);
        userMap.put("imported lists", this.ImportedLists);
        return userMap;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFriendsIds() {
        return FriendsIds;
    }
    public void setFriendsIds(String friendsIds) {
        FriendsIds = friendsIds;
    }
    public ArrayList<Map<String , Object>> getImportedLists() {
        return ImportedLists;
    }
    public String[] getImportedListsIds() {
        if(ImportedLists == null){
            return new String[0];
        }
        String[] ids = new String[ImportedLists.size()];
        for(int i = 0; i < ImportedLists.size(); i++){
            ids[i] = ImportedLists.get(i).get("Id").toString();
        }
        return ids;
    }

    public void removeListFromImportedLists(ShoppingList listToRemove){
        if(ImportedLists != null){
            ImportedLists.removeIf(listMap -> listMap.get("id").toString().equals(listToRemove.getId()));
        }
    }

    public void setImportedLists(List<Map<String , Object>> importedLists) {
        if(importedLists == null){
            ImportedLists = new ArrayList<>();
            return;
        }
        ImportedLists = new ArrayList<>(importedLists);
    }
}
