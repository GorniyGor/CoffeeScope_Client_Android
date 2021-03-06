package com.example.adm1n.coffeescope.search;

import android.support.annotation.Nullable;
import android.os.Bundle;

import com.example.adm1n.coffeescope.BaseActivityWithoutToolbar;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.search.view.SearchFragment;

public class SearchActivity extends BaseActivityWithoutToolbar {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, SearchFragment.newInstance()).commit();
        }
    }
}
