package com.example.adm1n.coffeescope.main_map.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm1n.coffeescope.BaseActivity;
import com.example.adm1n.coffeescope.BaseActivityWithoutToolbar;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.coffee_ingredients.view.CoffeeIngredientsActivity;
import com.example.adm1n.coffeescope.coffee_ingredients.view.CoffeeIngredientsFragment;
import com.example.adm1n.coffeescope.coffee_menu.MenuAdapter;
import com.example.adm1n.coffeescope.coffee_menu.custom_model.CoffeeMenu;
import com.example.adm1n.coffeescope.custom_view.CoffeeCardView;
import com.example.adm1n.coffeescope.introduction.authorization.IntroductionAuthorizationActivity;
import com.example.adm1n.coffeescope.main_map.presenter.MainPresenter;
import com.example.adm1n.coffeescope.models.Hours;
import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.models.Product;
import com.example.adm1n.coffeescope.models.basket.Basket;
import com.example.adm1n.coffeescope.order.view.OrderActivity;
import com.example.adm1n.coffeescope.utils.MapsUtils;
import com.example.adm1n.coffeescope.utils.PermissionUtils;
import com.example.adm1n.coffeescope.utils.SpaceItemDecoration;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MapsActivity extends BaseActivityWithoutToolbar implements OnMapReadyCallback, MenuAdapter.OnProductClick, IMapActivity {

    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final int DEFAULT_MAP_ZOOM = 13;

    private ArrayList<CoffeeMenu> coffeeMenuList = new ArrayList<>();
    private ArrayList<Place> placeList = new ArrayList<>();

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private boolean mPermissionDenied = false;
    private LatLng mLastKnownLocation;
    private Location bestLocation;

    //preViewCardView
    private AppBarLayout peakView;
    private TextView tvPreviewBottomJobTime;
    private TextView tvPreviewBottomRateCount;
    private TextView tvPreviewBottomRangeCount;
    private TextView tv_coffee_name;
    private TextView tv_coffee_address;
    private TextView tv_coffee_phone_number;
    private TextView tv_preview_card_place_average_time;
    private ImageView iv_preview_card_top_arrow;
    private ImageView ivPreviewBottomStatus;
    private BottomSheetBehavior mBottomSheetBehavior;

    private RecyclerView recyclerview;
    private MenuAdapter menuAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Button mBtnPayCoffee;

    private MainPresenter presenter;
    private Basket mBasket;
    private Observable<Basket> mBasketOBS;
    private CompositeDisposable disposable = new CompositeDisposable();

    private CoffeeCardView coffeeCardView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contain_main);

        coffeeCardView = (CoffeeCardView) findViewById(R.id.coffee_card_view);

        presenter = new MainPresenter(getApplicationContext(), this);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //init map control button
        Button mButtonPlusZoom = (Button) findViewById(R.id.btn_plus_zoom);
        mButtonPlusZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });
        Button mButtonMinusZoom = (Button) findViewById(R.id.btn_minus_zoom);
        mButtonMinusZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
        Button mButtonMyLocate = (Button) findViewById(R.id.btn_my_location);
        mButtonMyLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float currentZoom = mMap.getCameraPosition().zoom;
                if (mMap.getMyLocation() != null) {
                    if (currentZoom <= 13) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()), DEFAULT_MAP_ZOOM));
                    } else {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()), currentZoom));
                    }
                }
            }
        });
        iv_preview_card_top_arrow = coffeeCardView.iv_preview_card_top_arrow;
        Button mButtonMyProfile = (Button) findViewById(R.id.btn_profile);
        mButtonMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IntroductionAuthorizationActivity.class);
                startActivity(intent);
            }
        });
        Button mButtonSearch = (Button) findViewById(R.id.btn_search);
        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "On Search Click", Toast.LENGTH_SHORT).show();
            }
        });
        Button mButtonApply = (Button) findViewById(R.id.btn_apply);
        mButtonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "On Apply Click", Toast.LENGTH_SHORT).show();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //initBottomSheet
        peakView = coffeeCardView.peakView;

        Display display = ((android.view.WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        FrameLayout FlBottomSheetBehavior = (FrameLayout) findViewById(R.id.fl_sheet_content);
        ViewGroup.LayoutParams params = FlBottomSheetBehavior.getLayoutParams();

        params.height = (int) (display.getHeight() * 0.95);
        FlBottomSheetBehavior.setLayoutParams(params);
        mBottomSheetBehavior = BottomSheetBehavior.from(FlBottomSheetBehavior);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    peakView.setExpanded(true);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset > 0.5) {
                    iv_preview_card_top_arrow.setImageResource(R.drawable.arrow_down_icon2);
                } else {
                    iv_preview_card_top_arrow.setImageResource(R.drawable.arrow_down_icon);
                }
            }
        });
        //initRecycler
        recyclerview = coffeeCardView.recyclerview;

        mBtnPayCoffee = coffeeCardView.mBtnPayCoffee;
        mBtnPayCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(BaseActivity.PLACE_ID_EXTRA, mLastPlace.getId());
                startActivity(intent);
            }
        });
        mapFragment.getMapAsync(this);
    }

    void setAdapter(Place place) {
        coffeeCardView.setAdapter(createMenu(place), this);
//        menuAdapter = new MenuAdapter(createMenu(place), this);
//        recyclerview.getRecycledViewPool().clear();
//        recyclerview.setAdapter(menuAdapter);
//        for (int i = menuAdapter.getGroups().size() - 1; i >= 0; i--) {
//            expandGroup(i);
//        }
//        menuAdapter.notifyDataSetChanged();
        initBasket();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        presenter.getPlaces();
        enableMyLocation();
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                presenter.getPlace(marker.getSnippet());
                peakView.requestLayout();
                View previewTopElements = findViewById(R.id.preview_top_elements);
                mBottomSheetBehavior.setPeekHeight(previewTopElements.getHeight() + peakView.getHeight());
                showPeakView(presenter.getPlaceFromRealm(Integer.valueOf(marker.getSnippet())));
                if (mBottomSheetBehavior != null) {
                    switch (mBottomSheetBehavior.getState()) {
                        case (BottomSheetBehavior.STATE_HIDDEN):
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            break;
                        case (BottomSheetBehavior.STATE_COLLAPSED):
                            break;
                        case (BottomSheetBehavior.STATE_EXPANDED):
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            break;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mBottomSheetBehavior != null) {
                    switch (mBottomSheetBehavior.getState()) {
                        case (BottomSheetBehavior.STATE_HIDDEN):
                            break;
                        case (BottomSheetBehavior.STATE_COLLAPSED):
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            break;
                        case (BottomSheetBehavior.STATE_EXPANDED):
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    public void showPeakView(Place place) {
        mLastPlace = place;
        tv_coffee_name = (TextView) findViewById(R.id.tv_preview_card_product_name);
        ivPreviewBottomStatus = (ImageView) findViewById(R.id.ivPreviewBottomStatus);
        tv_coffee_address = (TextView) findViewById(R.id.tv_preview_card_place_address);
        tv_coffee_phone_number = (TextView) findViewById(R.id.tv_preview_card_place_phone_number);
        tv_preview_card_place_average_time = (TextView) findViewById(R.id.tv_preview_card_place_average_time);
        tvPreviewBottomRangeCount = (TextView) findViewById(R.id.tv_preview_card_place_range_count);
        tvPreviewBottomJobTime = (TextView) findViewById(R.id.tvPreviewBottomJobTime);
        tvPreviewBottomRateCount = (TextView) findViewById(R.id.tv_preview_card_place_rate_count);

        tv_coffee_name.setText(mLastPlace.getName());
        tv_coffee_address.setText(mLastPlace.getAddress());
        tv_coffee_phone_number.setText(mLastPlace.getPhone());
        tv_preview_card_place_average_time.setText(mLastPlace.getAverage_time() + " мин");
        tvPreviewBottomRateCount.setText(String.valueOf(mLastPlace.getRating()));
        if (mMap.getMyLocation() != null) {
            int distance = MapsUtils.calculationDistance(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude())
                    , new LatLng(mLastPlace.getCoodrinates().getLatitude(),
                            mLastPlace.getCoodrinates().getLongitude()));
            tvPreviewBottomRangeCount.setText(MapsUtils.castDistance(distance));
        } else {
            tvPreviewBottomRangeCount.setText("fail");
        }
        initDate();
        initBasket();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    List<CoffeeMenu> createMenu(Place place) {
        coffeeMenuList.clear();
        for (int i = 0; i < place.getCategories().size(); i++) {
            CoffeeMenu coffeeMenu = new CoffeeMenu(place.getCategories().get(i).getName(), place.getCategories().get(i).getProducts());
            coffeeMenuList.add(coffeeMenu);
        }
        return coffeeMenuList;
    }

    @Override
    public void onClick(View v, Product product) {
        Intent intent = new Intent(getApplicationContext(), CoffeeIngredientsActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(PRODUCT_ID_EXTRA, product.getId());
        intent.putExtra(PARAM_EXTRA, CoffeeIngredientsFragment.Param.Add);
        intent.putExtra(PLACE_ID_EXTRA, mLastPlace.getId());
        startActivity(intent);
    }

    @Override
    public void setMarkers(ArrayList<Place> list) {
        for (int i = 0; i < list.size(); i++) {
            Place place = list.get(i);
//            if (place.getImage().getLable() != null) {
//                Drawable drawable = Drawable.createFromPath(place.getImage().getLable());
//                Bitmap bitmap = drawableToBitmap(drawable);
//                mMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(place.getCoodrinates().getLongitude(), place.getCoodrinates().getLatitude()))
//                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
//                        .snippet(String.valueOf(place.getId())));
//            } else {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(place.getCoodrinates().getLatitude(), place.getCoodrinates().getLongitude()))
                    .snippet(String.valueOf(place.getId())));
//            }
        }
        placeList.addAll(list);
    }

    @Override
    public void showError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        if (disposable != null) {
            disposable.clear();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLastPlace != null) {
            initBasket();
        }
    }

    void initDate() {
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
        Hours hours = mLastPlace.getHours().get(currentDay);
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
                tvPreviewBottomJobTime.setText("до " + mLastPlace.getHours().get(currentDay).getClose());
            } else {
                if (current.before(placeOpenTime)) {
                    tvPreviewBottomJobTime.setText("до " + mLastPlace.getHours().get(currentDay).getOpen());
                } else if (current.after(placeCloseTime)) {
                    //достать след день
                    if (mLastPlace.getHours().size() != currentDay) {
                        tvPreviewBottomJobTime.setText("до " + mLastPlace.getHours().get(currentDay + 1).getOpen());
                    } else {
                        tvPreviewBottomJobTime.setText("до " + mLastPlace.getHours().get(0).getOpen());
                    }
                }
                ivPreviewBottomStatus.setImageResource(R.drawable.close_icon);
            }
        }
    }

    @Override
    public void setMenuAdapter(Place place) {
        setAdapter(place);
    }

    void initBasket() {
        mBasketOBS = presenter.getBasket(mLastPlace.getId());
        if (mBasketOBS != null) {
            disposable.add(mBasketOBS
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Basket>() {
                        @Override
                        public void accept(Basket basket) throws Exception {
                            if (basket != null) {
                                mBasket = basket;
                                mBtnPayCoffee.setText(String.valueOf(mBasket.getmBasketProductsList().size())
                                        + " Позиции " + mBasket.getSumma(mBasket));
                                if (mBasket.getmBasketProductsList().size() == 0) {
                                    mBtnPayCoffee.setEnabled(false);
                                } else {
                                    mBtnPayCoffee.setEnabled(true);
                                }
                                mBtnPayCoffee.setText("В заказе " + String.valueOf(mBasket.getmBasketProductsList().size())
                                        + " напитка (" + mBasket.getSumma(mBasket) + "Р)");
                            } else {
                                mBtnPayCoffee.setEnabled(false);
                            }
                        }
                    }));
        } else {
            Toast.makeText(this, "Ошибка RX", Toast.LENGTH_SHORT).show();
        }
    }
}