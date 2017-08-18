package com.example.adm1n.coffeescope;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.adm1n.coffeescope.models.Place;

import io.realm.Realm;

/**
 * Created by adm1n on 11.08.2017.
 */

public class BaseActivity extends AppCompatActivity {
    public static final String PLACE_EXTRA = "PLACE_EXTRA";

    protected Place mLastPlace;
    protected Realm mRealm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
        if (savedInstanceState != null) {
            mLastPlace = savedInstanceState.getParcelable(PLACE_EXTRA);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mLastPlace = savedInstanceState.getParcelable(PLACE_EXTRA);
    }
}
