package com.example.adm1n.coffeescope.custom_view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.coffee_menu.MenuAdapter;
import com.example.adm1n.coffeescope.coffee_menu.custom_model.CoffeeMenu;
import com.example.adm1n.coffeescope.utils.SpaceItemDecoration;

import java.util.List;

public class CoffeeCardView extends FrameLayout {
    public AppBarLayout peakView;
    public TextView tvPreviewBottomJobTime;
    public TextView tvPreviewBottomRateCount;
    public TextView tvPreviewBottomRangeCount;
    public TextView tv_coffee_name;
    public TextView tv_coffee_address;
    public TextView tv_coffee_phone_number;
    public TextView tv_preview_card_place_average_time;
    public ImageView iv_preview_card_top_arrow;
    public ImageView ivPreviewBottomStatus;
    public RecyclerView recyclerview;
    public Button mBtnPayCoffee;

    private MenuAdapter menuAdapter;

    public CoffeeCardView(@NonNull Context context) {
        super(context);
        initView();
    }

    public CoffeeCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CoffeeCardView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void initView() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        View container = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_content_view, null);
        addView(container, lp);

        peakView = (AppBarLayout) findViewById(R.id.peak_view);
        tvPreviewBottomJobTime = (TextView) findViewById(R.id.tvPreviewBottomJobTime);
        tvPreviewBottomRateCount = (TextView) findViewById(R.id.tv_preview_card_place_rate_count);
        tvPreviewBottomRangeCount = (TextView) findViewById(R.id.tv_preview_card_place_range_count);
        tv_coffee_name = (TextView) findViewById(R.id.tv_preview_card_product_name);
        tv_coffee_address = (TextView) findViewById(R.id.tv_preview_card_place_address);
        tv_coffee_phone_number = (TextView) findViewById(R.id.tv_preview_card_place_phone_number);
        tv_preview_card_place_average_time = (TextView) findViewById(R.id.tv_preview_card_place_average_time);
        iv_preview_card_top_arrow = (ImageView) findViewById(R.id.iv_preview_card_top_arrow);
        ivPreviewBottomStatus = (ImageView) findViewById(R.id.ivPreviewBottomStatus);
        peakView = (AppBarLayout) findViewById(R.id.peak_view);
        recyclerview = (RecyclerView) findViewById(R.id.rv_coffee_menu);
        mBtnPayCoffee = (Button) findViewById(R.id.btn_coffee_menu_pay);
    }

    private void init() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(manager);
        SpaceItemDecoration decorator = new SpaceItemDecoration(20, true, true);
        recyclerview.addItemDecoration(decorator);
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisiblePosition = manager.findFirstCompletelyVisibleItemPosition();
                    if (firstVisiblePosition == 0) {
                        peakView.setExpanded(true, true);
                    }
                }
            }
        });
    }

    public void setAdapter(List<CoffeeMenu> menu, MenuAdapter.OnProductClick listener) {
        menuAdapter = new MenuAdapter(menu, listener);
        recyclerview.getRecycledViewPool().clear();
        recyclerview.setAdapter(menuAdapter);
        for (int i = menuAdapter.getGroups().size() - 1; i >= 0; i--) {
            if (menuAdapter.isGroupExpanded(i)) {
                return;
            }
            menuAdapter.toggleGroup(i);
        }
        menuAdapter.notifyDataSetChanged();
//        initBasket();
    }
}
