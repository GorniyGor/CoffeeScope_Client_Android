package com.example.adm1n.coffeescope;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adm1n.coffeescope.models.Place;

import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by adm1n on 11.08.2017.
 */

public class BaseActivity extends AppCompatActivity {
    public static final String PLACE_EXTRA = "PLACE_EXTRA";
    public static final String PRODUCT_ID_EXTRA = "PRODUCT_ID_EXTRA";
    public static final String PLACE_ID_EXTRA = "PLACE_ID_EXTRA";
    public static final String PLACE_NAME_EXTRA = "PLACE_NAME_EXTRA";
    public static final String PARAM_EXTRA = "PARAM_EXTRA";

    protected Place mLastPlace;
    protected Realm mRealm = null;
    protected Toolbar toolbar;

    //toolbar
    protected TextView toolbarTitle;
    protected ImageView toolbarBackButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mLastPlace = savedInstanceState.getParcelable(PLACE_EXTRA);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
