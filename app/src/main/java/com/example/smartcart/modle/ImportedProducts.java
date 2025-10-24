package com.example.smartcart.modle;

import java.util.ArrayList;
import java.util.List;

public class ImportedProducts {
    private static ImportedProducts instance;
    private List<Item> cachedItems = new ArrayList<>();

    private ImportedProducts() {}

    public static synchronized ImportedProducts getInstance() {
        if (instance == null) instance = new ImportedProducts();
        return instance;
    }

    public List<Item> getCachedItems() {
        return cachedItems;
    }

}
