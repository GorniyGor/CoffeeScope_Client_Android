package com.example.adm1n.coffeescope;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm1n.coffeescope.coffee_ingredients.CoffeeIngredientsAdapter;
import com.example.adm1n.coffeescope.main_map.view.MapsActivity;
import com.example.adm1n.coffeescope.models.Ingredients;
import com.example.adm1n.coffeescope.models.Products;
import com.example.adm1n.coffeescope.utils.SpaceItemDecoration;

import java.util.ArrayList;

/**
 * Created by adm1n on 18.07.2017.
 */

public class ViborNapitka extends AppCompatActivity implements CoffeeIngredientsAdapter.OnIngredientsClick {

    private CoffeeIngredientsAdapter mAdapter;
    private RecyclerView recyclerview;
    private Toolbar toolbar;
    private Button mAddButton;
    private Button mPayButton;
    private LinearLayoutManager linearLayoutManager;
    private AppBarLayout app_bar_layout_vibor_napitka;
    private Products mProducts;
    private ArrayList<Ingredients> mIngredientsList;
    private TabLayout mTabLayout;
    private Basket basket;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.napitka_vibor);
        basket = Basket.getInstance();
        if (savedInstanceState == null) {
            mProducts = getIntent().getParcelableExtra(MapsActivity.PRODUCT_EXTRA);
            mIngredientsList = getIntent().getParcelableArrayListExtra(MapsActivity.INGREDIENTS_EXTRA);
            createTabs();
        }
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
        mAdapter = new CoffeeIngredientsAdapter(mIngredientsList, this);
        linearLayoutManager = new LinearLayoutManager(this);
        SpaceItemDecoration decorator = new SpaceItemDecoration(32, true, true);
        recyclerview.addItemDecoration(decorator);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(mAdapter);

        toolbar = (Toolbar) findViewById(R.id.cool_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tvActionBarTitle);
        title.setText(mProducts.getName());
        ImageView backButton = (ImageView) toolbar.findViewById(R.id.ivActionBarBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
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

    void createTabs() {
        mTabLayout = ((TabLayout) findViewById(R.id.tl_coffee_size));
        for (int i = 0; i < mProducts.getSizes().size(); i++) {
            if (mProducts.getSizes().get(i).getSize() != null) {
                TabLayout.Tab tab = mTabLayout.newTab().setIcon(R.drawable.star_icon_1).setText(mProducts.getSizes().get(i).getSize());
                mTabLayout.addTab(tab);
            }
//            if (mProducts.getSizes().get(i).getSize() != null) {
//                Установка активной вкладки с минимальной ценой
//            }
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Ингридиент добавлен", Toast.LENGTH_SHORT).show();
    }
}
