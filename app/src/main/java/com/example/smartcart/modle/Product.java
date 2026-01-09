package com.example.smartcart.modle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Product {
    private String Name;
    private String Id;
    private String Price;

    public Product(String name , String id , String price){
        this.Name = name;
        this.Id = id;
        this.Price = price;
    }

    public Product(String name){
        this.Name = name;
        this.Id = UUID.randomUUID().toString();
        this.Price = "0";
    }

    public Product(){

    }

    public Map<String , Object> exportProduct(){
        Map<String , Object> product = new HashMap<>();
        product.put("name" , this.Name);
        product.put("id" , this.Id);
        return product;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getPrice() {
        return Price;
    }

}
