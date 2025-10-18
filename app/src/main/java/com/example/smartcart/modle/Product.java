package com.example.smartcart.modle;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private String Name;
    private String Id;

    public Product(String name , String id){
        this.Name = name;
        this.Id = id;
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

    public void setId(String id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }
}
