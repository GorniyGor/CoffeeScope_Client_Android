package com.example.adm1n.coffeescope.introduction.authorization;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.adm1n.coffeescope.BaseActivityWithToolbar;
import com.example.adm1n.coffeescope.R;

/**
 * Created by adm1n on 04.09.2017.
 */

public class IntroductionAuthorizationActivity extends BaseActivityWithToolbar {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText(R.string.authorization);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    IntroductionAuthorizationFragment.newInstance()).commit();
        }
    }
}
