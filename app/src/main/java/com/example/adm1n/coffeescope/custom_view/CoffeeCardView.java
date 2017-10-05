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
import android.widget.Toast;

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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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

    public void setPlace(Place place) {
        tv_coffee_name.setText(place.getName());
        tv_coffee_address.setText(place.getAddress());
        tv_coffee_phone_number.setText(place.getPhone());
        tv_preview_card_place_average_time.setText(place.getAverage_time() + " мин");
        tvPreviewBottomRateCount.setText(String.valueOf(place.getRating()));

        initDate(place);
    }

    void initDate(Place place) {
        Date current = null;
        Date placeOpenTime = null;
        Date placeCloseTime = null;
        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

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
        if (placeCloseTime != null && placeOpenTime != null) {
            if (current.after(placeOpenTime) && current.before(placeCloseTime)) {
                ivPreviewBottomStatus.setImageResource(R.drawable.open_icon);
                tvPreviewBottomJobTime.setText("до " + place.getHours().get(currentDay).getClose());
            } else {
                if (current.before(placeOpenTime)) {
                    tvPreviewBottomJobTime.setText("до " + place.getHours().get(currentDay).getOpen());
                } else if (current.after(placeCloseTime)) {
                    //достать след день
                    if (place.getHours().size() != currentDay) {
                        tvPreviewBottomJobTime.setText("до " + place.getHours().get(currentDay + 1).getOpen());
                    } else {
                        tvPreviewBottomJobTime.setText("до " + place.getHours().get(0).getOpen());
                    }
                }
                ivPreviewBottomStatus.setImageResource(R.drawable.close_icon);
            }
        }
    }

    public void initBasket(Observable<Basket> mBasketOBS, CompositeDisposable disposable) {
        if (mBasketOBS != null) {
            disposable.add(mBasketOBS
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Basket>() {
                        @Override
                        public void accept(Basket basket) throws Exception {
                            if (basket != null) {
                                mBtnPayCoffee.setText(String.valueOf(basket.getmBasketProductsList().size())
                                        + " Позиции " + basket.getSumma(basket));
                                if (basket.getmBasketProductsList().size() == 0) {
                                    mBtnPayCoffee.setEnabled(false);
                                } else {
                                    mBtnPayCoffee.setEnabled(true);
                                }
                                mBtnPayCoffee.setText("В заказе " + String.valueOf(basket.getmBasketProductsList().size())
                                        + " напитка (" + basket.getSumma(basket) + "Р)");
                            } else {
                                mBtnPayCoffee.setEnabled(false);
                            }
                        }
                    }));
        } else {
            Toast.makeText(getContext(), "Ошибка RX", Toast.LENGTH_SHORT).show();
        }
    }

}
