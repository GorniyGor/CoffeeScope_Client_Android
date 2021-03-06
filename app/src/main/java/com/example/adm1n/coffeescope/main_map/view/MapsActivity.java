package com.example.adm1n.coffeescope.main_map.view;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.models.Product;
import com.example.adm1n.coffeescope.models.basket.Basket;
import com.example.adm1n.coffeescope.order.view.OrderActivity;
import com.example.adm1n.coffeescope.rating.RatingActivity;
import com.example.adm1n.coffeescope.search.SearchActivity;
import com.example.adm1n.coffeescope.utils.MapsUtils;
import com.example.adm1n.coffeescope.utils.PermissionUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    private HashMap<Integer, Place> placeHashMap = new HashMap();
    private HashMap<Integer, Marker> mHashMap = new HashMap();

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private boolean mPermissionDenied = false;
    private LatLng mLastKnownLocation;
    private Location bestLocation;

    private BottomSheetBehavior mBottomSheetBehavior;

    private MainPresenter presenter;
    private CompositeDisposable disposable = new CompositeDisposable();

    private CoffeeCardView coffeeCardView;
    private Marker mLastMarker;

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
                    MapsUtils.setMyLocationLatitude(mMap.getMyLocation().getLatitude());
                    MapsUtils.setMyLocationLongitude(mMap.getMyLocation().getLongitude());
                }
            }
        });
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
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        Button mButtonApply = (Button) findViewById(R.id.btn_apply);
        mButtonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getPlaces();
                Toast.makeText(MapsActivity.this, "Refresh Place", Toast.LENGTH_SHORT).show();
            }
        });
        mButtonApply.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RatingActivity.class);
                startActivity(intent);
                return false;
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //initBottomSheet

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
                    coffeeCardView.setExpanded(true);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset > 0.5) coffeeCardView.rotateArrow(-1);
                else coffeeCardView.rotateArrow(1);
            }
        });

        coffeeCardView.setOnOrderButtonClickListener(new View.OnClickListener() {
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
                showPreviewCard(Integer.valueOf(marker.getSnippet()));
                return false;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mBottomSheetBehavior != null) {
                    if (mLastMarker != null) {
                        Place lastPlace = placeHashMap.get(Integer.valueOf(mLastMarker.getSnippet()));
                        if (lastPlace != null && lastPlace.getIcon() != null) {
                            mLastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(lastPlace.getIcon()));
                        }
                    }
                    switch (mBottomSheetBehavior.getState()) {
                        case (BottomSheetBehavior.STATE_COLLAPSED):
                        case (BottomSheetBehavior.STATE_EXPANDED):
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            break;
                    }
                }
            }
        });
    }

    public void showPeakView(Place place) {
        mLastPlace = place;

        coffeeCardView.setPlace(mLastPlace);

        if (mMap.getMyLocation() != null) {
            int distance = MapsUtils.calculationDistance(
                    new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()),
                    new LatLng(mLastPlace.getCoodrinates().getLatitude(), mLastPlace.getCoodrinates().getLongitude())
            );
            coffeeCardView.setDistance(MapsUtils.castDistance(distance));
        } else {
            coffeeCardView.setDistance("fail");
        }
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

    /**
     * Last redaction 01.11.17 12:57 by Egor
     * (add ripple effect on item product click)
     *
     */

    @Override
    public void onClick(View v, Product product) {

        //--For other version there applying a ripple effect written in xml
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            //--анимация при нажатии
            ObjectAnimator animation = ObjectAnimator.ofFloat(v, View.ALPHA, 1, 0.1f);
            animation.setRepeatMode(ValueAnimator.REVERSE);
            animation.setRepeatCount(1);
            animation.start();
            //---
        }


        Intent intent = new Intent(getApplicationContext(), CoffeeIngredientsActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(PRODUCT_ID_EXTRA, product.getId());
        intent.putExtra(PARAM_EXTRA, CoffeeIngredientsFragment.Param.Add);
        intent.putExtra(PLACE_ID_EXTRA, mLastPlace.getId());

        //--Transaction between the screens animation
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            // Анимация, при которой название продукта переплывает в название экрана (на туллбаре),
            // который открывается после нажатия
            /*TextView view = (TextView) findViewById(R.id.tv_napitok_name);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, view, view.getTransitionName());*/


            //Анимация, при которой открывающийся экран выезжает справа
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent, options.toBundle());
        }
        else {
            startActivity(intent);
        }
        /*startActivity(intent);*/
    }

    @Override
    public void setMarkers(ArrayList<Place> list) {
        placeHashMap.clear();
        mHashMap.clear();
        for (int i = 0; i < list.size(); i++) {
            Place place = list.get(i);
            if (place.getImage().getLable() != null) {
                place = createAndSaveBitmap(place);
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(place.getCoodrinates().getLatitude(), place.getCoodrinates().getLongitude()))
                        .icon(BitmapDescriptorFactory.fromBitmap(place.getIcon()))
                        .snippet(String.valueOf(place.getId())));
                mHashMap.put(place.getId(), marker);
            } else {
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(place.getCoodrinates().getLatitude(), place.getCoodrinates().getLongitude()))
                        .snippet(String.valueOf(place.getId())));
                mHashMap.put(place.getId(), marker);
            }
            placeHashMap.put(place.getId(), place);
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

    @Override
    public void setMenuAdapter(Place place) {
        setAdapter(place);
    }

    void initBasket() {
        Observable<Basket> mBasketOBS = presenter.getBasket(mLastPlace.getId());
        if (mBasketOBS != null) {
            disposable.add(mBasketOBS
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Basket>() {
                        @Override
                        public void accept(Basket basket) throws Exception {
                            coffeeCardView.updateBaster(basket);
                        }
                    }));
        } else {
            Toast.makeText(this, "Ошибка RX", Toast.LENGTH_SHORT).show();
        }
    }

    Place createAndSaveBitmap(Place place) {
        try {
            Bitmap bmImg = Ion.with(getApplicationContext())
                    .load(place.getImage().getLable()).asBitmap().get();

            Bitmap scaledNormalBitmap = Bitmap.createScaledBitmap(bmImg, 70, 70, false);
            Bitmap scaledBigBitmap = Bitmap.createScaledBitmap(bmImg, 110, 110, false);
            place.setIcon(scaledNormalBitmap);
            place.setIconBig(scaledBigBitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return place;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100: {
                if (resultCode == android.app.Activity.RESULT_OK) {
                    Integer search_result_place_id = data.getIntExtra("EXTRA_SEARCH_ID", 0);
                    showPreviewCard(search_result_place_id);
                }
            }
        }
    }

    public void showPreviewCard(Integer placeId) {
        presenter.getPlace(String.valueOf(placeId));// Идем на сервак за меню!
        mBottomSheetBehavior.setPeekHeight(coffeeCardView.getHeaderHeight());
        Place currentPlaceForBitmap = placeHashMap.get(placeId);
        Place placeFromRealm = presenter.getPlaceFromRealm(placeId);
        showPeakView(placeFromRealm);
        Marker currentMarker = mHashMap.get(placeId);

        //устанавливаем увеличенную иконку на текущий маркер
        if (currentMarker != null) {
            if (currentPlaceForBitmap.getIcon() != null && currentPlaceForBitmap.getIconBig() != null) {
                currentMarker.setIcon(BitmapDescriptorFactory.fromBitmap(currentPlaceForBitmap.getIconBig()));
            }
        }

        //устанавливаем уменьшенную иконку на предыдущий маркер
        if (mLastMarker != null && mLastMarker != currentMarker) {
            Place lastPlace = placeHashMap.get(Integer.valueOf(mLastMarker.getSnippet()));
            if (lastPlace.getIcon() != null) {
                mLastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(lastPlace.getIcon()));
            }
        }

        if (mBottomSheetBehavior != null) {
            switch (mBottomSheetBehavior.getState()) {
                case (BottomSheetBehavior.STATE_HIDDEN):
                case (BottomSheetBehavior.STATE_EXPANDED):
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
            }
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(currentPlaceForBitmap.getCoodrinates().getLatitude(),
                        currentPlaceForBitmap.getCoodrinates().getLongitude()), DEFAULT_MAP_ZOOM));
        mLastPlace = placeFromRealm;
        mLastMarker = currentMarker;
    }
}