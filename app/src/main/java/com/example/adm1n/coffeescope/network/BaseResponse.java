package com.example.adm1n.coffeescope.network;

import com.example.adm1n.coffeescope.network.responses.Errors;
import com.google.gson.annotations.SerializedName;

/**
 * Created by adm1n on 06.09.2017.
 */

public class BaseResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("first_error")
    public String firstError;
    @SerializedName("error_key")
    public String errorKey;
    @SerializedName("errors")
    public Errors errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
