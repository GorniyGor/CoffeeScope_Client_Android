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

    ProfilePresenter(IProfileView v) {
        view = v;
        view.setFields("Тест1", "Тест2", "Тест3");
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
                            view.setFields(
                                    profileResponse.getData().getFirstName(),
                                    profileResponse.getData().getLastName(),
                                    profileResponse.getData().getEmail()
                            );
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
        App.getPrivateApi().editProfile(firstName, lastName, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EditProfileResponse>() {
                    @Override
                    public void accept(@NonNull EditProfileResponse editProfileResponse) throws Exception {
                        if (editProfileResponse.getProfile() != null) {
                            view.setFields(
                                    editProfileResponse.getProfile().getFirstName(),
                                    editProfileResponse.getProfile().getLastName(),
                                    editProfileResponse.getProfile().getEmail()
                            );
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
    }
}