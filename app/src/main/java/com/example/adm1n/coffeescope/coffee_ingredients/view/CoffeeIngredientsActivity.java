package com.example.adm1n.coffeescope.coffee_ingredients.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Window;

import com.example.adm1n.coffeescope.BaseActivityWithToolbar;
import com.example.adm1n.coffeescope.BaseActivityWithoutToolbar;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.coffee_ingredients.presenter.CoffeeIngredientsPresenter;
import com.example.adm1n.coffeescope.models.Product;
import com.example.adm1n.coffeescope.utils.OnBackPressedListener;

import static com.example.adm1n.coffeescope.BaseFragment.PRODUCT_POSITION_EDIT_EXTRA;

public class CoffeeIngredientsActivity extends BaseActivityWithoutToolbar {

    /**
     * Last redaction 02.11.17 13:03 by Egor
     * (add transaction animation)
     *
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        //--Transaction between the screens animation
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            Transition slide = TransitionInflater.from(this)
                    .inflateTransition(R.transition.transition_slideright);
            getWindow().setEnterTransition(slide);
        }

        super.onCreate(savedInstanceState);
        CoffeeIngredientsFragment.Param mParam = (CoffeeIngredientsFragment.Param) getIntent()
                .getSerializableExtra(PARAM_EXTRA);
        Integer mPlaceId = getIntent().getIntExtra(PLACE_ID_EXTRA, 0);
        Integer product = getIntent().getIntExtra(PRODUCT_ID_EXTRA, 0);
        int position = getIntent().getIntExtra(PRODUCT_POSITION_EDIT_EXTRA, 0);

        /*// Last redaction 03.11.17 11:15 by Egor
        // changing the ingredients' toolbar for the base toolbar
        Product mProduct = new CoffeeIngredientsPresenter().getProduct(product);
        toolbarTitle.setText(mProduct.getName());
        // --*/

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    CoffeeIngredientsFragment.newInstance(mPlaceId, product, mParam, position)).commit();
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