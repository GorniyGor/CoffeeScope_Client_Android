package com.example.adm1n.coffeescope.custom_view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
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
import com.example.adm1n.coffeescope.models.Hours;
import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.models.basket.Basket;
import com.example.adm1n.coffeescope.utils.SpaceItemDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

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
    public View previewTopElements;

    private MenuAdapter menuAdapter;

    public CoffeeCardView(@NonNull Context context) {
        this(context, null);
    }

    public CoffeeCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
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
        recyclerview = (RecyclerView) findViewById(R.id.rv_coffee_menu);
        mBtnPayCoffee = (Button) findViewById(R.id.btn_coffee_menu_pay);
        previewTopElements = findViewById(R.id.preview_top_elements);
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

    public void setPlace(Place place) {
        tv_coffee_name.setText(place.getName());
        tv_coffee_address.setText(place.getAddress());
        tv_coffee_phone_number.setText(place.getPhone());
        tv_preview_card_place_average_time.setText(place.getAverage_time() + " мин");
        tvPreviewBottomRateCount.setText(String.valueOf(place.getRating()));

        initDate(place);
    }

    private void initDate(Place place) {
        Date current = null;
        Date placeOpenTime = null;
        Date placeCloseTime = null;
        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // 1) получаем текущую дату
        Calendar calendar = GregorianCalendar.getInstance();
        // 2) определяем день недели
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
        // 3) получаем расписание по этому дню
        Hours hours = place.getHours().get(currentDay);
        String open = hours.getOpen();      //"17:31"
        String close = hours.getClose();    //"20:31"
        // 4) преобразую в нужный мне формат
        try {
            current = dateFormat.parse(dateFormat.format(currentDate));
            placeOpenTime = dateFormat.parse(open);
            placeCloseTime = dateFormat.parse(close);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //проверяю условия
        String time = "";
        @DrawableRes int iconId;
        if (placeCloseTime != null && placeOpenTime != null) {
            if (current.after(placeOpenTime) && current.before(placeCloseTime)) {
                iconId = R.drawable.open_icon;
                time = getContext().getString(R.string.until, place.getHours().get(currentDay).getClose());
            } else {
                iconId = R.drawable.close_icon;
                if (current.before(placeOpenTime)) {
                    time = getContext().getString(R.string.until, place.getHours().get(currentDay).getOpen());
                } else if (current.after(placeCloseTime)) {
                    //достать след день
                    if (place.getHours().size() != currentDay) {
                        time = getContext().getString(R.string.until, place.getHours().get(currentDay + 1).getOpen());
                    } else {
                        time = getContext().getString(R.string.until, place.getHours().get(0).getOpen());
                    }
                }
            }
            tvPreviewBottomJobTime.setText(time);
            ivPreviewBottomStatus.setImageResource(iconId);
        }
    }

    public void updateBaster(Basket basket) {
        if (basket != null) {
            int items = basket.getmBasketProductsList().size();
            String drinks = getContext().getResources().getQuantityString(R.plurals.drinks, items, items);
            mBtnPayCoffee.setEnabled(basket.getmBasketProductsList().size() > 0);
            mBtnPayCoffee.setText(getContext().getString(R.string.button_order, drinks, basket.getSumma(basket)));
        } else {
            mBtnPayCoffee.setEnabled(false);
        }
    }

    public int getHeaderHeight() {
        return previewTopElements.getHeight() + peakView.getHeight();
    }

    public void setDistance(String text) {
        tvPreviewBottomRangeCount.setText(text);
    }

    public void setExpanded(boolean e) {
        peakView.setExpanded(e);
    }

    public void rotateArrow(int angle) {
        iv_preview_card_top_arrow.animate().rotation(angle).setDuration(300).start();
    }

    public void setOnOrderButtonClickListener(View.OnClickListener listener) {
        mBtnPayCoffee.setOnClickListener(listener);
    }
}
