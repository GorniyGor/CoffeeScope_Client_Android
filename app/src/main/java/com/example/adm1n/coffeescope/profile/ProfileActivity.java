package com.example.adm1n.coffeescope.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.adm1n.coffeescope.BaseActivityWithToolbar;
import com.example.adm1n.coffeescope.R;

/**
 * Created by adm1n on 05.09.2017.
 */

public class ProfileActivity extends BaseActivityWithToolbar {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText(R.string.profile);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    ProfileFragment.newInstance()).commit();
        }
    }
}
