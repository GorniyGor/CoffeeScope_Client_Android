package com.example.adm1n.coffeescope;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by adm1n on 25.08.2017.
 */

public class BaseActivityWithToolbar extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_toolbar);
        if (savedInstanceState != null) {
            mLastPlace = savedInstanceState.getParcelable(PLACE_EXTRA);
        }
        toolbar = (Toolbar) findViewById(R.id.cool_toolbar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.tv_action_bar_title);
        toolbarBackButton = (ImageView) toolbar.findViewById(R.id.iv_action_bar_back_button);
        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
