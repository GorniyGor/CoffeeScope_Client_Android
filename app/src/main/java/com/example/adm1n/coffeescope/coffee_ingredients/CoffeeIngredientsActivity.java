package com.example.adm1n.coffeescope.coffee_ingredients;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.adm1n.coffeescope.BaseActivityWithoutToolbar;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.coffee_ingredients.fragment.CoffeeIngredientsFragment;
import com.example.adm1n.coffeescope.models.Products;

public class CoffeeIngredientsActivity extends BaseActivityWithoutToolbar {
    private Integer mPlaceId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlaceId = getIntent().getIntExtra(PLACE_ID_EXTRA, 0);
        Products products = getIntent().getParcelableExtra(PRODUCT_EXTRA);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    CoffeeIngredientsFragment.newInstance(mPlaceId, products)).commit();
        }
    }
}