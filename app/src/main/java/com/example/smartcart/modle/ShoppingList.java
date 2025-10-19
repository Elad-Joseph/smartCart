package com.example.smartcart.modle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smartcart.Activities.ListDisplayModel;
import com.example.smartcart.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class ShoppingList {
    private ArrayList<Item> ListOfItems;
    private String Name;
    private int Length;
    private int Id;

    private int Hadderid;
    private int EditButtonid;

    public ShoppingList(){
        this.ListOfItems = new ArrayList<>();
        this.Name = "";
        this.Length = 0;
        this.Id = UUID.randomUUID().hashCode();
        this.Hadderid = UUID.randomUUID().hashCode();
        this.EditButtonid = UUID.randomUUID().hashCode();
    }

    public ShoppingList(String name){
        this.ListOfItems = new ArrayList<>();
        this.Name = name;
        this.Length = 0;
        this.Id = UUID.randomUUID().hashCode();
        this.Hadderid = UUID.randomUUID().hashCode();
        this.EditButtonid = UUID.randomUUID().hashCode();
    }

    public ShoppingList(ArrayList<Item> listOfItems , String name , int length , int id , int hadderid , int editButtonid){
        this.ListOfItems = listOfItems;
        this.Name = name;
        this.Length = length;
        this.Id = id;
        this.Hadderid = hadderid;
        this.EditButtonid = editButtonid;
    }

    public void addItem(Item item){
        ListOfItems.add(item);
    }

    public void remove(Item item){
        ListOfItems.remove(item);
    }

    public String getName() {
        return Name;
    }

    public ArrayList<Item> getListOfItems() {
        return ListOfItems;
    }

    public int getLength() {
        return Length;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setLength(int length) {
        Length = length;
    }

    public void setEditButtonid(int editButtonid) {
        EditButtonid = editButtonid;
    }

    public void setHadderid(int hadderid) {
        Hadderid = hadderid;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }


    public Map<String , Object> exportListToDB() {
        Map<String, Object> listData = new HashMap<>();
        listData.put("name", this.Name);
        listData.put("numberOfItems", this.Length);
        List<Map<String , Object>> itemsList = new ArrayList<>();
        listData.put("items", itemsList);
        listData.put("Id", this.Id);
        listData.put("Hadderid", this.Hadderid);
        listData.put("EditButtonid", this.EditButtonid);

        return listData;
    }

    public LinearLayout createRow(Context context) {
        // Parent LinearLayout
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
        materialButton.setId(Id);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListDisplayModel.class);
                intent.putExtra("ListId", Id);
                context.startActivity(intent);
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

        // ImageButton (right 25%)
        ImageButton imageButton = new ImageButton(context);
        LinearLayout.LayoutParams ibParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1f // weight 1
        );
        imageButton.setId(EditButtonid);
        imageButton.setLayoutParams(ibParams);
        imageButton.setImageResource(R.drawable.baseline_edit_square);
        imageButton.setBackgroundColor(Color.TRANSPARENT);
        imageButton.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
        linearLayout.addView(imageButton);

        return linearLayout;
    }

    private static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
