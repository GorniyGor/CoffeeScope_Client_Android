package com.example.adm1n.coffeescope;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.chauthai.swipereveallayout.SwipeRevealLayout;


/**
 * Created by adm1n on 21.07.2017.
 */

public class Order extends AppCompatActivity {
    private SwipeRevealLayout swipeLayout;
    private EditText etOrderCommentField;
    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order);

        swipeLayout = (SwipeRevealLayout) findViewById(R.id.swipeLayout);
        etOrderCommentField = (EditText) findViewById(R.id.et_order_comment_field);
        etOrderCommentField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etOrderCommentField.setFocusable(true);
                etOrderCommentField.setFocusableInTouchMode(true);
                return false;
            }
        });

        toolbar = (Toolbar) findViewById(R.id.order_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Мой заказ");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.star_icon_1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
