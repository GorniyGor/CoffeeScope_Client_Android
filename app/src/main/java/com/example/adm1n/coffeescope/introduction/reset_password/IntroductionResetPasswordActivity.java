package com.example.adm1n.coffeescope.introduction.reset_password;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.adm1n.coffeescope.BaseActivityWithToolbar;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.introduction.authorization.IntroductionAuthorizationFragment;

/**
 * Created by adm1n on 10.10.2017.
 */

public class IntroductionResetPasswordActivity extends BaseActivityWithToolbar {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText(R.string.reset_password);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    IntroductionResetPasswordFragment.newInstance()).commit();
        }
    }
}
