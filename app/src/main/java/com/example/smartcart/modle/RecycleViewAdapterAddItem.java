package com.example.smartcart.modle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcart.Activities.ListDisplayModel;
import com.example.smartcart.R;
import com.example.smartcart.data.CallBack;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class RecycleViewAdapterAddItem extends RecyclerView.Adapter<RecycleViewAdapterAddItem.ViewHolder> {

    private ArrayList<Product> DataSet;
    private ArrayList<Product> FilteredDataSet;


    public RecycleViewAdapterAddItem(ArrayList<Product> products) {
        this.DataSet = products;
        this.FilteredDataSet = new ArrayList<>(products);
    }

    public void refreshDataSet(ArrayList<Product> products) {
        this.DataSet = products;
        notifyDataSetChanged();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase().trim();
                ArrayList<Product> results = new ArrayList<>();

                if(query.isEmpty()) {
                    results.addAll(DataSet);
                } else {
                    for (Product product : DataSet) {
                        if (product.getName().toLowerCase().contains(query)) {
                            results.add(product);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = results;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                FilteredDataSet.addAll((ArrayList<Product>) results.values);
                notifyDataSetChanged();
            }
        };
    }
    @NonNull
    @Override
    public RecycleViewAdapterAddItem.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Replace R.layout.shopping_list_item with the actual layout file that contains
        // ListButton, ListHeader and ListOptionsButton.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row_additem_popup, parent, false);
        return new RecycleViewAdapterAddItem.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterAddItem.ViewHolder holder, int position) {
        Product product = DataSet.get(position);
        String currentName = product.getName();


        if (holder.ProductNameDisplay != null) {
            holder.ProductNameDisplay.setText(currentName);
        }

        if (holder.ProductPriceDisplay != null) {
            holder.ProductPriceDisplay.setText(product.getPrice());
        }

        holder.itemView.setOnClickListener(v -> {
             Product SelectedProduct = FilteredDataSet.get(holder.getAdapterPosition());

        });

    }



    @Override
    public int getItemCount() {
        return DataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ProductNameDisplay;
        private TextView ProductPriceDisplay;

        public ViewHolder(View view) {
            super(view);
            ProductNameDisplay = view.findViewById(R.id.ProductNameToAdd);
            ProductPriceDisplay = view.findViewById(R.id.ItemPriceToAdd);

        }
    }

}

