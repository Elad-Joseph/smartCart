package com.example.smartcart.modle;

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
}
