package com.example.smartcart.modle;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private String name;
    private Boolean checked;

    public Item(String name, Boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    public Item(String name) {
        this.name = name;
        this.checked = false;
    }

    public String getName() {
        return name;
    }

    public Boolean getChecked() {
        return checked;
    }

    public Map<String, Object> exportToDatabase() {
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("name", name);
        itemMap.put("checked", checked);
        return itemMap;
    }
}
