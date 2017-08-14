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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.ViborNapitka;
import com.example.adm1n.coffeescope.coffee_menu.MenuAdapter;
import com.example.adm1n.coffeescope.coffee_menu.custom_model.CoffeeMenu;
import com.example.adm1n.coffeescope.main_map.presenter.MainPresenter;
import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.models.Products;
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

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, MenuAdapter.OnProductClick, IMapActivity {

    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final int DEFAULT_MAP_ZOOM = 13;
    public static final String PRODUCT_EXTRA = "PRODUCT_EXTRA";
    public static final String INGREDIENTS_EXTRA = "INGREDIENTS_EXTRA";

    private ArrayList<CoffeeMenu> coffeeMenuList = new ArrayList<>();
    private ArrayList<Place> placeList = new ArrayList<>();
    //map
    private Button mButtonPlusZoom;
    private Button mButtonMinusZoom;
    private Button mButtonMyLocate;
    private Button mButtonMyProfile;
    private Button mButtonSearch;
    private Button mButtonApply;

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private boolean mPermissionDenied = false;
    private LatLng mLastKnownLocation;

    //preViewCardView
    private AppBarLayout peakView;
    private TextView tvPreviewBottomJobTime;
    private TextView tvPreviewBottomRateCount;
    private TextView tvPreviewBottomRangeCount;
    private TextView tv_coffee_name;
    private TextView tv_coffee_address;
    private TextView tv_coffee_phone_number;
    private AppBarLayout appBarLayout;
    private BottomSheetBehavior mBottomSheetBehavior;

    private RecyclerView recyclerview;
    private MenuAdapter menuAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Place mLastPlace;
    private Button mBtnPayCoffee;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contain_main);
        presenter = new MainPresenter(getApplicationContext(), this);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //init map control button
        mButtonPlusZoom = (Button) findViewById(R.id.btn_plus_zoom);
        mButtonPlusZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });
        mButtonMinusZoom = (Button) findViewById(R.id.btn_minus_zoom);
        mButtonMinusZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
        mButtonMyLocate = (Button) findViewById(R.id.btn_my_location);
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

        mButtonMyProfile = (Button) findViewById(R.id.btn_profile);
        mButtonMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "On Profile Click", Toast.LENGTH_SHORT).show();
            }
        });
        mButtonSearch = (Button) findViewById(R.id.btn_search);
        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "On Search Click", Toast.LENGTH_SHORT).show();
            }
        });
        mButtonApply = (Button) findViewById(R.id.btn_apply);
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
        params.height = (int) (display.getHeight() * 0.88);
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
                Toast.makeText(MapsActivity.this, "Click Click", Toast.LENGTH_SHORT).show();
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
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                presenter.getPlace(marker.getSnippet());
                peakView = (AppBarLayout) findViewById(R.id.peak_view);
                View previewTopElements = findViewById(R.id.preview_top_elements);
                mBottomSheetBehavior.setPeekHeight(previewTopElements.getHeight() + peakView.getHeight());
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
        Location bestLocation = null;
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
        Intent intent = new Intent(getApplicationContext(), ViborNapitka.class);
        intent.putExtra(PRODUCT_EXTRA, product);
        intent.putParcelableArrayListExtra(INGREDIENTS_EXTRA, mLastPlace.getIngredients());
        startActivity(intent);
    }

    @Override
    public void setMarkers(ArrayList<Place> list) {
        for (int i = 0; i < list.size(); i++) {
            Place place = list.get(i);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(place.getCoodrinates().getLongitude(), place.getCoodrinates().getLatitude()))
                    .snippet(String.valueOf(place.getId())));
        }
        placeList.addAll(list);
    }

    @Override
    public void showError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPeakView(Place place) {
        mLastPlace = place;
        setAdapter(place);
        tv_coffee_name = (TextView) findViewById(R.id.tv_preview_card_product_name);
        tv_coffee_name.setText(mLastPlace.getName());
        tv_coffee_address = (TextView) findViewById(R.id.tv_preview_card_place_address);
        tv_coffee_address.setText(mLastPlace.getAddress());
        tv_coffee_phone_number = (TextView) findViewById(R.id.tv_preview_card_place_phone_number);
        tv_coffee_phone_number.setText(mLastPlace.getPhone());

        tvPreviewBottomJobTime = (TextView) findViewById(R.id.tvPreviewBottomJobTime);
//        tvPreviewBottomJobTime.setText(mLastPlace.getJobTime());
        tvPreviewBottomRateCount = (TextView) findViewById(R.id.tv_preview_card_place_rate_count);
        tvPreviewBottomRateCount.setText(String.valueOf(mLastPlace.getRating()));
        tvPreviewBottomRangeCount = (TextView) findViewById(R.id.tv_preview_card_place_range_count);
        if (mLastKnownLocation != null) {
            tvPreviewBottomRangeCount.setText(String.valueOf(
                    MapsUtils.calculationDistance(mLastKnownLocation, new LatLng(mLastPlace.getCoodrinates().getLatitude(),
                            mLastPlace.getCoodrinates().getLongitude()))));
        } else {
            tvPreviewBottomRangeCount.setText("fail");
        }
    }
}