package com.example.smartcart.modle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcart.Activities.ListDisplayModel;
import com.example.smartcart.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class RecycleViewAdapterListDistplay extends RecyclerView.Adapter<RecycleViewAdapterListDistplay.ViewHolder> {

    private ArrayList<Item> DataSet;
    private ShoppingList currentList;

    public RecycleViewAdapterListDistplay(ShoppingList shoppingList) {
        currentList = shoppingList;
        this.DataSet = currentList.getAllItems();
    }

    public void refreshDataSet() {

        this.DataSet = currentList.getAllItems();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Replace R.layout.shopping_list_item with the actual layout file that contains
        // ListButton, ListHeader and ListOptionsButton.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row_listdisplay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = DataSet.get(position);
        String currentName = item.getName();


        if (holder.ItemNameDisplay != null) {
            holder.ItemNameDisplay.setText(currentName);
        }

        final Item currentItem = currentList.get(position);
        if (holder.ItemCheckbox != null) {
            holder.ItemCheckbox.setChecked(currentItem.getChecked());
            holder.ItemCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentItem.markItem();
                }
            });
        }

        if (holder.ItemOptionsButton != null) {
            holder.ItemOptionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Implement options button functionality here
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return DataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ItemNameDisplay;
        private CheckBox ItemCheckbox;
        private ImageButton ItemOptionsButton;

        public ViewHolder(View view) {
            super(view);
            ItemNameDisplay = view.findViewById(R.id.ItemNameDisplay);
            ItemCheckbox = view.findViewById(R.id.ItemCheckBox);
            ItemOptionsButton = view.findViewById(R.id.ItemOptionsButton);
        }
    }
}

