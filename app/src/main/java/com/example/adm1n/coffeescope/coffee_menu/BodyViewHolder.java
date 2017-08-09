package com.example.adm1n.coffeescope.coffee_menu;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.models.Products;
import com.example.adm1n.coffeescope.models.Sizes;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by adm1n on 25.07.2017.
 */

public class BodyViewHolder extends ChildViewHolder {

    private TextView tv_napitok_cost;
    private TextView tv_napitok_name;
    private CardView cvCoffeeAdapterItem;
    private MenuAdapter.OnProductClick mListener;
    private Products mProduct;
    private BigDecimal mTheRealCost;

    public BodyViewHolder(View itemView, MenuAdapter.OnProductClick listener) {
        super(itemView);
        this.mListener = listener;
        tv_napitok_cost = ((TextView) itemView.findViewById(R.id.tv_napitok_cost));
        tv_napitok_name = ((TextView) itemView.findViewById(R.id.tv_napitok_name));
        cvCoffeeAdapterItem = (CardView) itemView.findViewById(R.id.cvCoffeeAdapterItem);
    }

    public void onBind(Products products) {
        mProduct = products;
        BigDecimal tempCost;
        tv_napitok_name.setText(products.getName());
        if (mProduct.getSizes() != null) {
            ArrayList<Sizes> sizes = mProduct.getSizes();
            for (int i = 0; i < sizes.size(); i++) {
                Sizes sizes1 = sizes.get(i);
                if (sizes1.getPrice() != null) {
                    BigDecimal price = BigDecimal.valueOf(sizes1.getPrice());
                    if (sizes1.getDiscount() != null) {
                        BigDecimal percent = BigDecimal.valueOf(sizes1.getDiscount());
                        BigDecimal discountSum = price.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).multiply(percent);
                        tempCost = price.subtract(discountSum);
                    } else {
                        tempCost = price;
                    }
                    if (mTheRealCost != null) {
                        if (tempCost.compareTo(mTheRealCost) >= 0) {
                            mTheRealCost = tempCost;
                        }
                    } else {
                        mTheRealCost = tempCost;
                    }
                }
            }
            if (mTheRealCost != null) {
                tv_napitok_cost.setText(String.valueOf(mTheRealCost.setScale(0, RoundingMode.HALF_DOWN)));
            } else {
                tv_napitok_cost.setText("нет цены");
            }
        } else {
            tv_napitok_cost.setText("нет цены");
        }
        cvCoffeeAdapterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(v, mProduct);
            }
        });
    }
}
