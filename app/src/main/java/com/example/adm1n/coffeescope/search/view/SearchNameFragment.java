package com.example.adm1n.coffeescope.search.view;

import android.app.Activity;
import android.content.Intent;
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
import com.example.adm1n.coffeescope.search.SearchAdapter;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;

/**
 * Created by adm1n on 23.10.2017.
 */

public class SearchNameFragment extends BaseFragment implements SearchAdapter.OnPlaceClickListener {

    private RecyclerView rlSearchName;
    private SearchAdapter mAdapter;
    private LinearLayout llGroup;
    private ArrayList<Place> places;
    private ArrayList<Place> placesSearchResult = new ArrayList<>();
    private EditText etSearch;

    public static SearchNameFragment newInstance(ArrayList<Place> placeList) {
        Bundle args = new Bundle();
        SearchNameFragment fragment = new SearchNameFragment();
        args.putParcelableArrayList("VALUE", placeList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            places = getArguments().getParcelableArrayList("VALUE");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_name, null);
        etSearch = (EditText) getParentFragment().getView().findViewById(R.id.et_search_text);
        rlSearchName = (RecyclerView) view.findViewById(R.id.rlSearchName);
        llGroup = (LinearLayout) view.findViewById(R.id.llEmptySearchResultName);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SearchAdapter(placesSearchResult, this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rlSearchName.setLayoutManager(linearLayoutManager);
        rlSearchName.setItemAnimator(itemAnimator);
        rlSearchName.setAdapter(mAdapter);
        RxTextView
                .textChangeEvents(etSearch)
                .filter(new Predicate<TextViewTextChangeEvent>() {
                    @Override
                    public boolean test(@NonNull TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {
                        return textViewTextChangeEvent.count() > 0;
                    }
                })
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TextViewTextChangeEvent>() {
                    @Override
                    public void accept(@NonNull TextViewTextChangeEvent textEvent) throws Exception {
                        if (textEvent.view().getText().length() != 0) {
                            int oldSize = placesSearchResult.size();
                            placesSearchResult.clear();
                            for (int i = 0; i < places.size(); i++) {
                                Place place = places.get(i);
                                String name = place.getName();
                                if (name.contains(String.valueOf(textEvent.text()))) {
                                    placesSearchResult.add(place);
                                }
                            }
                            if (placesSearchResult.size() == 0) {
                                showEmptyBanner();
                            } else {
                                hideEmptyBanner();
                                if (oldSize != 0) {
                                    mAdapter.notifyItemRangeRemoved(0, oldSize);
                                } else {
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
    }

    void showEmptyBanner() {
        rlSearchName.setVisibility(View.GONE);
        llGroup.setVisibility(View.VISIBLE);
    }

    void hideEmptyBanner() {
        rlSearchName.setVisibility(View.VISIBLE);
        llGroup.setVisibility(View.GONE);
    }

    @Override
    public void onPlaceClick(int position) {
        Intent intent = new Intent();
        Integer id = placesSearchResult.get(position).getId();
        intent.putExtra("EXTRA_SEARCH_ID", id);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
