package com.example.smartcart.data;

import java.util.ArrayList;
import java.util.Map;

public interface FireStoreListCallBack {
    void onCallBack(ArrayList<Map<String, ArrayList<Map<String ,Object>>>> lists);
}
