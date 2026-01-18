package com.example.smartcart.modle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.service.autofill.Dataset;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcart.Activities.ListDisplayModel;
import com.example.smartcart.R;
import com.example.smartcart.data.ListDatabase;
import com.example.smartcart.data.UserDatabase;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class RecycleViewAdapterListDistplay extends RecyclerView.Adapter<RecycleViewAdapterListDistplay.ViewHolder> {

    private ArrayList<Item> DataSet;
    private ShoppingList currentList;
    private UserDatabase userDatabase;
    private ListDatabase listDatabase;
    private static final int TYPE_MULTIPLE_ITEMS = 0;
    private static final int TYPE_SINGLE_ITEM = 1;

    public RecycleViewAdapterListDistplay(ShoppingList shoppingList) {
        currentList = shoppingList;
        this.DataSet = currentList.getAllItems();
        userDatabase = new UserDatabase();
        listDatabase = new ListDatabase();
    }

    public void refreshDataSet() {

        this.DataSet = currentList.getAllItems();
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        if (DataSet.get(position).getAmount() > 1) {
            return TYPE_MULTIPLE_ITEMS;
        }
        return TYPE_SINGLE_ITEM;
    }

    @NonNull
    @Override
    public RecycleViewAdapterListDistplay.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_MULTIPLE_ITEMS) {
            View view = inflater.inflate(R.layout.item_row_multiple_listdisplay, parent, false);
            return new MultipleItemsViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_row_listdisplay, parent, false);
            return new SingleItemsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_MULTIPLE_ITEMS) {
           bindMultipleItemsViewHolder(holder, position);
        } else {
            bindSingleItemViewHolder(holder, position);
        }
    }


    public void bindSingleItemViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = DataSet.get(position);
        String currentName = item.getName();


        if (holder.ItemNameDisplay != null) {
            holder.ItemNameDisplay.setText(currentName);
        }

        final Item currentItem = currentList.get(position);
        if (holder.ItemCheckbox != null) {
            holder.ItemCheckbox.setChecked(currentItem.getChecked());
            holder.ItemCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        currentItem.markItem();
                    }
                    else {
                        currentItem.unmarkItem();
                    }
                    userDatabase.updateUser();
                    listDatabase.updateList(currentList);
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


    public void bindMultipleItemsViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = DataSet.get(position);
        String currentName = item.getName();
        String currentAmount = String.valueOf(item.getAmount());
        String CheckedAmount = String.valueOf(item.getAmountChecked());
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
    public class SingleItemsViewHolder extends RecycleViewAdapterListDistplay.ViewHolder {
        private TextView ItemNameDisplay;
        private CheckBox ItemCheckbox;
        private ImageButton ItemOptionsButton;
        public SingleItemsViewHolder(View view) {
            super(view);
            ItemNameDisplay = view.findViewById(R.id.ItemNameDisplay);
            ItemCheckbox = view.findViewById(R.id.ItemCheckBox);
            ItemOptionsButton = view.findViewById(R.id.ItemOptionsButton);
            }
    }

    public class MultipleItemsViewHolder extends RecycleViewAdapterListDistplay.ViewHolder {
        private TextView ItemNameDisplay;
        private ImageButton ItemOptionsButton;
        private ImageButton IncreaseCheckedAmountButton;
        private ImageButton DecreaseCheckedAmountButton;
        private TextView CheckedAmountDisplay;

        public MultipleItemsViewHolder(View itemView) {
            super(itemView);
            ItemNameDisplay = itemView.findViewById(R.id.ItemNameDisplayMultiple);
            ItemOptionsButton = itemView.findViewById(R.id.ItemOptionsButtonMultiple);
            IncreaseCheckedAmountButton = itemView.findViewById(R.id.ItemcheckButton);
            DecreaseCheckedAmountButton = itemView.findViewById(R.id.ItemUncheckButton);
            CheckedAmountDisplay = itemView.findViewById(R.id.ItemQuantityDisplay);
        }
    }
}

