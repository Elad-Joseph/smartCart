package com.example.smartcart.modle;

import android.content.Context;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smartcart.R;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private String name;
    private Boolean checked;
    private Product product;
    private int amount;

    public Item(String name, Boolean checked , Product product) {
        this.checked = checked;
        this.product = product;
        this.name = product.getName();
        this.amount = 1;
    }



    public Item(String name) {
        this.name = name;
        this.checked = false;
    }

    public String getName() {
        return name;
    }

    public Boolean getChecked() {
        return checked;
    }

    public Map<String, Object> exportToDatabase() {
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("name", name);
        itemMap.put("checked", checked);
        itemMap.put("product", product.getId());
        itemMap.put("amount", amount);
        return itemMap;
    }

    public LinearLayout createItemLayout(Context context) {
        // Outer LinearLayout (vertical)
        LinearLayout itemContainer = new LinearLayout(context);
        itemContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        itemContainer.setOrientation(LinearLayout.VERTICAL);

        // Inner LinearLayout (horizontal)
        LinearLayout rowLayout = new LinearLayout(context);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (75 * context.getResources().getDisplayMetrics().density) // convert 75dp to px
        );
        rowLayout.setLayoutParams(rowParams);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setBackgroundResource(R.drawable.square);

        // FrameLayout (for button + text)
        FrameLayout frameLayout = new FrameLayout(context);
        LinearLayout.LayoutParams frameParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                10f // weight = 10
        );
        frameLayout.setLayoutParams(frameParams);

        // MaterialButton
        com.google.android.material.button.MaterialButton button =
                new com.google.android.material.button.MaterialButton(context);
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        button.setLayoutParams(buttonParams);
        button.setBackgroundColor(context.getResources().getColor(R.color.clear, null));

        // TextView
        TextView textView = new TextView(context);
        FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(textParams);
        textView.setText(this.name);
        textView.setTextSize(25);
        textView.setPadding((int) (8 * context.getResources().getDisplayMetrics().density), 0, 0, 0);
        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

        // Add button and text to FrameLayout
        frameLayout.addView(button);
        frameLayout.addView(textView);

        // Checkbox
        CheckBox checkBox = new CheckBox(context);
        LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1f // weight = 1
        );
        checkBox.setLayoutParams(checkParams);

        // Add FrameLayout and CheckBox to horizontal layout
        rowLayout.addView(frameLayout);
        rowLayout.addView(checkBox);

        // Add horizontal layout to outer layout
        itemContainer.addView(rowLayout);

        return itemContainer;
    }

}
