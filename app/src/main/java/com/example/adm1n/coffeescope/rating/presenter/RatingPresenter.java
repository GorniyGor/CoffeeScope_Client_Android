package com.example.adm1n.coffeescope.rating.presenter;

import com.example.adm1n.coffeescope.App;
import com.example.adm1n.coffeescope.network.BaseResponse;
import com.example.adm1n.coffeescope.rating.view.IRatingView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by adm1n on 31.10.2017.
 */

public class RatingPresenter implements IRatingPresenter {

    private IRatingView view;

    public RatingPresenter(IRatingView view) {
        this.view = view;
    }

    @Override
    public void sendRating(String comment, Integer placeId, String valuation) {
        App.getPrivateApi().sendRating(placeId, comment, valuation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(@NonNull BaseResponse baseResponse) throws Exception {
                        view.showOk();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.showError(throwable.getMessage());
                    }
                });
    }
}
