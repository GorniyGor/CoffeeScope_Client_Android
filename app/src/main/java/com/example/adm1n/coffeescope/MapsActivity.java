package com.example.adm1n.coffeescope;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm1n.coffeescope.coffee_menu.MenuAdapter;
import com.example.adm1n.coffeescope.coffee_menu.custom_model.CoffeeMenu;
import com.example.adm1n.coffeescope.model.Categories;
import com.example.adm1n.coffeescope.model.Products;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, MenuAdapter.OnProductClick {

    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final LatLng DEFAULT_RED_SQUARE = new LatLng(55.753922, 37.620783);
    private final int DEFAULT_MAP_ZOOM = 13;
    private final String coffeeLatLng[] = {"38.961814, -77.036347", "38.923592, -76.975781", "38.910040, -76.971516",
            "38.897782, -77.017332", "38.904939, -77.037914", "55.780813, 37.605429", "55.779516, 37.601032", "55.781724, 37.598918",
            "55.779781, 37.609029", "55.770781, 37.609223"};

    private ArrayList<Coffee> coffeeList = new ArrayList<>();
    private ArrayList<CoffeeMenu> coffeeMenuList;
    private ArrayList<Categories> categoriesList = new ArrayList<>();

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
    private View peakView;
    private TextView tvCoffeName;
    private TextView tvPreviewBottomJobTime;
    private TextView tvPreviewBottomRateCount;
    private TextView tvPreviewBottomRangeCount;
    private TextView tv_coffee_name;
    private TextView tv_coffee_address;
    private TextView tv_coffee_phone_number;
    private AppBarLayout appBarLayout;
    private ImageView ivPreviewArrowTop;
    private BottomSheetBehavior mBottomSheetBehavior;

    private RecyclerView recyclerview;
    private MenuAdapter menuAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contain_main);
        createCoffee();
        createCategory();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //init View
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
        appBarLayout = (AppBarLayout) findViewById(R.id.peakView);

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

        tvCoffeName = (TextView) findViewById(R.id.tv_coffee_name);
        ivPreviewArrowTop = (ImageView) findViewById(R.id.ivPreviewArrowTop);

        //initRecycler
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        menuAdapter = new MenuAdapter(getData(), this);
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
        recyclerview.setAdapter(menuAdapter);
        for (int i = menuAdapter.getGroups().size() - 1; i >= 0; i--) {
            expandGroup(i);
        }

        mapFragment.getMapAsync(this);
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
        enableMyLocation();
        getLastKnownLocation();
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                peakView = findViewById(R.id.peakView);
                View previewTopElements = findViewById(R.id.previewTopElements);
                mBottomSheetBehavior.setPeekHeight(peakView.getHeight() + previewTopElements.getHeight());
                initPeakView(marker);
                if (mBottomSheetBehavior != null) {
                    switch (mBottomSheetBehavior.getState()) {
                        case (BottomSheetBehavior.STATE_HIDDEN):
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            peakView.requestLayout();
                            break;
                        case (BottomSheetBehavior.STATE_COLLAPSED):
                            break;
                        case (BottomSheetBehavior.STATE_EXPANDED):
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            peakView.requestLayout();
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
        setMarkers();
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

    void createCoffee() {
        for (int i = 0; i < 10; i++) {
            Coffee coffee = new Coffee();
            coffee.setName("Coffee" + i);
            coffee.setAdress("Улица Старая Басманная д." + i);
            coffee.setNumber("+7 (903) 999 99 9" + i);
            coffee.setJobTime("22-3" + i);
            coffee.setRating(String.valueOf(i));
            coffee.setRange(String.valueOf(i) + "000");
            coffee.setCoordinate(coffeeLatLng[i]);
            coffeeList.add(coffee);
        }
    }

    void setMarkers() {
        for (int i = 0; i < coffeeList.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(MapsUtils.castLatLngToString(coffeeList.get(i).getCoordinate()))
                    .snippet(String.valueOf(i)));
        }
    }

    void initPeakView(Marker marker) {
        int id = Integer.parseInt(marker.getSnippet());
        Coffee coffee = coffeeList.get(id);
        tv_coffee_name = (TextView) findViewById(R.id.tv_coffee_name);
        tv_coffee_name.setText(coffee.getName());
        tv_coffee_address = (TextView) findViewById(R.id.tv_coffe_address);
        tv_coffee_address.setText(coffee.getAdress());
        tv_coffee_phone_number = (TextView) findViewById(R.id.tv_coffe_phone_number);
        tv_coffee_phone_number.setText(coffee.getNumber());

        tvPreviewBottomJobTime = (TextView) findViewById(R.id.tvPreviewBottomJobTime);
        tvPreviewBottomJobTime.setText(coffee.getJobTime());
        tvPreviewBottomRateCount = (TextView) findViewById(R.id.tvPreviewBottomRateCount);
        tvPreviewBottomRateCount.setText(coffee.getRating());
        tvPreviewBottomRangeCount = (TextView) findViewById(R.id.tvPreviewBottomRangeCount);
        if (mLastKnownLocation != null) {
            tvPreviewBottomRangeCount.setText(String.valueOf(
                    MapsUtils.calculationDistance(mLastKnownLocation, MapsUtils.castLatLngToString(coffee.getCoordinate()))) + "м");
        } else {
            tvPreviewBottomRangeCount.setText("fail");
        }
    }

    List<CoffeeMenu> getData() {
        coffeeMenuList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            CoffeeMenu coffeeMenu = new CoffeeMenu(categoriesList.get(i).getName(), categoriesList.get(i).getProducts());
            coffeeMenuList.add(coffeeMenu);
        }
        return coffeeMenuList;
    }

    void createCategory() {
        for (int i = 0; i < 2; i++) {
            Categories categories = new Categories();
            categories.setId(i);
            categories.setName("STALIN COFFEE" + i);
            categories.setProducts(createProducts());
            categoriesList.add(categories);
        }
    }

    ArrayList<Products> createProducts() {
        ArrayList<Products> productList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Products products = new Products();
            products.setId(i);
            products.setName("COFFEE" + i);
            products.setPrice(i);
            productList.add(products);
        }
        return productList;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), ViborNapitka.class);
        startActivity(intent);
    }
}
