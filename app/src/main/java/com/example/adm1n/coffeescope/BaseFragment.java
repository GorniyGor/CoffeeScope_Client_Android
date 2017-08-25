package com.example.adm1n.coffeescope;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.adm1n.coffeescope.models.Place;

import io.realm.Realm;

/**
 * Created by adm1n on 25.08.2017.
 */

public class BaseFragment extends Fragment {
    public static final String PLACE_EXTRA = "PLACE_EXTRA";
    public static final String PRODUCT_EXTRA = "PRODUCT_EXTRA";
    public static final String PLACE_ID_EXTRA = "PLACE_ID_EXTRA";

    protected Place mLastPlace;
    protected Realm mRealm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onStop() {
        mRealm.close();
        super.onStop();
    }
}
