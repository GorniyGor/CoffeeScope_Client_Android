package com.example.adm1n.coffeescope.profile.change_password;

import java.util.Random;

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
        // change password
        if (new Random().nextBoolean()) {
            view.passwordChanged();
        } else {
            view.showError("Some shit");
            view.setButtonEnabled(true);
        }
    }
}
