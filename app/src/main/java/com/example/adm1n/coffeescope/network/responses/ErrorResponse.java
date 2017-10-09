package com.example.adm1n.coffeescope.network.responses;

import com.example.adm1n.coffeescope.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by adm1n on 09.10.2017.
 */

public class ErrorResponse extends BaseResponse {
    @SerializedName("first_error")
    public String firstError;
    @SerializedName("error_key")
    public String errorKey;
    @SerializedName("errors")
    public Errors errors;

    public String getFirstError() {
        return firstError;
    }

    public void setFirstError(String firstError) {
        this.firstError = firstError;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}
