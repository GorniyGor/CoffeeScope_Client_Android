package com.example.adm1n.coffeescope.profile.change_password;

interface IChangePasswordView {
    void setButtonEnabled(boolean enabled);

    void passwordChanged();

    void showError(String error);

    void oldPasswordError();
}
