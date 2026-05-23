
package com.example.smartcart.modle;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShoppingList extends ArrayList<Item> {

    private String Name;
    private String Id;

    public ShoppingList() {
        this.Name = "";
        this.Id = UUID.randomUUID().toString();
    }

    public ShoppingList(String name) {
        this.Name = name;
        this.Id = UUID.randomUUID().toString();
    }

    public ShoppingList(String name, String id) {
        this.Name = name;
        this.Id = id;
    }

    public ArrayList<Item> getAllItems() {
        return this;
    }

    public void addItem(Item item) {
        this.add(item);
    }


    public String getName() {
        return Name;
    }

    public int getLength() {
        return size();
    }

    public void setName(String name) {
        Name = name;
    }


    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public Boolean containsItem(String itemId) {
        for (Item item : this) {
            if (item.getProductId().equals(itemId)) {
                return true;
            }
        }
        return false;
    }

    public Item getItemById(String itemId) {
        for (Item item : this) {
            if (item.getProductId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }

    public Map<String, Object> exportList() {
        Map<String, Object> listData = new HashMap<>();
        listData.put("name", this.Name);
        ArrayList<Map<String, Object>> itemsList = setShoppingListItemsToMap();
        listData.put("items", itemsList);
        listData.put("id", this.Id);

        return listData;
    }

    public ArrayList<Map<String, Object>> setShoppingListItemsToMap() {
        ArrayList<Map<String, Object>> itemsList = new ArrayList<>();
        for (Item item : this) {
            itemsList.add(item.exportToDatabase());
        }
        return itemsList;
    }


    public void markItem(String itemId) {
       for (Item item: this) {
           if (item.getProductId().equals(itemId)) {
               item.markItem();
               break;
           }
       }
    }

    public void unmarkItem(String itemId) {
       for (Item item: this) {
           if (item.getProductId().equals(itemId)) {
               item.unmarkItem();
               break;
           }
       }
    }
}