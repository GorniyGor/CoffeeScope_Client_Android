package com.example.adm1n.coffeescope.profile;

public interface IProfilePresenter {
    void getProfile();

    void saveProfile(String firstName, String lastName, String email);

    String getCurrentFirstName();

    String getCurrentLastName();

    String getCurrentEmail();
}
