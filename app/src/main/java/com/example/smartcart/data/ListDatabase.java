package com.example.smartcart.data;

import android.util.Log;

import com.example.smartcart.modle.CurrentUser;
import com.example.smartcart.modle.ImportedShoppingLists;
import com.example.smartcart.modle.ShoppingList;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class ListDatabase {
    private FirebaseFirestore Database;
    private DocumentReference DocRef;
    private CollectionReference ColRef;
    private CurrentUser currentUser;
    private ImportedShoppingLists importedShoppingLists;

    public ListDatabase() {
        Database = FirebaseFirestore.getInstance();
        ColRef = Database.collection("lists");
        currentUser = CurrentUser.getInstance();
        importedShoppingLists = ImportedShoppingLists.getInstance();
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

    public void updateList(String listId, ShoppingList list, CallBack callBack) {
        ColRef.document(listId).set(list.exportList());
        callBack.onCallBack(null);
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
                    importedShoppingLists.add(list);
                }
                callBack.onCallBack(null);
            }
        });

    }
}
