package com.example.adm1n.coffeescope.introduction.presenter;

/**
 * Created by adm1n on 04.09.2017.
 */

public interface IIntroductionPresenter {
    void login(String email, String password);
    void registration(String lastName, String name, String email, String password, String confirmPassword);
    void resetPassword(String eMail);
}
