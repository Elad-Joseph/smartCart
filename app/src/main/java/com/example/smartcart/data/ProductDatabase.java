package com.example.smartcart.data;

import android.util.Log;

import com.example.smartcart.modle.Product;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class ProductDatabase {

    private FirebaseFirestore Database;
    private DocumentReference DocRef;
    private CollectionReference ColRef;

    public ProductDatabase() {
        Database = FirebaseFirestore.getInstance();
        ColRef = Database.collection("products");
    }

    public void addProduct(Product product) {
        ColRef.add(product.exportProduct());
    }

    public void getProduct(String productId , CallBack<Map<String , Object>> callBack) {
        ColRef.whereEqualTo("id" , productId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    Map<String, Object> productData = querySnapshot.getDocuments().get(0).getData();
                    callBack.onCallBack(productData);
                }
                else {
                    callBack.onCallBack(null);
                }
            } else {
                callBack.onCallBack(null);
                Log.d("ProductDatabase", "Error getting documents: ", task.getException());
            }
        });
    }

    public void deleteProduct(String productId) {
        ColRef.whereEqualTo("id" , productId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    String docId = querySnapshot.getDocuments().get(0).getId();
                    ColRef.document(docId).delete();
                }
            } else {
                Log.d("ProductDatabase", "Error getting documents: ", task.getException());
            }
        });
    }
}
