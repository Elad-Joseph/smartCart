package com.example.smartcart.data;

import com.example.smartcart.modle.Product;
import com.example.smartcart.modle.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbUsersHandler {
    private FirebaseFirestore database;
    DocumentReference docRef;

    public DbUsersHandler(){
        database = FirebaseFirestore.getInstance();
    }

    public void addNewUser(User u){
        database.collection("users").add(u.exportUserToDB());
    }

    public void readDocument(String email, String password, FireStoreCallBack fireStoreCallBack) {
        database.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            // You might get multiple docs; get the first one
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            Map<String, Object> data = document.getData();

                            // Optionally check the password:
                            String docPassword = (String) data.get("password");
                            if (password.equals(docPassword)) {
                                // Success: return data via callback
                                List<Map<String, Object>> list = new ArrayList<>();
                                list.add(data);
                                fireStoreCallBack.onCallBack(list);
                            } else {
                                // Password doesn't match: return empty list or handle error
                                fireStoreCallBack.onCallBack(new ArrayList<>());
                            }
                        } else {
                            // No document found with this email
                            fireStoreCallBack.onCallBack(new ArrayList<>());
                        }
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                        fireStoreCallBack.onCallBack(new ArrayList<>());
                    }
                });
    }

    public void findProduct(String id , FireStoreCallBack fireStoreCallBack){
        database.collection("products").whereEqualTo("id" , id).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        QuerySnapshot querySnapshot = task.getResult();
                        if(!querySnapshot.isEmpty()){
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            Map<String, Object> data = document.getData();
                            String productName = (String) data.get("name");
                            fireStoreCallBack.StringCallBack(productName);
                        }
                        else {
                            fireStoreCallBack.StringCallBack("");
                        }
                    }
                    else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                        fireStoreCallBack.StringCallBack("");
                    }
                });
    }

    public void addNewProduct(Product p){
        database.collection("products").add(p.exportProduct());
    }

    public void getListFromUser(String email , FireStoreListCallBack fireStoreListCallBack){
        database.collection("users").whereEqualTo("email" , email).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            CollectionReference collectionReference = querySnapshot.getDocuments().get(0).getReference().collection("list");
                            ArrayList<Map<String, ArrayList<Map<String, Object>>>> lists = new ArrayList<>();
                            collectionReference.get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    QuerySnapshot querySnapshot1 = task1.getResult();
                                    if(!querySnapshot1.isEmpty()){
                                    for (QueryDocumentSnapshot document : task1.getResult()) {
                                        Map<String, Object> data = document.getData();
                                        String listName = (String) document.get("name");
                                        ArrayList<Map<String, Object>> items = (ArrayList<Map<String, Object>>) document.get("items");
                                        Map<String, ArrayList<Map<String, Object>>> list = new HashMap<>();
                                        list.put(listName, items);
                                        lists.add(list);
                                    }
                                    }
                                }
                            });
                            fireStoreListCallBack.onCallBack(lists);
                        } else {
                            fireStoreListCallBack.onCallBack(new ArrayList<>());
                        }
                    }else{
                        Log.w("Firestore", "Error getting lists.", task.getException());
                        fireStoreListCallBack.onCallBack(new ArrayList<>());
                    }
                });
    }

    public void getProfilePicture(String email){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] data = baos.toByteArray();
        database.collection("users").whereEqualTo(email , "email").get()
                .addOnCompleteListener(task -> {

                });
    }

}

