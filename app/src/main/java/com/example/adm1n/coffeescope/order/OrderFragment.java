package com.example.adm1n.coffeescope.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.adm1n.coffeescope.BaseFragment;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.coffee_ingredients.CoffeeIngredientsActivity;
import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.models.basket.Basket;
import com.example.adm1n.coffeescope.models.basket.BasketProducts;
import com.example.adm1n.coffeescope.utils.SpaceItemDecoration;

import io.realm.RealmList;

/**
 * Created by adm1n on 25.08.2017.
 */

public class OrderFragment extends BaseFragment implements OrderAdapter.OnOrderClick {

    private TextView tv_order_place_name;
    private TextView tv_order_place_address;
    private TextView tv_order_place_phone_number;
    private TextView tv_place_average_time;
    private Button btn_order_summa_count;

    private RealmList<BasketProducts> basketProductses;
    private SwipeRevealLayout swipeLayout;
    private EditText etOrderCommentField;
    private TimePicker timePicker;
    private RadioButton mRadioButtonFast;
    private RadioButton mRadioButtonTime;
    private RecyclerView mRecyclerView;

    private OrderAdapter mAdapter;
    private Basket mBasket;
    private Integer mPlaceId;


    public static OrderFragment newInstance(Integer placeId) {
        Bundle args = new Bundle();
        args.putInt(PLACE_ID_EXTRA, placeId);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().get(PLACE_ID_EXTRA) != null) {
            mPlaceId = getArguments().getInt(PLACE_ID_EXTRA);
            getPlace(mPlaceId);
            getBasket(mPlaceId);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etOrderCommentField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etOrderCommentField.setFocusable(true);
                etOrderCommentField.setFocusableInTouchMode(true);
                return false;
            }
        });
        initPlaceInfo();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        basketProductses = mBasket.getmBasketProductsList();
        mAdapter = new OrderAdapter(mBasket.getmBasketProductsList(), this);
        SpaceItemDecoration decorator = new SpaceItemDecoration(20, true, true);
        mRecyclerView.addItemDecoration(decorator);
        mRecyclerView.setAdapter(mAdapter);
        mRadioButtonFast.setOnClickListener(radioButtonClickListener);
        mRadioButtonTime.setOnClickListener(radioButtonClickListener);

        timePicker.setEnabled(false);
    }

    private void initPlaceInfo() {
        tv_order_place_name.setText(mLastPlace.getName());
        tv_order_place_address.setText(mLastPlace.getAddress());
        tv_order_place_phone_number.setText(mLastPlace.getPhone());
        tv_place_average_time.setText(mLastPlace.getAverage_time() + " мин");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, null);
        initView(view);
        return view;
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

    void initView(View v) {
        tv_order_place_name = (TextView) v.findViewById(R.id.tv_order_place_name);
        tv_order_place_address = (TextView) v.findViewById(R.id.tv_place_address);
        tv_order_place_phone_number = (TextView) v.findViewById(R.id.tv_place_phone_number);
        tv_place_average_time = (TextView) v.findViewById(R.id.tv_place_average_time);
        swipeLayout = (SwipeRevealLayout) v.findViewById(R.id.swipeLayout);
        etOrderCommentField = (EditText) v.findViewById(R.id.et_order_comment_field);
        mRadioButtonFast = (RadioButton) v.findViewById(R.id.rb_1_order_coffee);
        mRadioButtonTime = (RadioButton) v.findViewById(R.id.rb_2_order_coffee);
        timePicker = (TimePicker) v.findViewById(R.id.time_picker);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_order_basket);
        btn_order_summa_count = (Button) v.findViewById(R.id.btn_order_summa_count);
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
        Intent intent = new Intent(getContext(), CoffeeIngredientsActivity.class);
        startActivity(intent);
    }

    void saveBasket() {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(mBasket);
        mRealm.commitTransaction();
    }
}
