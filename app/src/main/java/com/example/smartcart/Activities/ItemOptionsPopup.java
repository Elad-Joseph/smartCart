package com.example.smartcart.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.smartcart.R;
import com.example.smartcart.data.ListDatabase;
import com.example.smartcart.data.UserDatabase;
import com.example.smartcart.modle.CurrentUser;
import com.example.smartcart.modle.ImportedShoppingLists;
import com.example.smartcart.modle.Item;
import com.example.smartcart.modle.ShoppingList;
import com.google.android.material.button.MaterialButton;

public class ItemOptionsPopup extends DialogFragment {

    private View PopupView;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private Item currentItem;
    private ShoppingList currentList;

    private UserDatabase userDatabase;
    private ListDatabase listDatabase;
    private ImportedShoppingLists importedShoppingLists;
    private CurrentUser currentUser;

    private TextView title;
    private EditText ItemName;
    private ImageButton AddQuantityButton;
    private ImageButton RemoveQuantityButton;
    private TextView ItemQuantityDisplay;
    private ImageButton UncheckItemButton;
    private ImageButton CheckItemButton;
    private TextView ItemCheckedQuantityTextView;
    private FrameLayout ProductDisplayFrame;
    private LinearLayout ProductDetailsLayout;
    private TextView ProductNameTextView;
    private TextView ProductIdTextView;
    private TextView ProductPriceTextView;
    private MaterialButton SaveChangesButton;
    private MaterialButton DeleteItemButton;
    private boolean DeleteItemConfirmation = false;

    public ItemOptionsPopup(ShoppingList list, Item item) {
        currentItem = item;
        currentList = list;
    }

    public interface OnPopupStopListener {
        void onPopupStopped();
    }

    private ItemOptionsPopup.OnPopupStopListener stopListener;

    // 2. Method to set the listener from the Activity
    public void setOnPopupStopListener(ItemOptionsPopup.OnPopupStopListener listener) {
        this.stopListener = listener;
    }

    @Override
    public void onDismiss(@NonNull android.content.DialogInterface dialog) {
        super.onDismiss(dialog);
        // 3. Notify the Activity that we stopped
        if (stopListener != null) {
            stopListener.onPopupStopped();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Item Options");
        inflater = requireActivity().getLayoutInflater();
        PopupView = inflater.inflate(R.layout.item_options_popup, null);
        builder.setView(PopupView);

        userDatabase = new UserDatabase();
        listDatabase = new ListDatabase();
        currentUser = CurrentUser.getInstance();
        importedShoppingLists = ImportedShoppingLists.getInstance();

        SetupIds();
        SetupListeners();

        title.setText(currentItem.getName());
        ItemName.setText(currentItem.getName());
        ItemQuantityDisplay.setText(String.valueOf(currentItem.getAmount()));
        ItemCheckedQuantityTextView.setText(String.valueOf(currentItem.getAmountChecked()));
        ProductNameTextView.setText(currentItem.getProduct().getName());
        ProductIdTextView.setText("ID: " + currentItem.getProduct().getId());
        ProductPriceTextView.setText("Price: $" + currentItem.getProduct().getPrice());

        return builder.create();
    }


    public void SetupIds(){
        title = PopupView.findViewById(R.id.item_options_popup_title);
        ItemName = PopupView.findViewById(R.id.item_optionsPopup_ChangeName);
        AddQuantityButton = PopupView.findViewById(R.id.item_optionsPopup_AddQuantityButton);
        RemoveQuantityButton = PopupView.findViewById(R.id.item_optionsPopup_DecressQuantityButton);
        ItemQuantityDisplay = PopupView.findViewById(R.id.item_optionsPopup_quantityDisplay);
        UncheckItemButton = PopupView.findViewById(R.id.item_optionsPopup_uncheckButton);
        CheckItemButton = PopupView.findViewById(R.id.item_optionsPopup_checkButton);
        ItemCheckedQuantityTextView = PopupView.findViewById(R.id.item_optionsPopup_CheckedQuantityDisplay);
        ProductDisplayFrame = PopupView.findViewById(R.id.item_optionsPopup_DisplayProduct);
        ProductDetailsLayout = PopupView.findViewById(R.id.item_optionsPopup_ProductDetailsLayout);
        ProductNameTextView = PopupView.findViewById(R.id.item_optionsPopup_ProductNameDisplay);
        ProductIdTextView = PopupView.findViewById(R.id.item_optionsPopup_ProductIdDisplay);
        ProductPriceTextView = PopupView.findViewById(R.id.item_optionsPopup_ProductPriceDisplay);

        SaveChangesButton = PopupView.findViewById(R.id.item_optionsPopup_SaveButton);
        DeleteItemButton = PopupView.findViewById(R.id.item_optionsPopup_DeleteButton);
    }

    public void SetupListeners() {
        ProductDisplayFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProductDetailsLayout.getVisibility() == View.VISIBLE) {
                    ProductDetailsLayout.setVisibility(View.GONE);
                } else {
                    ProductDetailsLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        AddQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.addAmount(1);
                ItemQuantityDisplay.setText(String.valueOf(currentItem.getAmount()));
            }
        });

        RemoveQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.decreaseAmount(1);
                ItemQuantityDisplay.setText(String.valueOf(currentItem.getAmount()));
            }
        });

        CheckItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.markItem();
                ItemCheckedQuantityTextView.setText(String.valueOf(currentItem.getAmountChecked()));
            }
        });

        UncheckItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.unmarkItem();
                ItemCheckedQuantityTextView.setText(String.valueOf(currentItem.getAmountChecked()));
            }
        });

        SaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.setName(ItemName.getText().toString());
                currentUser.setImportedListsToImportedShoppingLists();
                listDatabase.updateAllLists();
                userDatabase.updateUser();
                dismiss();
            }
        });

        DeleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DeleteItemConfirmation){
                    currentList.remove(currentItem);
                    currentUser.setImportedListsToImportedShoppingLists();
                    listDatabase.updateAllLists();
                    userDatabase.updateUser();
                    dismiss();
                }
                else{
                    Toast.makeText(getContext().getApplicationContext(), "press again to confirm", Toast.LENGTH_SHORT).show();
                    DeleteItemConfirmation = true;
                    new Handler(Looper.getMainLooper()).postDelayed(() -> DeleteItemConfirmation = false, 2000);

                }
            }
            });
        }


}

