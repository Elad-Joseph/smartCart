package com.example.smartcart.data;

import com.example.smartcart.modle.ShoppingList;

import java.util.ArrayList;
import java.util.Map;

public interface FireStoreListCallBack {
//    void onCallBack(ArrayList<Map<String, ArrayList<Map<String ,Object>>>> lists);
    void onCallBack(ArrayList<ShoppingList> lists);
}
