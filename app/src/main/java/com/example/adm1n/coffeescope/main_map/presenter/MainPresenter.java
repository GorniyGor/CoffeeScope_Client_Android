package com.example.adm1n.coffeescope.main_map.presenter;

import android.content.Context;

import com.example.adm1n.coffeescope.main_map.model.MainPlacesModel;
import com.example.adm1n.coffeescope.main_map.view.IMapActivity;
import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.network.responses.PlaceResponse;
import com.example.adm1n.coffeescope.network.responses.PlacesResponse;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by adm1n on 01.08.2017.
 */

public class MainPresenter implements IMainPresenter {
    private Context mContext;
    private IMapActivity mView;
    private MainPlacesModel model = new MainPlacesModel();

    public MainPresenter(Context mContext, IMapActivity mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void getPlaces() {
        model.getPlaces()
                .subscribe(new Consumer<PlacesResponse>() {
                    @Override
                    public void accept(@NonNull PlacesResponse placesResponse) throws Exception {
                        mView.setMarkers(placesResponse.getPlaceList());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mView.showError(throwable.getMessage());
                    }
                });
    }

    @Override
    public void getPlace(String placeId) {
        model.getPlace(placeId)
                .subscribe(new Consumer<PlaceResponse>() {
                    @Override
                    public void accept(@NonNull PlaceResponse placeResponse) throws Exception {
                        Place data = placeResponse.getData();
                        mView.showPeakView(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mView.showError(throwable.getMessage());
                    }
                });
    }
}
