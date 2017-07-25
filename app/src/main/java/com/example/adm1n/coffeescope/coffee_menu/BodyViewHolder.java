package com.example.adm1n.coffeescope.coffee_menu;

import android.view.View;
import android.widget.TextView;

import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.model.Products;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * Created by adm1n on 25.07.2017.
 */

public class BodyViewHolder extends ChildViewHolder {

    private TextView tv_napitok_cost;
    private TextView tv_napitok_name;

    public BodyViewHolder(View itemView) {
        super(itemView);
        tv_napitok_cost = ((TextView) itemView.findViewById(R.id.tv_napitok_cost));
        tv_napitok_name = ((TextView) itemView.findViewById(R.id.tv_napitok_name));
    }

    public void onBind(Products products) {
        tv_napitok_name.setText(products.getName());
        tv_napitok_cost.setText(String.valueOf(products.getPrice()));
    }
}
