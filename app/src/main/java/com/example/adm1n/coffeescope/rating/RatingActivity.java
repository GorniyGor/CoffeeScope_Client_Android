package com.example.adm1n.coffeescope.rating;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.adm1n.coffeescope.BaseActivityWithToolbar;
import com.example.adm1n.coffeescope.R;

/**
 * Created by adm1n on 31.10.2017.
 */

public class RatingActivity extends BaseActivityWithToolbar {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText(R.string.rating);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    RatingFragment.newInstance()).commit();
        }
    }
}
