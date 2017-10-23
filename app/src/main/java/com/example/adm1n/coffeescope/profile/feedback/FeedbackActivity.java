package com.example.adm1n.coffeescope.profile.feedback;

import android.os.Bundle;

import com.example.adm1n.coffeescope.BaseActivityWithToolbar;
import com.example.adm1n.coffeescope.R;

public class FeedbackActivity extends BaseActivityWithToolbar {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText("Обратная связь");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new FeedbackFragment()).commit();
        }
    }
}
