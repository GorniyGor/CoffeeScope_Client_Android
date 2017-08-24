package com.example.adm1n.coffeescope.main_map.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm1n.coffeescope.BaseActivity;
import com.example.adm1n.coffeescope.models.basket.Basket;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.coffee_ingredients.CoffeeIngredientsActivity;
import com.example.adm1n.coffeescope.coffee_menu.MenuAdapter;
import com.example.adm1n.coffeescope.coffee_menu.custom_model.CoffeeMenu;
import com.example.adm1n.coffeescope.main_map.presenter.MainPresenter;
import com.example.adm1n.coffeescope.models.Hours;
import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.models.Products;
import com.example.adm1n.coffeescope.models.basket.BasketProducts;
import com.example.adm1n.coffeescope.order.OrderActivity;
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
import com.jakewharton.rxbinding2.view.RxView;

import org.reactivestreams.Subscription;

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
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmList;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, MenuAdapter.OnProductClick, IMapActivity {

    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final int DEFAULT_MAP_ZOOM = 13;
    public static final String PRODUCT_EXTRA = "PRODUCT_EXTRA";
    public static final String INGREDIENTS_EXTRA = "INGREDIENTS_EXTRA";
    public static final String PLACE_ID_EXTRA = "PLACE_ID_EXTRA";

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
    private AppBarLayout appBarLayout;
    private BottomSheetBehavior mBottomSheetBehavior;

    private RecyclerView recyclerview;
    private MenuAdapter menuAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Button mBtnPayCoffee;

    private MainPresenter presenter;
    private Basket mBasket;
    private Observable<Basket> mBasketOBS;
    private Subscription subscription;
    private CompositeDisposable disposable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contain_main);
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
        iv_preview_card_top_arrow = (ImageView) findViewById(R.id.iv_preview_card_top_arrow);
        Button mButtonMyProfile = (Button) findViewById(R.id.btn_profile);
        mButtonMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "On Profile Click", Toast.LENGTH_SHORT).show();
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
        appBarLayout = (AppBarLayout) findViewById(R.id.peak_view);

        android.view.Display display = ((android.view.WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        FrameLayout FlBottomSheetBehavior = (FrameLayout) findViewById(R.id.fl_sheet_content);
        ViewGroup.LayoutParams params = FlBottomSheetBehavior.getLayoutParams();
        params.height = (int) (display.getHeight() * 0.90);
        FlBottomSheetBehavior.setLayoutParams(params);
        mBottomSheetBehavior = BottomSheetBehavior.from(FlBottomSheetBehavior);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    appBarLayout.setExpanded(true);
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
        recyclerview = (RecyclerView) findViewById(R.id.rv_coffee_menu);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(linearLayoutManager);
        SpaceItemDecoration decorator = new SpaceItemDecoration(32, true, true);
        recyclerview.addItemDecoration(decorator);
        recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisiblePosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if (firstVisiblePosition == 0) {
                        appBarLayout.setExpanded(true, true);
                    }
                }
            }
        });
        mBtnPayCoffee = (Button) findViewById(R.id.btn_coffee_menu_pay);
        mBtnPayCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                intent.putExtra(MapsActivity.PLACE_ID_EXTRA, mLastPlace.getId());
                startActivity(intent);
            }
        });
        mapFragment.getMapAsync(this);
    }

    void setAdapter(Place place) {
        menuAdapter = new MenuAdapter(createMenu(place), this);
        recyclerview.getRecycledViewPool().clear();
        recyclerview.setAdapter(menuAdapter);
        for (int i = menuAdapter.getGroups().size() - 1; i >= 0; i--) {
            expandGroup(i);
        }
        menuAdapter.notifyDataSetChanged();
        initBasket();
    }

    public void expandGroup(int gPos) {
        if (menuAdapter.isGroupExpanded(gPos)) {
            return;
        }
        menuAdapter.toggleGroup(gPos);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        presenter.getPlaces();
        enableMyLocation();
        getLastKnownLocation();
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showPeakView(mRealm.copyFromRealm(mRealm.where(Place.class).equalTo("id", Integer.valueOf(marker.getSnippet())).findFirst()));
                presenter.getPlace(marker.getSnippet());
                peakView = (AppBarLayout) findViewById(R.id.peak_view);
                View previewTopElements = findViewById(R.id.preview_top_elements);
                mBottomSheetBehavior.setPeekHeight(previewTopElements.getHeight() + peakView.getHeight() + 65);
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
                    peakView.requestLayout();
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

    private void getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(false);
        bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        if (bestLocation != null) {
            mLastKnownLocation = new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude());
        }
        if (mMap != null && mLastKnownLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLastKnownLocation, DEFAULT_MAP_ZOOM));
        } else {
            Toast.makeText(this, "Неудалось определить местоположение", Toast.LENGTH_SHORT).show();
        }
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
    public void onClick(View v, Products product) {
        Intent intent = new Intent(getApplicationContext(), CoffeeIngredientsActivity.class);
        intent.putExtra(PRODUCT_EXTRA, product);
        intent.putExtra(INGREDIENTS_EXTRA, mLastPlace);
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

    public void showPeakView(Place place) {
        mLastPlace = place;
        tv_coffee_name = (TextView) findViewById(R.id.tv_preview_card_product_name);
        tv_coffee_name.setText(mLastPlace.getName());
        ivPreviewBottomStatus = (ImageView) findViewById(R.id.ivPreviewBottomStatus);
        tv_coffee_address = (TextView) findViewById(R.id.tv_preview_card_place_address);
        tv_coffee_address.setText(mLastPlace.getAddress());
        tv_coffee_phone_number = (TextView) findViewById(R.id.tv_preview_card_place_phone_number);
        tv_coffee_phone_number.setText(mLastPlace.getPhone());
        tv_preview_card_place_average_time = (TextView) findViewById(R.id.tv_preview_card_place_average_time);
        tv_preview_card_place_average_time.setText(mLastPlace.getAverage_time() + " мин");

        tvPreviewBottomJobTime = (TextView) findViewById(R.id.tvPreviewBottomJobTime);
        tvPreviewBottomRateCount = (TextView) findViewById(R.id.tv_preview_card_place_rate_count);
        tvPreviewBottomRateCount.setText(String.valueOf(mLastPlace.getRating()));
        tvPreviewBottomRangeCount = (TextView) findViewById(R.id.tv_preview_card_place_range_count);
        if (mLastKnownLocation != null) {
            int distance = MapsUtils.calculationDistance(mLastKnownLocation, new LatLng(mLastPlace.getCoodrinates().getLatitude(),
                    mLastPlace.getCoodrinates().getLongitude()));
            tvPreviewBottomRangeCount.setText(MapsUtils.castDistance(distance));
        } else {
            tvPreviewBottomRangeCount.setText("fail");
        }
        initDate();
        initBasket();
    }

    @Override
    protected void onStop() {
        if (disposable != null) {
            disposable.dispose();
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
        disposable = new CompositeDisposable();
        disposable.add(mBasketOBS
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Basket>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Basket basket) throws Exception {
                        mBasket = basket;
                        if (mBasket != null) {
                            mBtnPayCoffee.setText(String.valueOf(mBasket.getmBasketProductsList().size())
                                    + " Позиции " + mBasket.getSumma(mBasket));
                            if (mBasket.getmBasketProductsList().size() == 0) {
                                mBtnPayCoffee.setEnabled(false);
                            } else {
                                mBtnPayCoffee.setEnabled(true);
                            }
                        } else {
                            mBtnPayCoffee.setEnabled(false);
                        }
                        mBtnPayCoffee.setText("В заказе " + String.valueOf(mBasket.getmBasketProductsList().size())
                                + " напитка (" + mBasket.getSumma(mBasket) + "Р)");
                    }
                }));
    }
}