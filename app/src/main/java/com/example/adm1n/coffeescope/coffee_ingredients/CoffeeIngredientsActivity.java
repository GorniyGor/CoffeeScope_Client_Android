package com.example.adm1n.coffeescope.coffee_ingredients;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm1n.coffeescope.Order;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.main_map.view.MapsActivity;
import com.example.adm1n.coffeescope.models.Basket;
import com.example.adm1n.coffeescope.models.Ingredients;
import com.example.adm1n.coffeescope.models.Products;
import com.example.adm1n.coffeescope.utils.SpaceItemDecoration;

import java.util.ArrayList;

/**
 * Created by adm1n on 18.07.2017.
 */

public class CoffeeIngredientsActivity extends AppCompatActivity implements CoffeeIngredientsAdapter.OnIngredientsClick {

    private CoffeeIngredientsAdapter mAdapter;
    private RecyclerView recyclerview;
    private Toolbar toolbar;
    private ImageView mAddButton;
    private Button mPayButton;
    private LinearLayoutManager linearLayoutManager;
    private AppBarLayout app_bar_layout_vibor_napitka;
    private Products mProducts;
    private ArrayList<Ingredients> mIngredientsList;
    private TabLayout mTabLayout;
    private Basket basket;
    private Integer mPlaceId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.napitka_vibor);
        if (savedInstanceState == null) {
            mProducts = getIntent().getParcelableExtra(MapsActivity.PRODUCT_EXTRA);
            mIngredientsList = getIntent().getParcelableArrayListExtra(MapsActivity.INGREDIENTS_EXTRA);
            mPlaceId = getIntent().getIntExtra(MapsActivity.PLACE_ID_EXTRA, 0);
            initBasket(mPlaceId);
            createTabs();
        }
        mAddButton = (ImageView) findViewById(R.id.btn_add_napitok);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CoffeeIngredientsActivity.this, "Напиток добавлен!", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        mPayButton = (Button) findViewById(R.id.btn_pay_napitok);
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Order.class);
                intent.putExtra(MapsActivity.PLACE_ID_EXTRA, mPlaceId);
                basket = new Basket(mPlaceId);
                //создаем в реалме запись корзины
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
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (mTabLayout.getTabCount() != 0) {
                            TabLayout.Tab tabAt = mTabLayout.getTabAt(0);
                            tabAt.select();
                        }
                    }
                }, 100);
    }

    void createTabs() {
        mTabLayout = ((TabLayout) findViewById(R.id.tl_coffee_size));
        for (int i = 0; i < mProducts.getSizes().size(); i++) {
            if (mProducts.getSizes().get(i).getSize() != null) {
                switch (mProducts.getSizes().get(i).getSize()) {
                    case "s":
                        View viewS = getLayoutInflater().inflate(R.layout.customtab, null);
                        viewS.findViewById(R.id.icon).setBackgroundResource(R.drawable.size_s_inactive);
                        TextView tvS = ((TextView) viewS.findViewById(R.id.tvCost));
                        tvS.setText(mProducts.getSizes().get(i).getPrice() + " P");
                        mTabLayout.addTab(mTabLayout.newTab().setCustomView(viewS).setContentDescription("s"));
                        break;
                    case "m":
                        View viewM = getLayoutInflater().inflate(R.layout.customtab, null);
                        viewM.findViewById(R.id.icon).setBackgroundResource(R.drawable.size_m_inactive);
                        TextView tvM = ((TextView) viewM.findViewById(R.id.tvCost));
                        tvM.setText(mProducts.getSizes().get(i).getPrice() + " P");
                        mTabLayout.addTab(mTabLayout.newTab().setCustomView(viewM).setContentDescription("m"));
                        break;
                    case "l":
                        View viewL = getLayoutInflater().inflate(R.layout.customtab, null);
                        viewL.findViewById(R.id.icon).setBackgroundResource(R.drawable.size_l_inactive);
                        TextView tvL = ((TextView) viewL.findViewById(R.id.tvCost));
                        tvL.setText(mProducts.getSizes().get(i).getPrice() + " P");
                        mTabLayout.addTab(mTabLayout.newTab().setCustomView(viewL).setContentDescription("l"));
                        break;
                    default:
                        break;
                }
            }
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (String.valueOf(tab.getContentDescription())) {
                    case "s":
//                        ImageView viewByIdS = (ImageView) tab.getCustomView().findViewById(R.id.icon);
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_s_active);
//                        viewByIdS.setImageResource(R.drawable.size_s_active);
                        break;
                    case "m":
//                        ImageView viewByIdM = (ImageView) tab.getCustomView().findViewById(R.id.icon);
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_m_active);
//                        viewByIdM.setImageResource(R.drawable.size_m_active);
                        break;
                    case "l":
//                        ImageView viewByIdL = (ImageView) tab.getCustomView().findViewById(R.id.icon);
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_l_active);
//                        viewByIdL.setImageResource(R.drawable.size_l_active);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (String.valueOf(tab.getContentDescription())) {
                    case "s":
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_s_inactive);
//                        ImageView viewByIdS = (ImageView) tab.getCustomView().findViewById(R.id.icon);
//                        viewByIdS.setImageResource(R.drawable.size_s_inactive);
                        break;
                    case "m":
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_m_inactive);
//                        ImageView viewByIdM = (ImageView) tab.getCustomView().findViewById(R.id.icon);
//                        viewByIdM.setImageResource(R.drawable.size_m_inactive);
                        break;
                    case "l":
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_l_inactive);
//                        ImageView viewByIdL = (ImageView) tab.getCustomView().findViewById(R.id.icon);
//                        viewByIdL.setImageResource(R.drawable.size_l_inactive);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    void initBasket(Integer basketId) {

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Ингридиент добавлен", Toast.LENGTH_SHORT).show();
    }
}
