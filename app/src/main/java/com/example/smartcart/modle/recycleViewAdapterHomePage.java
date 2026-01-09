package com.example.smartcart.modle;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcart.Activities.ListDisplayModel;
import com.example.smartcart.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class recycleViewAdapterHomePage extends RecyclerView.Adapter<recycleViewAdapterHomePage.ViewHolder> {

    private ArrayList<ShoppingList> DataSet;
    private ImportedShoppingLists importedShoppingLists;

    public recycleViewAdapterHomePage() {
        this.importedShoppingLists = ImportedShoppingLists.getInstance();
        this.DataSet = importedShoppingLists.getAllLists();
    }

    public void refreshDataSet() {
        this.DataSet = importedShoppingLists.getAllLists();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Replace R.layout.shopping_list_item with the actual layout file that contains
        // ListButton, ListHeader and ListOptionsButton.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_homepage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingList shoppingList = DataSet.get(position);
        String currentName = shoppingList.getName();

//        Idk what is this
//        if (holder.ListButton != null) {
//            holder.ListButton.setText(currentName);
//        }
        if (holder.ListHeader != null) {
            holder.ListHeader.setText(currentName);
        }

        final ShoppingList currentList = importedShoppingLists.get(position);
        if (holder.ListButton != null) {
            holder.ListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ListDisplayModel.class);
                    intent.putExtra("CurrentListId", currentList.getId());
                    context.startActivity(intent);
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                }
            });
        }

        if (holder.OptionsButton != null) {
            holder.OptionsButton.setOnClickListener(new View.OnClickListener() {
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
        public TextView ListHeader;
        private MaterialButton ListButton;
        private ImageButton OptionsButton;

        public ViewHolder(View view) {
            super(view);
            ListButton = view.findViewById(R.id.ListButton);
            ListHeader = view.findViewById(R.id.ListHeader);
            OptionsButton = view.findViewById(R.id.ListOptionsButton);
        }
    }
}
