package com.example.adm1n.coffeescope.profile.change_password;

import android.os.Bundle;

import com.example.adm1n.coffeescope.BaseActivityWithToolbar;
import com.example.adm1n.coffeescope.R;

public class ChangePasswordActivity extends BaseActivityWithToolbar {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText("Изменить пароль");
        toolbarBackButton.setImageResource(R.drawable.close_button);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ChangePasswordFragment()).commit();
        }
    }
}
