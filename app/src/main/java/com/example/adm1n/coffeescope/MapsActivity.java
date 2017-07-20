package com.example.adm1n.coffeescope;

import android.Manifest;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final LatLng DEFAULT_RED_SQUARE = new LatLng(55.753922, 37.620783);
    private final LatLng MARKER3 = new LatLng(55.779781, 37.609029);
    private final int DEFAULT_MAP_ZOOM = 10;
    private final String coffeeLatLng[] = {"38.961814, -77.036347", "38.923592, -76.975781", "38.910040, -76.971516",
            "38.897782, -77.017332", "38.904939, -77.037914", "55.780813, 37.605429", "55.779516, 37.601032", "55.781724, 37.598918",
            "55.779781, 37.609029", "55.770781, 37.609223"};

    private ArrayList<Coffee> coffeeList = new ArrayList<>();

    //map
    private Button mButtonPlusZoom;
    private Button mButtonMinusZoom;
    private Button mButtonMyLocate;
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
    private CoffeeAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contain_main);
        createCoffee();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
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
                if (mMap.getMyLocation() != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()), DEFAULT_MAP_ZOOM));
                }
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new CoffeeAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(linearLayoutManager);
        SpaceItemDecoration decorator = new SpaceItemDecoration(32, true, true);
        recyclerview.addItemDecoration(decorator);
        recyclerview.setAdapter(mAdapter);
        appBarLayout = (AppBarLayout) findViewById(R.id.peakView);
        FrameLayout parentThatHasBottomSheetBehavior = (FrameLayout) findViewById(R.id.fl_sheet_content);
        mBottomSheetBehavior = BottomSheetBehavior.from(parentThatHasBottomSheetBehavior);
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
        mapFragment.getMapAsync(this);
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
                mBottomSheetBehavior.setPeekHeight(peakView.getHeight() + tvCoffeName.getHeight() + ivPreviewArrowTop.getHeight());
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

    LatLng castLatLngToString(String latLng) {
        String[] newLatLng = latLng.split(",");
        double latitude = Double.parseDouble(newLatLng[0]);
        double longitude = Double.parseDouble(newLatLng[1]);
        LatLng coffeeLatLng = new LatLng(latitude, longitude);
        return coffeeLatLng;
    }

    void setMarkers() {
        for (int i = 0; i < coffeeList.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(castLatLngToString(coffeeList.get(i).getCoordinate())).snippet(String.valueOf(i)));
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
        tvPreviewBottomRangeCount.setText(String.valueOf(
                calculationDistance(mLastKnownLocation, castLatLngToString(coffee.getCoordinate()))));
    }

    public static double calculationDistance(LatLng StartP, LatLng EndP) {
        return calculationDistanceByCoord(StartP.latitude, StartP.longitude, EndP.latitude, EndP.longitude);
    }

    private static double calculationDistanceByCoord(double startPointLat, double startPointLon, double endPointLat, double endPointLon) {
        float[] results = new float[1];
        Location.distanceBetween(startPointLat, startPointLon, endPointLat, endPointLon, results);
        return results[0];
    }
}
