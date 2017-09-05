package com.example.adm1n.coffeescope.introduction.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.adm1n.coffeescope.BaseActivityWithToolbar;
import com.example.adm1n.coffeescope.R;

/**
 * Created by adm1n on 04.09.2017.
 */

public class IntroductionRegistrationActivity extends BaseActivityWithToolbar {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText(R.string.registration);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    IntroductionRegistrationFragment.newInstance()).commit();
        }
    }
}
