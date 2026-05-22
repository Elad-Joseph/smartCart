package com.example.smartcart.modle;

import android.content.Context;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.smartcart.R;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private String name;
    private Boolean checked;
    private Product product;
    private int amount;
    private int amountChecked;

    public Item(String name, Boolean checked, Product product) {
        this.checked = checked;
        this.product = product;
        this.name = name;
        this.amount = 1;
        if(checked){
            amountChecked = amount;
        }
    }


    public Item(String name) {
        this.name = name;
        this.checked = false;
        this.amount = 1;
        this.product = new Product(name);
    }

    public Item(Product product) {
        this.product = product;
        this.name = product.getName();
        this.checked = false;
        this.amount = 1;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void decreaseAmount(int amount) {
        if (this.amount - amount >= amountChecked && this.amount - amount > 0) {
            this.amount -= amount;
        }
    }

    public Product getProduct() {
        return product;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Boolean getChecked() {
        return checked;
    }

    public Map<String, Object> exportToDatabase() {
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("name", name);
        itemMap.put("checked", checked);
        itemMap.put("productId", product.getId());
        itemMap.put("amount", amount);
        itemMap.put("amountChecked", amountChecked);
        return itemMap;
    }

    public String getProductId() {
        return product.getId();
    }

    public void markItem() {
        if (!checked) {
            if (amountChecked < amount) {
                amountChecked += 1;
            }
            if (amountChecked == amount) {
                checked = true;
            }
        }
    }

    public void unmarkItem() {
        if (amountChecked > 0) {
            amountChecked -= 1;
            checked = false;
        }

    }

    public void updateIsChecked() {
        if (amountChecked == amount) {
            checked = true;
        } else {
            checked = false;
        }
    }

    public int getAmountChecked() {
        return amountChecked;
    }

    public void setAmountChecked(int amountChecked) {
        this.amountChecked = amountChecked;
        updateIsChecked();
    }
}
