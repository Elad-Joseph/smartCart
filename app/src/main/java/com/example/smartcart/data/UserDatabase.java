package com.example.smartcart.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.smartcart.modle.CurrentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDatabase {
    private FirebaseFirestore Database;
    private CollectionReference ColRef;
    private CurrentUser currentUser;
    private autenticationHandler authHandler;
    private ProductDatabase productDatabase;

    public UserDatabase() {
        Database = FirebaseFirestore.getInstance();
        ColRef = Database.collection("users");
        currentUser = CurrentUser.getInstance();
        authHandler = new autenticationHandler();
        productDatabase = new ProductDatabase();

    }

    public void createNewUser(CallBack<Boolean> callBack){
        String email = currentUser.getEmail();
        String password = currentUser.getPassword();
        authHandler.sighup(email , password , new CallBack<FirebaseUser>() {
            @Override
            public void onCallBack(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    currentUser.setId(firebaseUser.getUid());
                    ColRef.document(firebaseUser.getUid()).set(currentUser.exportCurrentUserToDB());
                    callBack.onCallBack(true);
                }
                else{
                    callBack.onCallBack(false);
                }
            }
        });
    }

    public void loginUser(String email , String password , CallBack<Boolean> callBack){

        authHandler.login(email , password , new CallBack<FirebaseUser>() {
            @Override
            public void onCallBack(FirebaseUser firebaseUser) {
                if(firebaseUser == null){
                    callBack.onCallBack(false);
                    return;
                }
                ColRef.document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String , Object> userData = document.getData();

                                String email = (String) userData.get("email");
                                String username = (String) userData.get("username");
                                String password = (String) userData.get("password");
                                ArrayList<Map<String , Object>> importedLists = (ArrayList<Map<String , Object>>) userData.get("imported lists");

                                productDatabase.getNProducts(20, new CallBack<ArrayList<Map<String, Object>>>() {
                                    @Override
                                    public void onCallBack(ArrayList<Map<String, Object>> value) {
                                        currentUser.setRecommendedProducts(value);
                                        currentUser.setEmail(email);
                                        currentUser.setUsername(username);
                                        currentUser.setPassword(password);
                                        currentUser.setImportedLists(importedLists);
                                        currentUser.setId(firebaseUser.getUid());

                                        callBack.onCallBack(true);
                                    }
                                });

                            } else {
                                callBack.onCallBack(false);
                            }
                        } else {
                            Log.d("UserDatabase", "Error getting documents: ", task.getException());
                            callBack.onCallBack(false);
                        }
                    }
                });
            }
        });
    }

    public void checkForExistingSession(CallBack<Boolean> callBack){
        if(authHandler.getCurrentUser() != null){
            ColRef.document(authHandler.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String , Object> userData = document.getData();

                            String email = (String) userData.get("email");
                            String username = (String) userData.get("username");
                            String password = (String) userData.get("password");
                            ArrayList<Map<String , Object>> importedLists = (ArrayList<Map<String , Object>>) userData.get("imported lists");

                            productDatabase.getNProducts(20, new CallBack<ArrayList<Map<String, Object>>>() {
                                @Override
                                public void onCallBack(ArrayList<Map<String, Object>> value) {
                                    currentUser.setRecommendedProducts(value);
                                    currentUser.setEmail(email);
                                    currentUser.setUsername(username);
                                    currentUser.setPassword(password);
                                    currentUser.setImportedLists(importedLists);
                                    currentUser.setId(authHandler.getCurrentUser().getUid());

                                    callBack.onCallBack(true);
                                }
                            });

                        } else {
                            Log.d("UserDatabase", "No such document");
                            callBack.onCallBack(false);
                        }
                    } else {
                        Log.d("UserDatabase", "Error getting documents: ", task.getException());
                        callBack.onCallBack(false);
                    }
                }
            });
        }
    }

    public void signOutUser(){
        authHandler.signOut();
        currentUser.clear();
    }

    public void addUser() {
        ColRef.add(currentUser.exportCurrentUserToDB());
    }

    public void deleteUser(String userId) {
        ColRef.document(userId).delete();
    }

    public void updateUser() {
        currentUser.setImportedListsToImportedShoppingLists();
        ColRef.document(currentUser.getId()).set(currentUser.exportCurrentUserToDB());
    }

    public void getUser(String userId, CallBack<Map<String , Object>> callBack) {
        ColRef.document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String , Object> userData = task.getResult().getData();
                callBack.onCallBack(userData);
            } else {
                callBack.onCallBack(null);
                Log.d("UserDatabase", "Error getting document: ", task.getException());
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
        currentUser.removeListFromImportedShoppingList(listId);
        updateUser();
    }

    public void getListsFromUser(CallBack<List<Map<String , Object>>> callBack){
        currentUser = CurrentUser.getInstance();
        ColRef.document(currentUser.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String , Object> userData = document.getData();
                        List<Map<String , Object>> importedLists = (List<Map<String , Object>>) userData.get("imported lists");
                        callBack.onCallBack(importedLists);
                    } else {
                        callBack.onCallBack(null);
                    }
                } else {
                    Log.d("UserDatabase", "Error getting documents: ", task.getException());
                    callBack.onCallBack(null);
                }
            }
        });
    }

    

}
