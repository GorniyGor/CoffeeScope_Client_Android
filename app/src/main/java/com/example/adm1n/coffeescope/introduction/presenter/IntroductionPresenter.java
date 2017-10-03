package com.example.adm1n.coffeescope.introduction.presenter;

import android.content.Context;

import com.example.adm1n.coffeescope.App;
import com.example.adm1n.coffeescope.introduction.authorization.IIntroductionAuthorizationView;
import com.example.adm1n.coffeescope.introduction.registration.IIntroductionRegistrationView;
import com.example.adm1n.coffeescope.network.responses.AuthResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by adm1n on 04.09.2017.
 */

public class IntroductionPresenter implements IIntroductionPresenter {

    private Context mContext;
    private IIntroductionAuthorizationView authorizationView;
    private IIntroductionRegistrationView registrationView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public IntroductionPresenter(Context mContext, IIntroductionAuthorizationView authorizationView) {
        this.mContext = mContext;
        this.authorizationView = authorizationView;
    }

    public IntroductionPresenter(Context mContext, IIntroductionRegistrationView registrationView) {
        this.mContext = mContext;
        this.registrationView = registrationView;
    }

    @Override
    public void login(String email, String password) {
        App.getApiInterface().authorization(email, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AuthResponse>() {
                    @Override
                    public void accept(@NonNull AuthResponse authResponse) throws Exception {
                        if (authResponse.getStatus().equals("success")) {
                            authorizationView.successLogin();
                        } else {
                            authorizationView.showError(authResponse.getFirst_error());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        authorizationView.showError(throwable.getMessage());
                    }
                });
    }

    @Override
    public void registration(String lastName, String name, String email, String password, String confirmPassword) {
        App.getApiInterface().registration(lastName, name, email, password, confirmPassword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AuthResponse>() {
                    @Override
                    public void accept(@NonNull AuthResponse authResponse) throws Exception {
                        if (authResponse.getStatus().equals("success")) {
                            registrationView.onRegistrationFinish();
                        } else {
                            registrationView.showError(authResponse.getFirst_error());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        registrationView.showError(throwable.getMessage());
                    }
                });
    }

    public void onStop() {
        compositeDisposable.dispose();
    }
}
