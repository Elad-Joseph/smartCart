package com.example.smartcart.data;

import java.util.List;
import java.util.Map;

public interface FireStoreCallBack{
    void onCallBack(List<Map<String, Object>> list);
    void StringCallBack(String string);
}
