package com.example.smartcart.modle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShoppingList {
    private ArrayList<Product> ListOfProducts;
    private String Name;
    private int Length;

    public ShoppingList(){

    }

    public ShoppingList(ArrayList<Product> listOfProducts , String name , int length){
        this.ListOfProducts = listOfProducts;
        this.Name = name;
        this.Length = length;
    }

    public void addItem(Product product){
        ListOfProducts.add(product);
    }

    public void remove(Product product){
        ListOfProducts.remove(product);
    }

    public String getName() {
        return Name;
    }

    public ArrayList<Product> getListOfProducts() {
        return ListOfProducts;
    }

    public int getLength() {
        return Length;
    }

}
