package com.example.adm1n.coffeescope.coffee_menu;

import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.TextView;

import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.custom_view.CustomTextView;
import com.example.adm1n.coffeescope.models.Products;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * Created by adm1n on 25.07.2017.
 */

public class BodyViewHolder extends ChildViewHolder {

    private CustomTextView tv_napitok_cost;
    private TextView tv_napitok_name;
    private TextView tv_napitok_cost_with_discount;
    private CardView cvCoffeeAdapterItem;
    private MenuAdapter.OnProductClick mListener;
    private Products mProduct;
    private Integer mTheRealCost;

    public BodyViewHolder(View itemView, MenuAdapter.OnProductClick listener) {
        super(itemView);
        this.mListener = listener;
        tv_napitok_cost = ((CustomTextView) itemView.findViewById(R.id.tv_napitok_cost));
        tv_napitok_name = ((TextView) itemView.findViewById(R.id.tv_napitok_name));
        cvCoffeeAdapterItem = (CardView) itemView.findViewById(R.id.cvCoffeeAdapterItem);
        tv_napitok_cost_with_discount = (TextView) itemView.findViewById(R.id.tv_napitok_cost_with_discount);
    }

    public void onBind(Products products) {
        mProduct = products;
        Integer lowCostWithDiscount = null;
        tv_napitok_name.setText(products.getName());
        if (mProduct.getSizes() != null) {
            for (int i = 0; i < mProduct.getSizes().size(); i++) {
                if (mProduct.getSizes().get(i).getPrice_with_discount() != null) {
                    if (lowCostWithDiscount == null) {
                        lowCostWithDiscount = mProduct.getSizes().get(i).getPrice_with_discount();
                        mTheRealCost = mProduct.getSizes().get(i).getPrice();
                    } else {
                        if (lowCostWithDiscount <= mProduct.getSizes().get(i).getPrice_with_discount()) {
                            break;
                        } else {
                            lowCostWithDiscount = mProduct.getSizes().get(i).getPrice_with_discount();
                            mTheRealCost = mProduct.getSizes().get(i).getPrice();
                        }
                    }
                }
            }
            if (lowCostWithDiscount != null) {
                if (!lowCostWithDiscount.equals(mTheRealCost)) {
                    tv_napitok_cost_with_discount.setVisibility(View.VISIBLE);
                    tv_napitok_cost_with_discount.setText(String.valueOf(lowCostWithDiscount));
                    tv_napitok_cost.setText(String.valueOf(mTheRealCost));
                } else {
                    tv_napitok_cost_with_discount.setText(String.valueOf(mTheRealCost));
                    tv_napitok_cost.setVisibility(View.GONE);
                }
            } else {
                tv_napitok_cost_with_discount.setText("нет цены");
                tv_napitok_cost.setVisibility(View.GONE);
            }
        } else {
            tv_napitok_cost_with_discount.setText("нет цены");
            tv_napitok_cost.setVisibility(View.GONE);
        }
        cvCoffeeAdapterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(v, mProduct);
            }
        });
    }
}
