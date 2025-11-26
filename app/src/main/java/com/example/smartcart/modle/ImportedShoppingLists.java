
package com.example.smartcart.modle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// addlist , removelist , getlists , getlistbyid
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
            if (list.getId() == id) {
                return list;
            }
        }
        return null;
    }
}
