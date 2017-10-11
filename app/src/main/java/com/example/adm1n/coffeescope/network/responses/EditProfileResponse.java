package com.example.adm1n.coffeescope.network.responses;

import com.example.adm1n.coffeescope.models.Profile;

public class EditProfileResponse {
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
