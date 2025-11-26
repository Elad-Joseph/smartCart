package com.example.smartcart.data;

import android.util.Log;

import com.example.smartcart.modle.CurrentUser;
import com.example.smartcart.modle.ImportedShoppingLists;
import com.example.smartcart.modle.ShoppingList;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

    public void deleteList(String listId , CallBack callBack) {
        ColRef.whereEqualTo("Id", listId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    String docId = querySnapshot.getDocuments().get(0).getId();
                    ColRef.document(docId).delete();
                }
            } else {
                Log.d("ListDatabase", "Error getting documents: ", task.getException());
            }
        });
        callBack.onCallBack(null);
    }

    public void updateList(String listId, ShoppingList list , CallBack callBack) {
        ColRef.document(listId).set(list.exportList());
        callBack.onCallBack(null);
    }

    public void getList(String listId, CallBack<Map<String , Object>> callBack) {
        ColRef.whereEqualTo("id", listId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    Map<String , Object> listData = querySnapshot.getDocuments().get(0).getData();

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
        for(int i = 0; i < userListIds.length; i++) {
            int I = i;
            getList(userListIds[i] , listData -> {
                if (listData != null) {
                    ShoppingList list = new ShoppingList(listData.get("name").toString(),
                            listData.get("id").toString(),
                            (int[]) listData.get("itemsIds"));
                    importedShoppingLists.add(list);
                }
                // Check if all lists have been processed

            });
            callBack.onCallBack(null);
        }
    }
}
