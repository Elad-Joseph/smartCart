
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
            if (id.equals(list.getId())) {
                return list;
            }
        }
        return null;
    }

    public void RemoveListById(String id) {
        for(ShoppingList i : this){
            if(i.getId().equals(id)){
                this.remove(this.indexOf(i));
                return;
            }
        }
    }

    public String[] getAllIds(){
        ArrayList<String> ids = new ArrayList<>();
        for (ShoppingList list : this) {
            ids.add(list.getId());
        }
        return ids.toArray(new String[0]);
    }


}
