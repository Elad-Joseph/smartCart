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
        ColRef.add(list.exportList());
        currentUser.addListToImportedLists(list);
    }

    public void deleteList(String listId, CallBack callBack) {
        ColRef.whereEqualTo("Id", listId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    String docId = querySnapshot.getDocuments().get(0).getId();
                    ColRef.document(docId).delete();
                    callBack.onCallBack(null);
                }
            } else {
                Log.d("ListDatabase", "Error getting documents: ", task.getException());
            }
        });
        callBack.onCallBack(null);
    }

    public void updateList(String listId, ShoppingList list, CallBack callBack) {
        ColRef.document(listId).set(list.exportList());
        callBack.onCallBack(null);
    }

    public void getList(String listId, CallBack<Map<String, Object>> callBack) {
        ColRef.whereEqualTo("Id", listId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    Map<String, Object> listData = querySnapshot.getDocuments().get(0).getData();

                    callBack.onCallBack(listData);
                } else {
                    callBack.onCallBack(null);
                }
            } else {
                callBack.onCallBack(null);
                Log.d("ListDatabase", "Error getting documents: ", task.getException());
            }
        });
    }

    public void getAllListsForCurrentUser(CallBack callBack) {
        String[] userListIds = currentUser.getImportedListsIds();
        importedShoppingLists.clear();
        for (int i = 0; i < userListIds.length; i++) {

            getList(userListIds[i], new CallBack<Map<String, Object>>() {
                @Override
                public void onCallBack(Map<String, Object> value) {
                    if (value != null) {
                        ShoppingList list = new ShoppingList(value.get("name").toString(),
                                value.get("Id").toString(),
                                (List<String>) value.get("items"));
                        String a = list.getName();
                        importedShoppingLists.add(list);
                        if (importedShoppingLists.size() == userListIds.length) {
                            {
                                callBack.onCallBack(null);
                            }
                        }
                    }
                }
            });
        }

    }
}
