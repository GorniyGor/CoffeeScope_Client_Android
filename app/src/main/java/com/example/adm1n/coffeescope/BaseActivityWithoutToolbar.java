package com.example.adm1n.coffeescope;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by adm1n on 25.08.2017.
 */

public class BaseActivityWithoutToolbar extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_without_toolbar);
    }
}
