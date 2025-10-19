// java
package com.example.smartcart.modle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImportedShoppingLists {
    private final ArrayList<ShoppingList> lists;

    private ImportedShoppingLists() {
        this.lists = new ArrayList<>();
    }

    private static class Holder {
        private static final ImportedShoppingLists INSTANCE = new ImportedShoppingLists();
    }

    public static ImportedShoppingLists getInstance() {
        return Holder.INSTANCE;
    }

    public synchronized void addList(ShoppingList list) {
        this.lists.add(list);
    }

    public synchronized void removeList(ShoppingList list) {
        this.lists.remove(list);
    }

    public synchronized List<ShoppingList> getLists() {
        return Collections.unmodifiableList(new ArrayList<>(this.lists));
    }

    public synchronized ShoppingList getListById(int id) {
        for (ShoppingList list : lists) {
            if (list.getId() == id) {
                return list;
            }
        }
        return null;
    }
}
