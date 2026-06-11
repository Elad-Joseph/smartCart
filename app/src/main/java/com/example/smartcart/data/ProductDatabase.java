package com.example.smartcart.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.smartcart.helpers.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ProductDatabase {

    private FirebaseFirestore Database;
    private CollectionReference ColRef;

    public ProductDatabase() {
        Database = FirebaseFirestore.getInstance();
        ColRef = Database.collection("products");
    }

    public void addProduct(Product product) {
        ColRef.document(product.getId()).set(product.exportProduct());
    }

    public void getProduct(String productId , CallBack<Map<String , Object>> callBack) {
        ColRef.document(productId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    Map<String, Object> productData = task.getResult().getData();
                    callBack.onCallBack(productData);
                } else {
                    callBack.onCallBack(null);
                }
            } else {
                callBack.onCallBack(null);
                Log.d("ProductDatabase", "Error getting document: ", task.getException());
            }
        });
    }

    public void deleteProduct(String productId) {
        ColRef.document(productId).delete();
    }

    public void getNProducts(int numberOfProducts, CallBack<ArrayList<Map<String, Object>>> callBack) {
        ColRef.limit(numberOfProducts).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                ArrayList<Map<String, Object>> products = new ArrayList<>();
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    products.add(document.getData());
                }
                callBack.onCallBack(products);
            } else {
                callBack.onCallBack(null);
                Log.d("ProductDatabase", "Error getting documents: ", task.getException());
            }
        });
    }

    public void IsExist(String productId , CallBack<Boolean> callBack){
        ColRef.document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    callBack.onCallBack(documentSnapshot.exists());
                }else{
                    callBack.onCallBack(null);
                }
            }
        });
    }
}
