package com.example.adm1n.coffeescope.network.responses;

import com.example.adm1n.coffeescope.models.Profile;

public class ProfileResponse {
    private Profile data;

    public Profile getData() {
        return data;
    }

    public void setData(Profile data) {
        this.data = data;
    }
}
