package com.example.adm1n.coffeescope.profile;

public interface IProfileView {
    void setFields(String firstName, String lastName, String email);

    void setButtonEnabled(boolean enabled);

    void logout();
}
