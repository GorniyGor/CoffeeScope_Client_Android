package com.example.adm1n.coffeescope.network.responses;

import java.util.List;

/**
 * Created by adm1n on 04.09.2017.
 */

public class AuthResponse {
    private String status;
    private String first_error;
    private String accessToken;
    private String tokenType;
    private String who;
    private Integer expiresIn;
    private List<Object> error = null;
//    private Errors errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public List<Object> getError() {
        return error;
    }

    public void setError(List<Object> error) {
        this.error = error;
    }

    public String getFirst_error() {
        return first_error;
    }

    public void setFirst_error(String first_error) {
        this.first_error = first_error;
    }
}
