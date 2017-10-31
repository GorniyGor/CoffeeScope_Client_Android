package com.example.adm1n.coffeescope.profile;

import com.example.adm1n.coffeescope.App;
import com.example.adm1n.coffeescope.network.responses.EditProfileResponse;
import com.example.adm1n.coffeescope.network.responses.ProfileResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

class ProfilePresenter implements IProfilePresenter {

    private IProfileView view;

    private String currentFirstName = "";
    private String currentLastName = "";
    private String currentEmail = "";

    ProfilePresenter(IProfileView v) {
        view = v;
        setFields();
    }

    @Override
    public void getProfile() {
        App.getPrivateApi().getProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ProfileResponse>() {
                    @Override
                    public void accept(@NonNull ProfileResponse profileResponse) throws Exception {
                        if (profileResponse.getData() != null) {
                            currentFirstName = profileResponse.getData().getFirstName();
                            currentLastName = profileResponse.getData().getLastName();
                            currentEmail = profileResponse.getData().getEmail();
                            setFields();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                });
    }

    @Override
    public void saveProfile(final String firstName, final String lastName, final String email) {
        view.setButtonEnabled(false);
        App.getPrivateApi().editProfile(firstName, lastName, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EditProfileResponse>() {
                    @Override
                    public void accept(@NonNull EditProfileResponse editProfileResponse) throws Exception {
                        if (editProfileResponse.getData() != null) {
                            currentFirstName = editProfileResponse.getData().getFirstName();
                            currentLastName = editProfileResponse.getData().getLastName();
                            currentEmail = editProfileResponse.getData().getEmail();
                            view.showMessage("Профиль успешно изменен");
                        } else {
                            view.setButtonEnabled(true);
                            view.showMessage("Какие-то проблемы");
                        }
                        setFields();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.setButtonEnabled(true);
                        setFields();
                        view.showMessage(throwable.getMessage());
                    }
                });
    }

    @Override
    public void logout() {
        App.logout();
        view.logout();
    }

    private void setFields() {
        view.setFields(
                currentFirstName,
                currentLastName,
                currentEmail
        );
    }
}
