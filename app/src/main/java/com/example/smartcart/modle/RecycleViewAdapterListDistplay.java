package com.example.smartcart.modle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcart.Activities.ItemOptionsPopup;
import com.example.smartcart.R;
import com.example.smartcart.data.ListDatabase;
import com.example.smartcart.data.UserDatabase;

import java.util.ArrayList;


public class RecycleViewAdapterListDistplay extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Item> DataSet;
    private ShoppingList currentList;
    private UserDatabase userDatabase;
    private ListDatabase listDatabase;
    private ItemOptionsPopup itemOptionsPopup;

    private static final int TYPE_MULTIPLE_ITEMS = 0;
    private static final int TYPE_SINGLE_ITEM = 1;

    public RecycleViewAdapterListDistplay(ShoppingList shoppingList) {
        this.currentList = shoppingList;
        this.DataSet = currentList.getAllItems();
        this.userDatabase = new UserDatabase();
        this.listDatabase = new ListDatabase();
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MultipleItemsViewHolder) {
            bindMultipleItemsViewHolder((MultipleItemsViewHolder) holder, position);
        } else if (holder instanceof SingleItemsViewHolder) {
            bindSingleItemViewHolder((SingleItemsViewHolder) holder, position);
        }
    }


    public void bindSingleItemViewHolder(@NonNull SingleItemsViewHolder holder, int position) {
        Item item = DataSet.get(position);

        holder.ItemNameDisplay.setText(item.getName());


        holder.ItemCheckbox.setOnCheckedChangeListener(null);
        holder.ItemCheckbox.setChecked(item.getChecked());

        holder.ItemCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                item.markItem();
            } else {
                item.unmarkItem();
            }
            userDatabase.updateUser();
            listDatabase.updateList(currentList);
        });

        holder.ItemOptionsButton.setOnClickListener(v -> {
            itemOptionsPopup = new ItemOptionsPopup(currentList , item);
            itemOptionsPopup.show(((androidx.fragment.app.FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager(), "ItemOptionsPopup");
            itemOptionsPopup.setOnPopupStopListener(() -> refreshDataSet());
        });
    }


    public void bindMultipleItemsViewHolder(@NonNull MultipleItemsViewHolder holder, int position) {
        Item item = DataSet.get(position);

        holder.ItemNameDisplay.setText(item.getName());
        holder.CheckedAmountDisplay.setText(item.getAmount() + " / " + item.getAmountChecked());

        holder.IncreaseCheckedAmountButton.setOnClickListener(v -> {

            item.markItem();
            userDatabase.updateUser();
            listDatabase.updateList(currentList);
            notifyItemChanged(position);
        });

        holder.DecreaseCheckedAmountButton.setOnClickListener(v -> {
            item.unmarkItem();
            userDatabase.updateUser();
            listDatabase.updateList(currentList);
            notifyItemChanged(position);
        });

        holder.ItemOptionsButton.setOnClickListener(v -> {
            itemOptionsPopup = new ItemOptionsPopup(currentList , item);
            itemOptionsPopup.show(((androidx.fragment.app.FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager(), "ItemOptionsPopup");
            itemOptionsPopup.setOnPopupStopListener(() -> refreshDataSet());

        });
    }

    @Override
    public int getItemCount() {
        return DataSet.size();
    }


    public static class SingleItemsViewHolder extends RecyclerView.ViewHolder {
        TextView ItemNameDisplay;
        CheckBox ItemCheckbox;
        ImageButton ItemOptionsButton;

        public SingleItemsViewHolder(View view) {
            super(view);
            ItemNameDisplay = view.findViewById(R.id.ItemNameDisplay);
            ItemCheckbox = view.findViewById(R.id.ItemCheckBox);
            ItemOptionsButton = view.findViewById(R.id.ItemOptionsButton);
        }
    }

    public static class MultipleItemsViewHolder extends RecyclerView.ViewHolder {
        TextView ItemNameDisplay;
        ImageButton ItemOptionsButton;
        ImageButton IncreaseCheckedAmountButton;
        ImageButton DecreaseCheckedAmountButton;
        TextView CheckedAmountDisplay;

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