package com.example.adm1n.coffeescope.order.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.adm1n.coffeescope.BaseActivity;
import com.example.adm1n.coffeescope.BaseFragment;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.coffee_ingredients.view.CoffeeIngredientsActivity;
import com.example.adm1n.coffeescope.coffee_ingredients.view.CoffeeIngredientsFragment;
import com.example.adm1n.coffeescope.custom_view.CustomGridLayoutManager;
import com.example.adm1n.coffeescope.models.basket.BasketProducts;
import com.example.adm1n.coffeescope.order.OrderAdapter;
import com.example.adm1n.coffeescope.order.presenter.OrderPresenter;
import com.example.adm1n.coffeescope.utils.SpaceItemDecoration;

import io.realm.RealmList;

import static com.example.adm1n.coffeescope.R.string.btn_order_result_text;

public class OrderFragment extends BaseFragment implements OrderAdapter.OnOrderClick {

    private TextView tv_order_place_name;
    private TextView tv_order_place_address;
    private TextView tv_order_place_phone_number;
    private TextView tv_place_average_time;
    private Button btn_order_summa_count;

    private RealmList<BasketProducts> basketProductses;
    private EditText etOrderCommentField;
    private TimePicker timePicker;
    private RadioButton mRadioButtonFast;
    private RadioButton mRadioButtonTime;
    private RecyclerView mRecyclerView;
    private OrderPresenter presenter;

    private OrderAdapter mAdapter;
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
        presenter = new OrderPresenter();
        if (getArguments().get(PLACE_ID_EXTRA) != null) {
            mPlaceId = getArguments().getInt(PLACE_ID_EXTRA);
            mBasket = presenter.getBasket(mPlaceId);
            mLastPlace = presenter.getPlace(mPlaceId);
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
        CustomGridLayoutManager customGridLayoutManager = new CustomGridLayoutManager(getContext());
        customGridLayoutManager.setScrollEnabled(false);
        mRecyclerView.setLayoutManager(customGridLayoutManager);
        basketProductses = mBasket.getmBasketProductsList();
        mAdapter = new OrderAdapter(mBasket.getmBasketProductsList(), this);
        SpaceItemDecoration decorator = new SpaceItemDecoration(8, true, true);
        mRecyclerView.addItemDecoration(decorator);
        mRecyclerView.setAdapter(mAdapter);
        mRadioButtonFast.setOnClickListener(radioButtonClickListener);
        mRadioButtonTime.setOnClickListener(radioButtonClickListener);
        timePicker.setIs24HourView(true);
        timePicker.setEnabled(false);
        btn_order_summa_count.setText(getString(btn_order_result_text) + " " + mBasket.getSumma() + "\u20BD");
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
        etOrderCommentField = (EditText) v.findViewById(R.id.et_order_comment_field);
        mRadioButtonFast = (RadioButton) v.findViewById(R.id.rb_1_order_coffee);
        mRadioButtonTime = (RadioButton) v.findViewById(R.id.rb_2_order_coffee);
        timePicker = (TimePicker) v.findViewById(R.id.time_picker);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_order_basket);
        btn_order_summa_count = (Button) v.findViewById(R.id.btn_order_summa_count);
    }

    @Override
    public void onDelete(View v, int position) {
        mBasket.getmBasketProductsList().remove(position);
        mAdapter.notifyDataSetChanged();
        presenter.saveBasket(mBasket);
    }

    @Override
    public void onRefactor(View v, int position) {
        presenter.saveBasket(mBasket);
        Intent intent = new Intent(getContext(), CoffeeIngredientsActivity.class);
        intent.putExtra(PLACE_ID_EXTRA, mPlaceId);
        intent.putExtra(PARAM_EXTRA, CoffeeIngredientsFragment.Param.Edit);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(PRODUCT_POSITION_EDIT_EXTRA, position);
        intent.putExtra(BaseActivity.PRODUCT_ID_EXTRA, mBasket.getmBasketProductsList().get(position).getProductId());
        startActivity(intent);
    }
}
