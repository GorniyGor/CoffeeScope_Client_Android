package com.example.adm1n.coffeescope.introduction.presenter;

import android.content.Context;
import android.preference.PreferenceManager;

import com.example.adm1n.coffeescope.App;
import com.example.adm1n.coffeescope.Const;
import com.example.adm1n.coffeescope.introduction.authorization.IIntroductionAuthorizationView;
import com.example.adm1n.coffeescope.introduction.registration.IIntroductionRegistrationView;
import com.example.adm1n.coffeescope.introduction.reset_password.IIntroductionResetPasswordView;
import com.example.adm1n.coffeescope.network.responses.AuthResponse;
import com.example.adm1n.coffeescope.network.responses.ResetPassResponse;

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
    private IIntroductionResetPasswordView resetPasswordView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public IntroductionPresenter(Context mContext, IIntroductionAuthorizationView authorizationView) {
        this.mContext = mContext;
        this.authorizationView = authorizationView;
    }

    public IntroductionPresenter(Context mContext, IIntroductionRegistrationView registrationView) {
        this.mContext = mContext;
        this.registrationView = registrationView;
    }

    public IntroductionPresenter(Context mContext, IIntroductionResetPasswordView resetPasswordView) {
        this.mContext = mContext;
        this.resetPasswordView = resetPasswordView;
    }

    @Override
    public void login(String email, String password) {
        App.getPrivateApi().authorization(email, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AuthResponse>() {
                    @Override
                    public void accept(@NonNull AuthResponse authResponse) throws Exception {
                        if (authResponse.getStatus().equals("success")) {
                            saveToken(authResponse.getAccessToken(), authResponse.getToken_type());
                            authorizationView.successLogin();
                        } else {
                            authorizationView.showError(authResponse.getFirstError());
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
        App.getPrivateApi().registration(lastName, name, email, password, confirmPassword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AuthResponse>() {
                    @Override
                    public void accept(@NonNull AuthResponse authResponse) throws Exception {
                        if (authResponse.getStatus().equals("success")) {
                            saveToken(authResponse.getAccessToken(), authResponse.getToken_type());
                            registrationView.onRegistrationFinish();
                        } else {
                            registrationView.showError(authResponse.getFirstError());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        registrationView.showError(throwable.getMessage());
                    }
                });
    }

    @Override
    public void resetPassword(String eMail) {
        App.getPrivateApi().resetPassword(eMail)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResetPassResponse>() {
                    @Override
                    public void accept(@NonNull ResetPassResponse resetPassResponse) throws Exception {
                        if (resetPassResponse.getStatus().equals("success")) {
                            resetPasswordView.onResetSuccess();
                        } else {
//                            resetPasswordView.showError(resetPassResponse.getFirst_error());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        resetPasswordView.showError(throwable.getMessage());
                    }
                });
    }

    public void onStop() {
        compositeDisposable.dispose();
    }

    private void saveToken(String token, String type) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(Const.API_TOKEN, token)
                .apply();

        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(Const.API_TOKEN_TYPE, type)
                .apply();
    }
}
