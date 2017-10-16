package com.example.adm1n.coffeescope.introduction.reset_password;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.adm1n.coffeescope.BaseActivityWithToolbar;
import com.example.adm1n.coffeescope.R;

/**
 * Created by adm1n on 10.10.2017.
 */

public class IntroductionResetPasswordActivity extends BaseActivityWithToolbar {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText(R.string.reset_password);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(getDrawable(R.drawable.close_button));
        } else {
            toolbar.setNavigationIcon(getApplicationContext().getResources().getDrawable(R.drawable.close_button));
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    IntroductionResetPasswordFragment.newInstance()).commit();
        }
    }
}
