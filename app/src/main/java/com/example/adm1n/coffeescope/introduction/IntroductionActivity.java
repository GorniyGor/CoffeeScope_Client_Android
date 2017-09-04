package com.example.adm1n.coffeescope.introduction;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.adm1n.coffeescope.BaseActivityWithToolbar;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.introduction.authorization.IntroductionAuthorizationFragment;

/**
 * Created by adm1n on 04.09.2017.
 */

public class IntroductionActivity extends BaseActivityWithToolbar {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    IntroductionAuthorizationFragment.newInstance()).commit();
        }
    }
}
