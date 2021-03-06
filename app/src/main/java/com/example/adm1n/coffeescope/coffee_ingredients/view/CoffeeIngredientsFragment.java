package com.example.adm1n.coffeescope.coffee_ingredients.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm1n.coffeescope.BaseActivity;
import com.example.adm1n.coffeescope.BaseFragment;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.coffee_ingredients.CoffeeIngredientsAdapter;
import com.example.adm1n.coffeescope.coffee_ingredients.presenter.CoffeeIngredientsPresenter;
import com.example.adm1n.coffeescope.models.Ingredients;
import com.example.adm1n.coffeescope.models.Product;
import com.example.adm1n.coffeescope.models.basket.Basket;
import com.example.adm1n.coffeescope.models.basket.BasketProducts;
import com.example.adm1n.coffeescope.order.view.OrderActivity;
import com.example.adm1n.coffeescope.utils.SpaceItemDecoration;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;
import io.realm.RealmList;

public class CoffeeIngredientsFragment extends BaseFragment implements CoffeeIngredientsAdapter.OnIngredientsClick {

    public enum Param {Add, Edit}

    private Param mParam;
    private CoffeeIngredientsAdapter mAdapter;
    private RecyclerView recyclerview;
    private ImageView mAddButton;
    private Button mPayButton;
    private Button mButtonSave;
    private LinearLayoutManager linearLayoutManager;
    private AppBarLayout app_bar_layout_vibor_napitka;
    private Product mProduct;
    private RealmList<Ingredients> mIngredientsList = new RealmList<>();
    private TabLayout mTabLayout;
    private Basket mBasket;
    private BasketProducts mBasketProducts = new BasketProducts();
    private Integer mPlaceId;
    private Integer mProductId;
    private CoffeeIngredientsPresenter presenter;
    private View btnLayout;
    private LinearLayout llBack;
    private TextView toolbarBackText;
    private ImageView toolbarBackImage;
    private ImageView ivAddProductCount;
    private ImageView ivRemoveProductCount;
    private TextView tvProductCount;
    private TextView tvActionBarTitle;
    private int mEditProductPosition;

    public static CoffeeIngredientsFragment newInstance(Integer placeId, Integer productId, Param param, int position) {
        Bundle args = new Bundle();
        args.putInt(PLACE_ID_EXTRA, placeId);
        args.putSerializable(PARAM_EXTRA, param);
        args.putInt(PRODUCT_EXTRA, productId);
        args.putInt(PRODUCT_POSITION_EDIT_EXTRA, position);
        CoffeeIngredientsFragment fragment = new CoffeeIngredientsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CoffeeIngredientsPresenter();
        if (savedInstanceState == null) {
            mParam = (Param) getArguments().getSerializable(PARAM_EXTRA);
            mProductId = getArguments().getInt(PRODUCT_EXTRA);
            mProduct = presenter.getProduct(mProductId);
            mPlaceId = getArguments().getInt(PLACE_ID_EXTRA);
            mEditProductPosition = getArguments().getInt(PRODUCT_POSITION_EDIT_EXTRA);
            mBasket = presenter.getBasket(mPlaceId);
            mIngredientsList = presenter.getIngredients(mPlaceId);
            if (mParam != null && mParam.equals(Param.Add)) {
                createProduct();
            } else {
                mBasketProducts = mBasket.getmBasketProductsList().get(mEditProductPosition);
            }
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
        mButtonSave = (Button) v.findViewById(R.id.btn_order_save_button);
        btnLayout = v.findViewById(R.id.layout_btn_vibor_napitka);
        //-Объяснение 1: Егор закомментил, т.к. нужно было убрать с экрана ингридиентов надпись "Меню"
        /*toolbarBackText = (TextView) v.findViewById(R.id.tv_action_bar_back_text);
        llBack = (LinearLayout) v.findViewById(R.id.ll_coffee_ingredients_back);*/
        toolbarBackImage = (ImageView) v.findViewById(R.id.iv_action_bar_back_button);
        if (mParam != null && mParam.equals(Param.Edit)) {
            mButtonSave.setVisibility(View.VISIBLE);
            btnLayout.setVisibility(View.GONE);
            //-Объяснение 1), ищи выше
            /*toolbarBackText.setVisibility(View.GONE);*/
        } else {
            mButtonSave.setVisibility(View.GONE);
            btnLayout.setVisibility(View.VISIBLE);
        }
    }

    void initRecycler() {
        if (mParam != null && mParam.equals(Param.Edit)) {
            mAdapter = new CoffeeIngredientsAdapter(mIngredientsList, this, mBasket.getmBasketProductsList().get(mEditProductPosition).getmIngredientsList());
        } else {
            mAdapter = new CoffeeIngredientsAdapter(mIngredientsList, this);
        }
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
        tvActionBarTitle.setText(mProduct.getName());
        if (mParam != null && mParam.equals(Param.Add)) {
            tvProductCount.setText(String.valueOf(mBasketProducts.getCount()));
        } else {
            tvProductCount.setText(String.valueOf(mBasket.getmBasketProductsList().get(mEditProductPosition).getCount()));
        }
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Анимация при нажатии
                if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
                    animateLikeARipple(v);


                presenter.saveBasket(mBasket);
                getActivity().onBackPressed();
            }
        });
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveBasket(mBasket);
                Intent intent = new Intent(getContext(), OrderActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(BaseActivity.PLACE_ID_EXTRA, mPlaceId);
                startActivity(intent);
            }
        });
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveBasket(mBasket);
                Intent intent = new Intent(getContext(), OrderActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(BaseActivity.PLACE_ID_EXTRA, mPlaceId);
                startActivity(intent);
            }
        });
        if (mProduct.getSizes() != null) {
            createTabs();
        }
        //-Объяснение 1), ищи выше
        toolbarBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        setRxView();
        showSumma();
    }

    void createTabs() {
        for (int i = 0; i < mProduct.getSizes().size(); i++) {
            if (mProduct.getSizes().get(i).getSize() != null) {
                switch (mProduct.getSizes().get(i).getSize()) {
                    case "s":
                        View viewS = LayoutInflater.from(getContext()).inflate(R.layout.customtab, null);
                        viewS.findViewById(R.id.icon).setBackgroundResource(R.drawable.size_s_inactive);
                        TextView tvS = ((TextView) viewS.findViewById(R.id.tvCost));
                        tvS.setText(String.valueOf(mProduct.getSizes().get(i).getPrice()));
                        mTabLayout.addTab(mTabLayout.newTab().setCustomView(viewS).setContentDescription("s"));
                        break;
                    case "m":
                        View viewM = LayoutInflater.from(getContext()).inflate(R.layout.customtab, null);
                        viewM.findViewById(R.id.icon).setBackgroundResource(R.drawable.size_m_inactive);
                        TextView tvM = ((TextView) viewM.findViewById(R.id.tvCost));
                        tvM.setText(String.valueOf(mProduct.getSizes().get(i).getPrice()));
                        mTabLayout.addTab(mTabLayout.newTab().setCustomView(viewM).setContentDescription("m"));
                        break;
                    case "l":
                        View viewL = LayoutInflater.from(getContext()).inflate(R.layout.customtab, null);
                        viewL.findViewById(R.id.icon).setBackgroundResource(R.drawable.size_l_inactive);
                        TextView tvL = ((TextView) viewL.findViewById(R.id.tvCost));
                        tvL.setText(String.valueOf(mProduct.getSizes().get(i).getPrice()));
                        mTabLayout.addTab(mTabLayout.newTab().setCustomView(viewL).setContentDescription("l"));
                        break;
                    default:
                        break;
                }
            }
        }

        /**
         * Last redaction 01.11.17 11:06 by Egor
         * (add animation on tab)
         */
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View viewCup = tab.getCustomView().findViewById(R.id.icon);

                animateLikeJelly(viewCup);

                switch (String.valueOf(tab.getContentDescription())) {
                    case "s":
                        viewCup.setBackgroundResource(R.drawable.size_s_active);
                        TextView viewByIdS = (TextView) tab.getCustomView().findViewById(R.id.tvCost);
                        mBasketProducts.setCost(Integer.valueOf(viewByIdS.getText().toString()));
                        mBasketProducts.setSizeId(tab.getContentDescription().toString());
                        break;
                    case "m":
                        viewCup.setBackgroundResource(R.drawable.size_m_active);
                        TextView viewByIdM = (TextView) tab.getCustomView().findViewById(R.id.tvCost);
                        mBasketProducts.setCost(Integer.valueOf(viewByIdM.getText().toString()));
                        mBasketProducts.setSizeId(tab.getContentDescription().toString());
                        break;
                    case "l":
                        viewCup.setBackgroundResource(R.drawable.size_l_active);
                        TextView viewByIdL = (TextView) tab.getCustomView().findViewById(R.id.tvCost);
                        mBasketProducts.setCost(Integer.valueOf(viewByIdL.getText().toString()));
                        mBasketProducts.setSizeId(tab.getContentDescription().toString());
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
                        mBasketProducts.setSizeId(tab.getContentDescription().toString());
                        break;
                    case "m":
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_m_active);
                        TextView viewByIdM = (TextView) tab.getCustomView().findViewById(R.id.tvCost);
                        mBasketProducts.setCost(Integer.valueOf(viewByIdM.getText().toString()));
                        mBasketProducts.setSizeId(tab.getContentDescription().toString());
                        break;
                    case "l":
                        tab.getCustomView().findViewById(R.id.icon).setBackgroundResource(R.drawable.size_l_active);
                        TextView viewByIdL = (TextView) tab.getCustomView().findViewById(R.id.tvCost);
                        mBasketProducts.setCost(Integer.valueOf(viewByIdL.getText().toString()));
                        mBasketProducts.setSizeId(tab.getContentDescription().toString());
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

    void createProduct() {
        mBasketProducts = new BasketProducts();
        mBasketProducts.setProductId(mProductId);
        mBasketProducts.setCount(1);
        mBasketProducts.setCost(0);
        mBasketProducts.setName(mProduct.getName());
        if (mBasket != null) {
            mBasket.getmBasketProductsList().add(mBasketProducts);
        } else {
            mBasket = presenter.getBasket(mPlaceId);
            if (mBasket.getmBasketProductsList() != null) {
                mBasket.getmBasketProductsList().add(mBasketProducts);
            } else {
                mBasket.setmBasketProductsList(new RealmList<BasketProducts>());
                mBasket.getmBasketProductsList().add(mBasketProducts);
            }
        }
    }

    /**
     * Last redaction 01.11.17 by Egor
     * (add animation for the checkIcon)
     */

    @Override
    //Клик по ингредиенту
    public void onIngredientsClick(View v, int position) {

        //--For other version there applying a ripple effect written in xml
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            //--анимация при нажатии
            //!!!!!!!!!--Сделать подпрыгивание при выборе ингридиента---------
            //-Не сделал, т.к. пошёл разбираться в анимациях перехода
            ObjectAnimator animation = ObjectAnimator.ofFloat(v, View.ALPHA, 1, 0.5f);
            animation.setRepeatMode(ValueAnimator.REVERSE);
            animation.setRepeatCount(1);
            animation.start();
            //---
        }

        int realPosition = position - 1;
        Ingredients ingredientClick = mIngredientsList.get(realPosition);
        ImageView viewById = (ImageView) v.findViewById(R.id.iv_napitok_add);



        if (checkIcon(realPosition)) {
            for (int i = 0; i < mBasketProducts.getmIngredientsList().size(); i++) {
                if (ingredientClick.getId().equals(mBasketProducts.getmIngredientsList().get(i).getId())) {
                    mBasketProducts.getmIngredientsList().remove(i);
                }
            }
            animateCoinFlip(viewById, R.drawable.add_icon);
        } else {
            mBasketProducts.getmIngredientsList().add(ingredientClick);
            animateCoinFlip(viewById, R.drawable.done_icon);
        }
        showSumma();
    }

    Boolean checkIcon(int position) {
        for (Ingredients ingredient : mBasketProducts.getmIngredientsList()) {
            if (ingredient.getId().equals(mIngredientsList.get(position).getId())) {
                return true;
            }
        }
        return false;
    }

    void setRxView() {
        RxView.clicks(ivAddProductCount).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

                //if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT){
                    animateLikeARipple(ivAddProductCount);


                int i = Integer.parseInt(tvProductCount.getText().toString());
                if (i != 9) {
                    i++;
                    tvProductCount.setText(String.valueOf(i));
                    if (mParam != null && mParam.equals(Param.Add)) {
                        mBasketProducts.setCount(i);
                    } else {
                        mBasket.getmBasketProductsList().get(mEditProductPosition).setCount(i);
                    }
                    showSumma();
                }
            }
        });

        RxView.clicks(ivRemoveProductCount).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

                //if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    animateLikeARipple(ivRemoveProductCount);


                int i = Integer.parseInt(tvProductCount.getText().toString());
                if (i != 1) {
                    i--;
                    tvProductCount.setText(String.valueOf(i));
                    if (mParam != null && mParam.equals(Param.Add)) {
                        mBasketProducts.setCount(i);
                    } else {
                        mBasket.getmBasketProductsList().get(mEditProductPosition).setCount(i);
                    }
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

    /**
     * Animation by Egor
     */

    /**
     * Эффект мигания (разовое при каком-либо действии)
     * @param view
     */
    void animateLikeARipple(View view) {
        view.animate()
                .alpha(0.5f)//число экспериментальное
                //.scaleY(0.75f)
                .setInterpolator(new CycleInterpolator(1))
                .start();
    }

    /**
     * Эффект желе
     * @param view
     */
    void animateLikeJelly(View view){
        view.animate()
                //Проблема: при использовании CycleInterpolator картинка вылазит за рамки
                //т.е. обрезается
                //Решение: либо другой интерполятор юзать, либо увеличить padding
                .scaleX(0.75f)
                .scaleY(0.75f)
                .setInterpolator(new CycleInterpolator(1));
    }

    /**
     * Эффект переворачивающейся монетки
     * @param view - Вью, для которой применяется
     * @param resourceTo - Изображени, НА которое поменяется нынешнее, выставленное во "view"
     */
    void animateCoinFlip(ImageView view, int resourceTo){
        view.animate()
                .scaleX(0).start();

        view.setImageResource(resourceTo);
        view.setScaleX(0);

        view.animate()
                .scaleX(1).start();

    }
}
