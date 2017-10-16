package com.example.adm1n.coffeescope.network.responses;

import com.example.adm1n.coffeescope.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by adm1n on 12.10.2017.
 */

public class ResetPassResponse extends BaseResponse {
    private String first_error;
    private String error_key;
    private List<Errors> errors = null;

    public String getFirst_error() {
        return first_error;
    }

    public void setFirst_error(String first_error) {
        this.first_error = first_error;
    }

    public String getError_key() {
        return error_key;
    }

    public void setError_key(String error_key) {
        this.error_key = error_key;
    }

    public List<Errors> getErrors() {
        return errors;
    }

    public void setErrors(List<Errors> errors) {
        this.errors = errors;
    }
}
