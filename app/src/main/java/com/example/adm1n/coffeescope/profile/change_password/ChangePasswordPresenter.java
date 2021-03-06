package com.example.adm1n.coffeescope.profile.change_password;

import com.example.adm1n.coffeescope.App;
import com.example.adm1n.coffeescope.ErrorKeys;
import com.example.adm1n.coffeescope.network.BaseResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Vorkytaka on 12.10.2017.
 */

public class ChangePasswordPresenter implements IChangePasswordPresenter {

    private IChangePasswordView view;

    ChangePasswordPresenter(IChangePasswordView v) {
        view = v;
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String newPasswordRepeat) {
        view.setButtonEnabled(false);
        App.getPrivateApi().changePassword(oldPassword, newPassword, newPasswordRepeat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(@NonNull BaseResponse response) throws Exception {
                        view.passwordChanged();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        switch (throwable.getMessage()) {
                            case ErrorKeys.OLD_PASSWORD:
                                view.oldPasswordError();
                                view.showError("Неверный пароль");
                                break;
                            default:
                                view.showError("some shit");
                                break;
                        }
                        view.setButtonEnabled(true);
                    }
                });
    }
}
