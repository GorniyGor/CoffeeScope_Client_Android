package com.example.adm1n.coffeescope.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.adm1n.coffeescope.BaseActivity;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.coffee_ingredients.CoffeeIngredientsActivity;
import com.example.adm1n.coffeescope.main_map.view.MapsActivity;
import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.models.basket.Basket;
import com.example.adm1n.coffeescope.models.basket.BasketProducts;
import com.example.adm1n.coffeescope.utils.SpaceItemDecoration;

import io.realm.RealmList;

/**
 * Created by adm1n on 21.07.2017.
 */

public class OrderActivity extends BaseActivity implements OrderAdapter.OnOrderClick {

    private TextView tv_order_place_name;
    private TextView tv_order_place_address;
    private TextView tv_order_place_phone_number;
    private TextView tv_order_place_average_time;
    private Button btn_order_summa_count;

    private RealmList<BasketProducts> basketProductses;
    private SwipeRevealLayout swipeLayout;
    private EditText etOrderCommentField;
    private Toolbar toolbar;
    private TimePicker timePicker;
    private RadioButton mRadioButtonFast;
    private RadioButton mRadioButtonTime;
    private RecyclerView mRecyclerView;

    private OrderAdapter mAdapter;
    private Basket mBasket;
    private Integer mPlaceId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order);
        if (savedInstanceState == null) {
            mPlaceId = getIntent().getIntExtra(MapsActivity.PLACE_ID_EXTRA, 0);
            getBasket(mPlaceId);
            getPlace(mPlaceId);
        }
        initView();
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

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_order_basket);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        basketProductses = mBasket.getmBasketProductsList();
        mAdapter = new OrderAdapter(mBasket.getmBasketProductsList(), this);
        SpaceItemDecoration decorator = new SpaceItemDecoration(32, true, true);
        mRecyclerView.addItemDecoration(decorator);
        mRecyclerView.setAdapter(mAdapter);
        toolbar = (Toolbar) findViewById(R.id.order_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Мой заказ");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mRadioButtonFast = (RadioButton) findViewById(R.id.rb_1_order_coffee);
        mRadioButtonFast.setOnClickListener(radioButtonClickListener);
        mRadioButtonTime = (RadioButton) findViewById(R.id.rb_2_order_coffee);
        mRadioButtonTime.setOnClickListener(radioButtonClickListener);
        timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePicker.setEnabled(false);

        btn_order_summa_count = (Button) findViewById(R.id.btn_order_summa_count);
        btn_order_summa_count.setText("Итого:" + mBasket.getSumma());
    }

    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton) v;
            switch (rb.getId()) {
                case R.id.rb_1_order_coffee:
                    timePicker.setEnabled(false);
                    break;
                case R.id.rb_2_order_coffee:
                    timePicker.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

    void initView() {
        tv_order_place_name = (TextView) findViewById(R.id.tv_order_place_name);
        tv_order_place_name.setText(mLastPlace.getName());
        tv_order_place_address = (TextView) findViewById(R.id.tv_order_place_address);
        tv_order_place_address.setText(mLastPlace.getAddress());
        tv_order_place_phone_number = (TextView) findViewById(R.id.tv_order_place_phone_number);
        tv_order_place_phone_number.setText(mLastPlace.getPhone());
        tv_order_place_average_time = (TextView) findViewById(R.id.tv_order_place_average_time);
        tv_order_place_average_time.setText(mLastPlace.getAverage_time() + " мин");
    }

    private void getBasket(Integer id) {
        mBasket = mRealm.copyFromRealm(mRealm.where(Basket.class).equalTo("mBasketId", id).findFirst());
    }

    private void getPlace(Integer id) {
        mLastPlace = mRealm.copyFromRealm(mRealm.where(Place.class).equalTo("id", id).findFirst());
    }

    @Override
    public void onDelete(View v, int position) {
        basketProductses.remove(basketProductses.get(position));
        mAdapter.notifyDataSetChanged();
        saveBasket();
    }

    @Override
    public void onRefactor(View v, int position) {
        Intent intent = new Intent(getApplicationContext(), CoffeeIngredientsActivity.class);
        startActivity(intent);
    }

    void saveBasket() {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(mBasket);
        mRealm.commitTransaction();
    }
}
