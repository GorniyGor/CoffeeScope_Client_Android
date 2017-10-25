package com.example.adm1n.coffeescope.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.adm1n.coffeescope.BaseFragment;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.models.Place;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by adm1n on 23.10.2017.
 */

public class SearchAddressFragment extends BaseFragment implements SearchAdapter.OnPlaceClickListener {

    private RecyclerView rlSearchAddress;
    private SearchAdapter mAdapter;
    private LinearLayout llGroup;
    private ArrayList<Place> places;
    private ArrayList<Place> placesSearchResult = new ArrayList<>();
    private EditText etSearch;

    public static SearchAddressFragment newInstance(ArrayList<Place> placeList) {
        Bundle args = new Bundle();
        SearchAddressFragment fragment = new SearchAddressFragment();
        args.putParcelableArrayList("VALUEADDRESS", placeList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            places = getArguments().getParcelableArrayList("VALUEADDRESS");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_address, null);
        etSearch = (EditText) getParentFragment().getView().findViewById(R.id.et_search_text);
        rlSearchAddress = (RecyclerView) view.findViewById(R.id.rlSearchAddress);
        llGroup = (LinearLayout) view.findViewById(R.id.llEmptySearchResultAddress);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SearchAdapter(placesSearchResult, this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rlSearchAddress.setLayoutManager(linearLayoutManager);
        rlSearchAddress.setItemAnimator(itemAnimator);
        rlSearchAddress.setAdapter(mAdapter);
        RxTextView
                .afterTextChangeEvents(etSearch)
                .subscribe(new Consumer<TextViewAfterTextChangeEvent>() {
                    @Override
                    public void accept(@NonNull TextViewAfterTextChangeEvent textEvent) throws Exception {
                        placesSearchResult.clear();
                        for (int i = 0; i < places.size(); i++) {
                            Place place = places.get(i);
                            String address = place.getAddress();
                            if (address.contains(String.valueOf(textEvent.editable()))) {
                                placesSearchResult.add(place);
                            }
                        }
                        if (placesSearchResult.isEmpty()) {
                            showEmptyBanner();
                        } else {
                            hideEmptyBanner();
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    void showEmptyBanner() {
        rlSearchAddress.setVisibility(View.GONE);
        llGroup.setVisibility(View.VISIBLE);
    }

    void hideEmptyBanner() {
        rlSearchAddress.setVisibility(View.VISIBLE);
        llGroup.setVisibility(View.GONE);
    }

    @Override
    public void onPlaceClick(int position) {

    }
}
