package com.example.smartcart.data;

import android.util.Log;

import com.example.smartcart.modle.ShoppingList;
import com.example.smartcart.modle.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class UserDatabase {
    private FirebaseFirestore Database;
    private DocumentReference DocRef;
    private CollectionReference ColRef;

    public UserDatabase() {
        Database = FirebaseFirestore.getInstance();
        ColRef = Database.collection("users");
    }

    public void addUser(User user) {
        ColRef.add(user.exportUserToDB());
    }

    public void deleteUser(String userId) {
        ColRef.document(userId).delete();
    }

    public void updateUser(String userId, User user) {
        ColRef.document(userId).set(user.exportUserToDB());
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


}
