package com.example.smartcart.modle;

import java.util.ArrayList;

public class ImportedShoppingLists {
    private ArrayList<ShoppingList> lists;
    public ImportedShoppingLists(){
        this.lists = new ArrayList<>();
    }
    public void addList(ShoppingList list){
        this.lists.add(list);
    }

    public void removeList(ShoppingList list){
        this.lists.remove(list);
    }
    public ArrayList<ShoppingList> getLists(){
        return this.lists;
    }
}
