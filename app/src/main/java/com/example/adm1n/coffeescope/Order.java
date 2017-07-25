package com.example.adm1n.coffeescope;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.chauthai.swipereveallayout.SwipeRevealLayout;


/**
 * Created by adm1n on 21.07.2017.
 */

public class Order extends AppCompatActivity {
    SwipeRevealLayout swipeLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order);

        swipeLayout = (SwipeRevealLayout) findViewById(R.id.swipeLayout);

    }
}
