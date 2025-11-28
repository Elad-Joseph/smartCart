// java
package com.example.smartcart.modle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcart.Activities.ListDisplayModel;
import com.example.smartcart.Activities.ProfilePageModel;
import com.example.smartcart.Activities.loginModel;
import com.example.smartcart.R;
import com.example.smartcart.data.DbUsersHandler;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShoppingList extends ArrayList<Item>{

    private String Name;
    private String Id;
    private int Hadderid;
    private int EditButtonid;

    public ShoppingList(){
        this.Name = "";
        this.Id = String.valueOf(UUID.randomUUID().hashCode());
        this.Hadderid = UUID.randomUUID().hashCode();
        this.EditButtonid = UUID.randomUUID().hashCode();
    }

    public ShoppingList(String name){
        this.Name = name;
        this.Id = String.valueOf(UUID.randomUUID().hashCode());
        this.Hadderid = UUID.randomUUID().hashCode();
        this.EditButtonid = UUID.randomUUID().hashCode();
    }

    public ShoppingList(ArrayList<Item> listOfItems , String name , int length , String id , int hadderid , int editButtonid){
        this.Name = name;
        this.Id = id;
        this.Hadderid = hadderid;
        this.EditButtonid = editButtonid;
    }

    public ShoppingList(String name , String id , List<String> itemsIds){
        this.Name = name;
        this.Id = id;
        this.Hadderid = UUID.randomUUID().hashCode();
        this.EditButtonid = UUID.randomUUID().hashCode();
    }

    public void addItem(Item item){
        this.add(item);
    }

    public void remove(Item item){
        this.remove(item);
    }

    public String getName() {
        return Name;
    }

    public ArrayList<Item> getListOfItems() {
        return this;
    }

    public int getLength() {
        return size();
    }

    public void setName(String name) {
        Name = name;
    }


    public void setEditButtonid(int editButtonid) {
        EditButtonid = editButtonid;
    }

    public void setHadderid(int hadderid) {
        Hadderid = hadderid;

    }


    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }


    public Map<String , Object> exportList() {
        Map<String, Object> listData = new HashMap<>();
        listData.put("name", this.Name);
        List<Map<String , Object>> itemsList = new ArrayList<>();
        listData.put("items", itemsList);
        listData.put("Id", this.Id);
        listData.put("Hadderid", this.Hadderid);
        listData.put("EditButtonid", this.EditButtonid);

        return listData;
    }


    public LinearLayout createRow(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dpToPx(context, 75)
        );
        linearLayout.setLayoutParams(llParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.LEFT);
        linearLayout.setBackgroundResource(R.drawable.square); // blue border drawable

        // FrameLayout (left 75%)
        FrameLayout frameLayout = new FrameLayout(context);
        LinearLayout.LayoutParams flParams = new LinearLayout.LayoutParams(
                0,
                dpToPx(context, 75),
                5f // weight 5
        );
        frameLayout.setLayoutParams(flParams);
        frameLayout.setBackgroundColor(Color.TRANSPARENT); // clear background

        // MaterialButton
        MaterialButton materialButton = new MaterialButton(context);
        FrameLayout.LayoutParams btnParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        materialButton.setId(Integer.parseInt(Id));
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListDisplayModel.class);
                intent.putExtra("ListId", Id);
                if (context instanceof Activity) {
                    ((Activity) context).startActivity(intent);
                } else {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
        materialButton.setLayoutParams(btnParams);
        materialButton.setBackgroundResource(R.drawable.clear_background);
        materialButton.setBackgroundTintList(null); // clear tint
        frameLayout.addView(materialButton);

        // TextView inside button
        TextView textView = new TextView(context);
        FrameLayout.LayoutParams tvParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textView.setId(Hadderid);
        tvParams.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(tvParams);
        textView.setText(Name);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(25);
        textView.setPadding(dpToPx(context, 8), 0, 0, 0);
        frameLayout.addView(textView);

        // Add FrameLayout to LinearLayout
        linearLayout.addView(frameLayout);

        return linearLayout;
    }

    private static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

}