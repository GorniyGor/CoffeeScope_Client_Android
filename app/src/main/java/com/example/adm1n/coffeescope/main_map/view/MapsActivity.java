package com.example.adm1n.coffeescope.main_map.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
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

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private boolean mPermissionDenied = false;
    private LatLng mLastKnownLocation;
    private Location bestLocation;

    private BottomSheetBehavior mBottomSheetBehavior;

    private MainPresenter presenter;
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
                presenter.getPlaces();
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
                if (slideOffset > 0.5) coffeeCardView.rotateArrow(180);
                else coffeeCardView.rotateArrow(0);
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
                presenter.getPlace(marker.getSnippet());
                mBottomSheetBehavior.setPeekHeight(coffeeCardView.getHeaderHeight());
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
            if (place.getImage().getLable() != null) {
                try {
                    Bitmap bmImg = Ion.with(getApplicationContext())
                            .load(place.getImage().getLable()).asBitmap().get();

                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(place.getCoodrinates().getLatitude(), place.getCoodrinates().getLongitude()))
                            .icon(BitmapDescriptorFactory.fromBitmap(bmImg))
                            .snippet(String.valueOf(place.getId())));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(place.getCoodrinates().getLatitude(), place.getCoodrinates().getLongitude()))
                        .snippet(String.valueOf(place.getId())));
            }
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
}