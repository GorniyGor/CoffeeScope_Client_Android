package com.example.adm1n.coffeescope.coffee_ingredients.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm1n.coffeescope.BaseActivity;
import com.example.adm1n.coffeescope.BaseFragment;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.coffee_ingredients.CoffeeIngredientsAdapter;
import com.example.adm1n.coffeescope.models.Ingredients;
import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.models.Products;
import com.example.adm1n.coffeescope.models.basket.Basket;
import com.example.adm1n.coffeescope.models.basket.BasketProducts;
import com.example.adm1n.coffeescope.order.OrderActivity;
import com.example.adm1n.coffeescope.utils.SpaceItemDecoration;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;
import io.realm.RealmList;

/**
 * Created by adm1n on 25.08.2017.
 */

public class CoffeeIngredientsFragment extends BaseFragment implements CoffeeIngredientsAdapter.OnIngredientsClick {

    private CoffeeIngredientsAdapter mAdapter;
    private RecyclerView recyclerview;
    private ImageView mAddButton;
    private Button mPayButton;
    private LinearLayoutManager linearLayoutManager;
    private AppBarLayout app_bar_layout_vibor_napitka;
    private Products mProducts;
    private RealmList<Ingredients> mIngredientsList = new RealmList<>();
    private TabLayout mTabLayout;
    private Basket mBasket;
    private BasketProducts mBasketProducts = new BasketProducts();
    private Integer mPlaceId;
    private Integer mProductId;

    private ImageView ivAddProductCount;
    private ImageView ivRemoveProductCount;
    private TextView tvProductCount;
    private TextView tvActionBarTitle;

    public static CoffeeIngredientsFragment newInstance(Integer placeId, Products products) {
        Bundle args = new Bundle();
        args.putInt(PLACE_ID_EXTRA, placeId);
        args.putParcelable(PRODUCT_EXTRA, products);
        CoffeeIngredientsFragment fragment = new CoffeeIngredientsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mProducts = getArguments().getParcelable(PRODUCT_EXTRA);
            mPlaceId = getArguments().getInt(PLACE_ID_EXTRA);
            mProductId = mProducts.getId();
            getIngredients();
            initBasket(mPlaceId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coffee_ingredients, null);
        initView(view);
        return view;
    }

    void initView(View v) {
        ivAddProductCount = (ImageView) v.findViewById(R.id.iv_count_control_plus);
        ivRemoveProductCount = (ImageView) v.findViewById(R.id.iv_count_control_minus);
        tvProductCount = (TextView) v.findViewById(R.id.tv_product_count_product_count);
        tvActionBarTitle = (TextView) v.findViewById(R.id.tv_action_bar_title);
        mAddButton = (ImageView) v.findViewById(R.id.btn_add_napitok);
        mPayButton = (Button) v.findViewById(R.id.btn_pay_napitok);
        recyclerview = (RecyclerView) v.findViewById(R.id.rv_coffee_ingredients);
        app_bar_layout_vibor_napitka = (AppBarLayout) v.findViewById(R.id.app_bar_layout_vibor_napitka);
        mTabLayout = ((TabLayout) v.findViewById(R.id.tl_coffee_size));
    }

    void initRecycler() {
        mAdapter = new CoffeeIngredientsAdapter(mLastPlace.getIngredients(), this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        SpaceItemDecoration decorator = new SpaceItemDecoration(32, true, true);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(mAdapter);
        recyclerview.addItemDecoration(decorator);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecycler();
        tvActionBarTitle.setText(mProducts.getName());
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //добавляем заказ
                saveBasket();
                //// TODO: 25.08.2017 добавить бекер
//                super.onBackPressed();
            }
        });
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBasket();
                Intent intent = new Intent(getContext(), OrderActivity.class);
                intent.putExtra(BaseActivity.PLACE_ID_EXTRA, mPlaceId);
                startActivity(intent);
            }
        });
        if (mProducts.getSizes() != null) {
            createTabs();
        }
        setRxView();
        showSumma();
    }


    void createTabs() {
        for (int i = 0; i < mProducts.getSizes().size(); i++) {
            if (mProducts.getSizes().get(i).getSize() != null) {
                switch (mProducts.getSizes().get(i).getSize()) {
                    case "s":
                        View viewS = LayoutInflater.from(getContext()).inflate(R.layout.customtab, null);
                        viewS.findViewById(R.id.icon).setBackgroundResource(R.drawable.size_s_inactive);
                        TextView tvS = ((TextView) viewS.findViewById(R.id.tvCost));
                        tvS.setText(String.valueOf(mProducts.getSizes().get(i).getPrice()));
                        mTabLayout.addTab(mTabLayout.newTab().setCustomView(viewS).setContentDescription("s"));
                        break;
                    case "m":
                        View viewM = LayoutInflater.from(getContext()).inflate(R.layout.customtab, null);
                        viewM.findViewById(R.id.icon).setBackgroundResource(R.drawable.size_m_inactive);
                        TextView tvM = ((TextView) viewM.findViewById(R.id.tvCost));
                        tvM.setText(String.valueOf(mProducts.getSizes().get(i).getPrice()));
                        mTabLayout.addTab(mTabLayout.newTab().setCustomView(viewM).setContentDescription("m"));
                        break;
                    case "l":
                        View viewL = LayoutInflater.from(getContext()).inflate(R.layout.customtab, null);
                        viewL.findViewById(R.id.icon).setBackgroundResource(R.drawable.size_l_inactive);
                        TextView tvL = ((TextView) viewL.findViewById(R.id.tvCost));
                        tvL.setText(String.valueOf(mProducts.getSizes().get(i).getPrice()));
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
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_s_active);
                        TextView viewByIdS = (TextView) tab.getCustomView().findViewById(R.id.tvCost);
                        mBasketProducts.setCost(Integer.valueOf(viewByIdS.getText().toString()));
                        break;
                    case "m":
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_m_active);
                        TextView viewByIdM = (TextView) tab.getCustomView().findViewById(R.id.tvCost);
                        mBasketProducts.setCost(Integer.valueOf(viewByIdM.getText().toString()));
                        break;
                    case "l":
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_l_active);
                        TextView viewByIdL = (TextView) tab.getCustomView().findViewById(R.id.tvCost);
                        mBasketProducts.setCost(Integer.valueOf(viewByIdL.getText().toString()));
                        break;
                    default:
                        break;
                }
                showSumma();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (String.valueOf(tab.getContentDescription())) {
                    case "s":
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_s_inactive);
                        break;
                    case "m":
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_m_inactive);
                        break;
                    case "l":
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_l_inactive);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (String.valueOf(tab.getContentDescription())) {
                    case "s":
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_s_active);
                        TextView viewByIdS = (TextView) tab.getCustomView().findViewById(R.id.tvCost);
                        mBasketProducts.setCost(Integer.valueOf(viewByIdS.getText().toString()));
                        break;
                    case "m":
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_m_active);
                        TextView viewByIdM = (TextView) tab.getCustomView().findViewById(R.id.tvCost);
                        mBasketProducts.setCost(Integer.valueOf(viewByIdM.getText().toString()));
                        break;
                    case "l":
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_l_active);
                        TextView viewByIdL = (TextView) tab.getCustomView().findViewById(R.id.tvCost);
                        mBasketProducts.setCost(Integer.valueOf(viewByIdL.getText().toString()));
                        break;
                    default:
                        break;
                }
                showSumma();
            }
        });
        if (mTabLayout.getTabCount() != 0) {
            mTabLayout.getTabAt(0).select();
        }
    }

    void initBasket(Integer basketId) {
        mBasket = mRealm.copyFromRealm(mRealm.where(Basket.class).equalTo("mBasketId", basketId).findFirst());

        mBasketProducts = new BasketProducts();
        mBasketProducts.setProductId(mProductId);
        mBasketProducts.setCount(1);
        mBasketProducts.setCost(0);
        mBasketProducts.setName(mProducts.getName());
        mBasket.getmBasketProductsList().add(mBasketProducts);
    }

    @Override
    //Клик по ингредиенту
    public void onIngredientsClick(View v, int position) {
        int realPosotion = position - 1;
        //Ингредиент по которому мы кликнули
        Ingredients ingredients = mIngredientsList.get(realPosotion);
        //Ингредиенты в корзине
        RealmList<Ingredients> ingredientsList = mBasketProducts.getmIngredientsList();
        ImageView viewById = (ImageView) v.findViewById(R.id.iv_napitok_add);
        if (!ingredientsList.contains(ingredients)) {
            mBasketProducts.getmIngredientsList().add(ingredients);
            viewById.setImageResource(R.drawable.done_icon);
        } else {
            viewById.setImageResource(R.drawable.add_icon);
            mBasketProducts.getmIngredientsList().remove(ingredients);
        }
        showSumma();
    }

    void getIngredients() {
        Place place = mRealm.where(Place.class)
                .equalTo("id", mPlaceId)
                .findFirst();
        mLastPlace = mRealm.copyFromRealm(place);
        if (mLastPlace.getIngredients() != null) {
            mIngredientsList.addAll(mLastPlace.getIngredients());
        }
    }

    void setRxView() {
        RxView.clicks(ivAddProductCount).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                int i = Integer.parseInt(tvProductCount.getText().toString());
                i++;
                tvProductCount.setText(String.valueOf(i));
                mBasketProducts.setCount(i);
                showSumma();
            }
        });

        RxView.clicks(ivRemoveProductCount).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                int i = Integer.parseInt(tvProductCount.getText().toString());
                if (i != 1) {
                    i--;
                    tvProductCount.setText(String.valueOf(i));
                    mBasketProducts.setCount(i);
                } else {
                    Toast.makeText(getContext(), "ТЫ НА ДНЕ", Toast.LENGTH_SHORT).show();
                }
                showSumma();
            }
        });
    }

    void showSumma() {
        if (mBasket != null) {
            mPayButton.setText("Оплатить (" + String.valueOf(mBasket.getSumma(mBasket)) + ")");
        }
    }

    void saveBasket() {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(mBasket);
        mRealm.commitTransaction();
    }
}
