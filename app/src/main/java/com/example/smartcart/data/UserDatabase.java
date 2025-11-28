package com.example.smartcart.data;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.smartcart.modle.CurrentUser;
import com.example.smartcart.modle.ShoppingList;
import com.example.smartcart.modle.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Currency;
import java.util.Map;

public class UserDatabase {
    private FirebaseFirestore Database;
    private DocumentReference DocRef;
    private CollectionReference ColRef;
    private CurrentUser currentUser;

    public UserDatabase() {
        Database = FirebaseFirestore.getInstance();
        ColRef = Database.collection("users");
        currentUser = CurrentUser.getInstance();
    }

    public void addUser() {
        ColRef.add(currentUser.exportCurrentUserToDB());
    }

    public void deleteUser(String userId) {
        ColRef.document(userId).delete();
    }

    public void updateUser() {
//        ColRef.document(userId).set(currentUser.exportCurrentUserToDB());
        ColRef.whereEqualTo("email", currentUser.getEmail()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    String docId = querySnapshot.getDocuments().get(0).getId();
                    ColRef.document(docId).set(currentUser.exportCurrentUserToDB());
                }
            } else {
                Log.d("UserDatabase", "Error getting documents: ", task.getException());
            }
        });
    }

    public void getUser(String userId, CallBack<Map<String , Object>> callBack) {
        ColRef.whereEqualTo("id", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    Map<String , Object> userData = querySnapshot.getDocuments().get(0).getData();

                    callBack.onCallBack(userData);
                } else {
                    callBack.onCallBack(null);
                }
            } else {
                callBack.onCallBack(null);
                android.util.Log.d("UserDatabase", "Error getting documents: ", task.getException());
            }
        });
    }

    public void getUserByEmail(String email, String password ,CallBack<Map<String , Object>> callBack) {
        ColRef.whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    Map<String , Object> userData = querySnapshot.getDocuments().get(0).getData();
                    if(userData.get("password").equals(password)){
                        callBack.onCallBack(userData);
                        return;
                    }
                    callBack.onCallBack(null);
                } else {
                    callBack.onCallBack(null);
                }
            } else {
                callBack.onCallBack(null);
                Log.d("UserDatabase", "Error getting documents: ", task.getException());
            }
        });
    }

    public void deleteListFromUser(String listId){
        currentUser.removeListFromImportedLists(listId);
        updateUser();
    }

}
