package com.example.adm1n.coffeescope.profile.change_password;

import com.example.adm1n.coffeescope.App;
import com.example.adm1n.coffeescope.ErrorKeys;
import com.example.adm1n.coffeescope.network.responses.ErrorResponse;

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
                .subscribe(new Consumer<ErrorResponse>() {
                    @Override
                    public void accept(@NonNull ErrorResponse response) throws Exception {
                        //todo: обдумать получение верного и неверного объекта
                        if (response.getStatus().equals("success")) {
                            view.passwordChanged();
                        } else {
                            switch (response.getErrorKey()) {
                                case ErrorKeys.OLD_PASSWORD:
                                    view.oldPasswordError();
                                    view.showError("Неверный пароль");
                                    break;
                            }
                            view.setButtonEnabled(true);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.showError("Some shit");
                        view.setButtonEnabled(true);
                    }
                });
    }
}
