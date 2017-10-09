package com.example.adm1n.coffeescope.profile;

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
    public void saveProfile(String firstName, String lastName, String email) {
        currentFirstName = firstName;
        currentLastName = lastName;
        currentEmail = email;
        view.setRx(currentFirstName, currentLastName, currentEmail);
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
