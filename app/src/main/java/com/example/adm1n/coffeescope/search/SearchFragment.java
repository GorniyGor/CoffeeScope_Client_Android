package com.example.adm1n.coffeescope.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.adm1n.coffeescope.BaseFragment;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.models.Place;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;

/**
 * Created by adm1n on 23.10.2017.
 */

public class SearchFragment extends BaseFragment implements ISearchView {

    static final int NUM_ITEMS = 2;
    private ArrayList<Place> places = new ArrayList<>();
    private ViewPager vpSearch;
    private ViewPagerAdapter vpAdapter;
    private EditText etSearchText;
    private ImageView ivClear;
    private TabLayout tlAbout;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        places = getPlaceFromRealm();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);
        vpSearch = (ViewPager) view.findViewById(R.id.vp_search);
        tlAbout = (TabLayout) view.findViewById(R.id.tlAbout);
        ivClear = (ImageView) view.findViewById(R.id.ivSearchClearText);
        etSearchText = (EditText) view.findViewById(R.id.et_search_text);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearchText.setText("");
            }
        });
        initViewPager();
    }

    public ArrayList<Place> getPlaceFromRealm() {
        try { // I could use try-with-resources here
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    places = (ArrayList<Place>) mRealm.copyFromRealm(mRealm.where(Place.class).findAll());
                }
            });
        } finally {
            if (mRealm != null) {
                mRealm.close();
            }
        }
        return places;
    }

    @Override
    public void initViewPager() {
        vpAdapter = new ViewPagerAdapter(getChildFragmentManager());
        tlAbout.setupWithViewPager(vpSearch);
        vpSearch.setAdapter(vpAdapter);
        vpSearch.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        static final String NAME = "Название";
        static final String ADRES = "Адрес";

        private HashMap<Integer, Fragment> mPageMap;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mPageMap = new HashMap<>();
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment myFragment;
            switch (position) {
                case 0:
                    myFragment = SearchNameFragment.newInstance(places);
                    break;
                case 1:
                    myFragment = SearchAddressFragment.newInstance(places);
                    break;
                default:
                    myFragment = null;
            }
            mPageMap.put(position, myFragment);
            return myFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return NAME;
                case 1:
                    return ADRES;
                default:
                    return "";
            }
        }
    }
}
