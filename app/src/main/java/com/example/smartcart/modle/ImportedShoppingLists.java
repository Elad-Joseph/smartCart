
package com.example.smartcart.modle;

import java.util.ArrayList;

public class ImportedShoppingLists extends ArrayList<ShoppingList>{

    private static ImportedShoppingLists instance;

    private ImportedShoppingLists() {
        super();
    }

    public static synchronized ImportedShoppingLists getInstance() {
        if (instance == null) {
            instance = new ImportedShoppingLists();
        }
        return instance;
    }

    public ShoppingList getListById(String id) {
        for (ShoppingList list : this) {
            if (id.equals(list.getId())) {
                return list;
            }
        }
        return null;
    }

    public void RemoveListById(String id) {
        this.removeIf(list -> list.getId().equals(id));
    }

    public String[] getAllIds(){
        ArrayList<String> ids = new ArrayList<>();
        for (ShoppingList list : this) {
            ids.add(list.getId());
        }
        return ids.toArray(new String[0]);
    }

    public void clearInstance() {
        if (instance != null) {
            instance.clear();
            instance = null;
        }
    }

    public ArrayList<ShoppingList> getAllLists() {
        return this;
    }
}
