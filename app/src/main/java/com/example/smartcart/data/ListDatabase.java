package com.example.smartcart.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.smartcart.modle.CurrentUser;
import com.example.smartcart.modle.ImportedShoppingLists;
import com.example.smartcart.modle.Item;
import com.example.smartcart.modle.Product;
import com.example.smartcart.modle.ShoppingList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListDatabase {
    private FirebaseFirestore Database;
    private DocumentReference DocRef;
    private CollectionReference ColRef;
    private CurrentUser currentUser;
    private ImportedShoppingLists importedShoppingLists;
    private ProductDatabase productDatabase;

    public ListDatabase() {
        Database = FirebaseFirestore.getInstance();
        ColRef = Database.collection("lists");
        currentUser = CurrentUser.getInstance();
        importedShoppingLists = ImportedShoppingLists.getInstance();
        productDatabase = new ProductDatabase();
    }

    public void addList(ShoppingList list) {
        importedShoppingLists.add(list);
        currentUser.setImportedListsToImportedShoppingLists();
        ColRef.document(list.getId()).set(list.exportList());
    }

    public void deleteList(String listId, CallBack<Void> callBack) {
        ColRef.document(listId).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callBack.onCallBack(null);
            } else {
                Log.d("ListDatabase", "Error deleting document: ", task.getException());
            }
        });
        callBack.onCallBack(null);
    }

    public void updateList(ShoppingList list) {
        ColRef.document(list.getId()).set(list.exportList());
    }

    public void getList(String listId, CallBack<Map<String, Object>> callBack) {
        ColRef.document(listId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> listData = task.getResult().getData();
                callBack.onCallBack(listData);
            } else {
                callBack.onCallBack(null);
                Log.d("ListDatabase", "Error getting document: ", task.getException());
            }
        });
    }

    public void getAllListsForCurrentUser(CallBack callBack) {

        UserDatabase userDatabase = new UserDatabase();
        userDatabase.getListsFromUser(new CallBack<List<Map<String, Object>>>() {
            @Override
            public void onCallBack(List<Map<String , Object>> value) {
                importedShoppingLists.clear();
                if(value == null){
                    callBack.onCallBack(null);
                    return;
                }
                for(Map<String , Object> listData : value){
                    ShoppingList list = new ShoppingList(listData.get("name").toString(),
                            listData.get("id").toString());
                    AddItemsToList(list.getId());
                    importedShoppingLists.add(list);
                }
                callBack.onCallBack(null);
            }
        });



    }
    public void AddItemsToList(String listId){
        ColRef.document(listId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Map<String , Object>> items = (ArrayList<Map<String, Object>>) task.getResult().get("items");
                    ShoppingList list = importedShoppingLists.getListById(listId);
                    for (Map<String , Object> i : items){
                        productDatabase.getProduct(i.get("productId").toString(), new CallBack<Map<String, Object>>() {
                            @Override
                            public void onCallBack(Map<String, Object> value) {
                                Product product = new Product(value.get("name").toString() , value.get("id").toString() , value.get("price").toString());
                                Item item = new Item(i.get("name").toString() , (boolean) i.get("checked") , product);
                                item.setAmount(Integer.parseInt(i.get("amount").toString()));
                                list.add(item);
                            }
                        });
                    }
                }
            }
        });
    }
}
