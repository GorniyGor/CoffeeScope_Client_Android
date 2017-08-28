package com.example.adm1n.coffeescope.order.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.adm1n.coffeescope.BaseActivityWithToolbar;
import com.example.adm1n.coffeescope.R;

/**
 * Created by adm1n on 21.07.2017.
 */

public class OrderActivity extends BaseActivityWithToolbar {

    private Integer placeId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placeId = getIntent().getIntExtra(PLACE_ID_EXTRA, 0);

        toolbarTitle.setText(R.string.my_order);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, OrderFragment.newInstance(placeId)).commit();
        }
    }
}
