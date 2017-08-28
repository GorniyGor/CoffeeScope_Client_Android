package com.example.adm1n.coffeescope.coffee_ingredients.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.adm1n.coffeescope.BaseActivityWithoutToolbar;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.coffee_ingredients.view.CoffeeIngredientsFragment;
import com.example.adm1n.coffeescope.models.Product;
import com.example.adm1n.coffeescope.utils.OnBackPressedListener;

import static com.example.adm1n.coffeescope.BaseFragment.PRODUCT_POSITION_EDIT_EXTRA;

public class CoffeeIngredientsActivity extends BaseActivityWithoutToolbar {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoffeeIngredientsFragment.Param mParam = (CoffeeIngredientsFragment.Param) getIntent().getSerializableExtra(PARAM_EXTRA);
        Integer mPlaceId = getIntent().getIntExtra(PLACE_ID_EXTRA, 0);
        Integer product = getIntent().getIntExtra(PRODUCT_ID_EXTRA, 0);
        int position = getIntent().getIntExtra(PRODUCT_POSITION_EDIT_EXTRA, 0);

        if (savedInstanceState == null) {
            if (mParam != null && mParam.equals(CoffeeIngredientsFragment.Param.Edit)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        CoffeeIngredientsFragment.newInstance(mPlaceId, product, mParam, position)).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        CoffeeIngredientsFragment.newInstance(mPlaceId, product, mParam, position)).commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment : fm.getFragments()) {
            if (fragment instanceof OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
//            backPressedListener.onBackPressed();
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}