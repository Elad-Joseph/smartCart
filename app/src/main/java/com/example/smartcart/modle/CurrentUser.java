package com.example.smartcart.modle;

import android.provider.ContactsContract;
import android.util.Log;

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
    private ArrayList<Map<String , Object>> importedLists;
    private ImportedShoppingLists importedShoppingLists;
    private UserDatabase userDatabase;
    private ArrayList<Product> recommendedProducts;


    private CurrentUser() {
    }

    public static synchronized CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public ArrayList<Product> getRecommendedProducts() {
        return recommendedProducts;
    }

    public void setRecommendedProducts(ArrayList<Product> recommendedProducts) {
        this.recommendedProducts = recommendedProducts;
    }

    public ArrayList<String> getRecommendedProductsNames() {
        ArrayList<String> names = new ArrayList<>();
        if (recommendedProducts != null) {
            for (Product product : recommendedProducts) {
                names.add(product.getName());
            }
        }
        return names;
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
        importedLists = null;
        importedShoppingLists.clearInstance();
    }


    public void addListToImportedLists(ShoppingList newList){
        Map<String , Object> listMap = newList.exportList();
        if(importedLists == null){
            importedLists = new ArrayList<>();
        }
        importedLists.add(listMap);
    }


    public void removeListFromImportedShoppingList(String idToRemove){
        importedShoppingLists = ImportedShoppingLists.getInstance();
        importedShoppingLists.RemoveListById(idToRemove);
        setImportedListsToImportedShoppingLists();
    }
    public Map<String , Object> exportCurrentUserToDB() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", this.username);
        userMap.put("email", this.email);
        userMap.put("password", this.password);
        userMap.put("id", this.id);
        userMap.put("friends ids", this.FriendsIds);
        userMap.put("imported lists", this.importedLists);
        return userMap;
    }


    public void setImportedListsToImportedShoppingLists(){
        importedShoppingLists = ImportedShoppingLists.getInstance();
        if(importedLists == null){
            importedLists = new ArrayList<>();
        }
        importedLists.clear();
        for(ShoppingList list : importedShoppingLists){
            importedLists.add(list.exportList());
        }
    }

    public void setImportedLists(ArrayList<Map<String , Object>> importedLists) {
        this.importedLists = importedLists;
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
        return importedLists;
    }

    public String[] getImportedListsIds() {
        if(importedLists == null){
            return new String[0];
        }

        String[] ids = new String[importedLists.size()];
        for(int i = 0; i < importedLists.size(); i++){
            ids[i] = importedLists.get(i).get("id").toString();
        }
        return ids;
    }


}
