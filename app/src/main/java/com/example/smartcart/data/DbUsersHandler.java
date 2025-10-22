package com.example.smartcart.data;

import com.example.smartcart.Activities.HomePageModel;
import com.example.smartcart.modle.Item;
import com.example.smartcart.modle.Product;
import com.example.smartcart.modle.ShoppingList;
import com.example.smartcart.modle.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.util.Log;
import android.widget.LinearLayout;

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
        database.collection("users").get()
                .addOnCompleteListener(allDocsTask -> {
                    if (allDocsTask.isSuccessful()) {
                        Log.d("FirestoreDebug", "All user documents:");
                        for (DocumentSnapshot doc : allDocsTask.getResult()) {
                            Log.d("FirestoreDebug", doc.getId() + " -> " + doc.getData());
                        }
                    } else {
                        Log.e("FirestoreDebug", "Error getting all users: ", allDocsTask.getException());
                    }
                });

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
                                // Password doesn't match: return empty list
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

    public void getListsFromUser(String email , FireStoreListCallBack fireStoreListCallBack){
        database.collection("users").whereEqualTo("email" , email).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            CollectionReference collectionReference = querySnapshot.getDocuments().get(0).getReference().collection("list");
                            ArrayList<ShoppingList> shoppingLists = new ArrayList<>();

                            collectionReference.get().addOnCompleteListener(getLists -> {
                                if (getLists.isSuccessful()) {
                                    QuerySnapshot querySnapshot1 = getLists.getResult();
                                    if(!querySnapshot1.isEmpty()){
                                    for (QueryDocumentSnapshot document : getLists.getResult()) {
                                        Map<String, Object> list = document.getData();
                                        ShoppingList shoppingList = new ShoppingList();
                                        for(String key : list.keySet()){
                                            Log.d("FirestoreDebug", "List Data Key: " + key + ", Value: " + list.get(key));
                                            switch (key) {
                                                case "name":
                                                    shoppingList.setName((String) list.get(key));
                                                    break;

                                                case "items":
                                                    ArrayList<Map<String, Object>> databaseItems = (ArrayList<Map<String, Object>>) list.get(key);
                                                    for (Map<String, Object> databaseItem : databaseItems) {
                                                        Item item = new Item( (String) databaseItem.get("name") ,(boolean) databaseItem.get("checked"));
                                                        shoppingList.addItem(item);
                                                    }
                                                    break;

                                                case "numberOfItems":
                                                    shoppingList.setLength(((Long) list.get(key)).intValue());
                                                    break;

                                                case "Id":
                                                    shoppingList.setId(((Long) list.get(key)).intValue());
                                                    break;
                                                case "Hadderid":
                                                    shoppingList.setHadderid(((Long) list.get(key)).intValue());
                                                    break;
                                                case "EditButtonid":
                                                    shoppingList.setEditButtonid(((Long) list.get(key)).intValue());
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                        shoppingLists.add(shoppingList);
                                    }
                                    }
                                }
                                fireStoreListCallBack.onCallBack(shoppingLists);
                            });

                        } else {
                            fireStoreListCallBack.onCallBack(new ArrayList<>());
                        }
                    }else{
                        Log.w("Firestore", "Error getting lists.", task.getException());
                        fireStoreListCallBack.onCallBack(new ArrayList<>());
                    }
                });
    }

    public void addNewListToUser(String email, ShoppingList shoppingList) {
        Log.d("FirestoreDebug", "addNewListToUser called with email: " + email);
        database.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot userDocument = querySnapshot.getDocuments().get(0);
                            CollectionReference listCollection = userDocument.getReference().collection("list");

                            listCollection.add(shoppingList.exportListToDB())
                                    .addOnSuccessListener(documentReference ->
                                            Log.d("FirestoreDebug", "List added successfully with ID: " + documentReference.getId()))
                                    .addOnFailureListener(e ->
                                            Log.e("FirestoreDebug", "Error adding list", e));

                        } else {
                            Log.e("FirestoreDebug", "No user found with email: " + email);
                        }
                    } else {
                        Log.e("FirestoreDebug", "User query failed", task.getException());
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

    public void removeListFromUser(String email , int id){
        database.collection("users").whereEqualTo("email" , email).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            CollectionReference collectionReference = querySnapshot.getDocuments().get(0).getReference().collection("list");

                            collectionReference.get().addOnCompleteListener(getLists -> {
                                if (getLists.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : getLists.getResult()) {
                                        Map<String, Object> list = document.getData();
                                        if(((Long)list.get("Id")).intValue() == id){
                                            document.getReference().delete();
                                            Log.d("FirestoreDebug", "List deleted:" + id);
                                            break;
                                        }
                                    }
                                }
                            });

                        } else {
                            Log.e("FirestoreDebug", "No user found with email: " + email);
                        }
                    } else {
                        Log.e("FirestoreDebug", "User query failed", task.getException());
                    }
                });
    }

}