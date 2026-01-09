package com.example.smartcart.modle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcart.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class AddItemBottomSheet extends BottomSheetDialogFragment {

    private CurrentUser currentUser;


    public interface OnProductSelectedListener {
        void onProductSelected(Product product);
    }
    private OnProductSelectedListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        currentUser = CurrentUser.getInstance();

        View view = inflater.inflate(R.layout.popup_add_item, container, false);

        SearchView searchView = view.findViewById(R.id.searchViewAddItem);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewItemsToAdd);
        RecycleViewAdapterAddItem adapter = new RecycleViewAdapterAddItem(currentUser.getRecommendedProducts());
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        // Initialize your views here and set up any listeners

        return view;
    }

    public void onAttach(@Nullable android.content.Context context) {
        super.onAttach(context);
        if(context instanceof OnProductSelectedListener) {
            listener = (OnProductSelectedListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    + " must implement OnProductSelectedListener");
        }

    }


}
