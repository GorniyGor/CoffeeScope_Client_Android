package com.example.adm1n.coffeescope.network.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by adm1n on 09.10.2017.
 */

class Errors {
    @SerializedName("email")
    public List<String> errors = null;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
