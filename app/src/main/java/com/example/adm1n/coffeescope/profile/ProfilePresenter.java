package com.example.adm1n.coffeescope.profile;

import com.example.adm1n.coffeescope.App;
import com.example.adm1n.coffeescope.network.responses.ProfileResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

class ProfilePresenter implements IProfilePresenter {
    private String currentFirstName;
    private String currentLastName;
    private String currentEmail;

    private IProfileView view;

    ProfilePresenter(IProfileView v) {
        currentFirstName = "Борис";
        currentLastName = "Дядька";
        currentEmail = "e@e.e";

        view = v;
    }

    @Override
    public void getProfile() {
        App.getApiInterface().getProfile()
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
    public void saveProfile(String firstName, String lastName, String email) {
        currentFirstName = firstName;
        currentLastName = lastName;
        currentEmail = email;
        view.setFields(currentFirstName, currentLastName, currentEmail);
    }

    @Override
    public String getCurrentFirstName() {
        return currentFirstName;
    }

    @Override
    public String getCurrentLastName() {
        return currentLastName;
    }

    @Override
    public String getCurrentEmail() {
        return currentEmail;
    }
}
