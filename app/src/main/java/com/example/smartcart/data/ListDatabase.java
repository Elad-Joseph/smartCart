package com.example.smartcart.data;

import android.util.Log;

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

    public ListDatabase() {
        Database = FirebaseFirestore.getInstance();
        ColRef = Database.collection("lists");
    }

    public void addList(ShoppingList list) {
        ColRef.add(list.exportList());
    }

    public void deleteList(String listId) {
        ColRef.document(listId).delete();
    }

    public void updateList(String listId, ShoppingList list) {
        ColRef.document(listId).set(list.exportList());
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
}
