package com.example.adm1n.coffeescope;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.adm1n.coffeescope.utils.SpaceItemDecoration;

/**
 * Created by adm1n on 18.07.2017.
 */

public class ViborNapitka extends AppCompatActivity {

    private CoffeeAdapter mAdapter;
    private RecyclerView recyclerview;
    private Toolbar toolbar;
    private Button mAddButton;
    private Button mPayButton;
    private LinearLayoutManager linearLayoutManager;
    private AppBarLayout app_bar_layout_vibor_napitka;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.napitka_vibor);

        mAddButton = (Button) findViewById(R.id.btn_add_napitok);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViborNapitka.this, "Напиток добавлен!", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        mPayButton = (Button) findViewById(R.id.btn_pay_napitok);
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Order.class);
                startActivity(intent);
            }
        });

        recyclerview = (RecyclerView) findViewById(R.id.rv);
        app_bar_layout_vibor_napitka = (AppBarLayout) findViewById(R.id.app_bar_layout_vibor_napitka);
        mAdapter = new CoffeeAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);
        SpaceItemDecoration decorator = new SpaceItemDecoration(32, true, true);
        recyclerview.addItemDecoration(decorator);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(mAdapter);


        toolbar = (Toolbar) findViewById(R.id.cool_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Капучино");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.star_icon_1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisiblePosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if (firstVisiblePosition == 0) {
                        app_bar_layout_vibor_napitka.setExpanded(true, true);
                    }
                }
            }
        });
    }


}
