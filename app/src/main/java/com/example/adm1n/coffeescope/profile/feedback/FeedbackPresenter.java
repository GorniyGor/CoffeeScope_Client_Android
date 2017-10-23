package com.example.adm1n.coffeescope.profile.feedback;

import android.util.Log;

import com.example.adm1n.coffeescope.App;
import com.example.adm1n.coffeescope.network.BaseResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FeedbackPresenter implements IFeedbackPresenter {

    private final IFeedbackView view;

    FeedbackPresenter(IFeedbackView v) {
        view = v;
    }

    @Override
    public void sendFeedback(String comment) {
        view.showProgress(true);
        App.getPrivateApi().feedback(comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(@NonNull BaseResponse baseResponse) throws Exception {
                        view.feedbackSent();
                        view.showProgress(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.d("Makaka", throwable.getMessage());
                        view.showProgress(false);
                    }
                });
    }
}
